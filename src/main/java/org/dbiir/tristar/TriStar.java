package org.dbiir.tristar;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.configuration2.HierarchicalConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DisabledListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.tree.ImmutableNode;
import org.apache.commons.configuration2.tree.xpath.XPathExpressionEngine;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.dbiir.tristar.adapter.Flusher;
import org.dbiir.tristar.benchmarks.Phase;
import org.dbiir.tristar.benchmarks.Results;
import org.dbiir.tristar.benchmarks.ThreadBench;
import org.dbiir.tristar.benchmarks.WorkloadConfiguration;
import org.dbiir.tristar.benchmarks.api.*;
import org.dbiir.tristar.benchmarks.types.DatabaseType;
import org.dbiir.tristar.benchmarks.types.State;
import org.dbiir.tristar.benchmarks.util.ClassUtil;
import org.dbiir.tristar.benchmarks.util.FileUtil;
import org.dbiir.tristar.benchmarks.util.JSONSerializable;
import org.dbiir.tristar.benchmarks.util.JSONUtil;
import org.dbiir.tristar.benchmarks.util.ResultWriter;
import org.dbiir.tristar.benchmarks.util.StringUtil;
import org.dbiir.tristar.benchmarks.util.TimeUtil;

import lombok.Setter;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

public class TriStar {
    private static final Logger logger = Logger.getLogger(TriStar.class);
    private static final String RATE_DISABLED = "disabled";
    private static final String RATE_UNLIMITED = "unlimited";
    private static final String SINGLE_LINE = StringUtil.repeat("=", 70);
    private static Thread flushThread;
    private static final AtomicBoolean waitForRespond = new AtomicBoolean(false);
    private static String buffer;

    /* variable and set functions for test */
    @Setter
    private static String type_ = "default";
    @Setter
    private static int terminals_ = -1;
    @Setter
    private static int hotspotNum_ = -1;
    @Setter
    private static double hotspotProbability_ = -1.0;
    @Setter
    private static double zipFain_ = -1.0;
    @Setter
    private static int[] weight_ = null;

    public static void main(String[] args) throws Exception {
        CommandLineParser parser = new DefaultParser();
        XMLConfiguration pluginConfig = buildConfiguration("config/plugin.xml");

        Options options = buildOptions(pluginConfig);

        CommandLine argsLine = parser.parse(options, args);

        if (argsLine.hasOption("h")) {
            printUsage(options);
            return;
        } else if (!argsLine.hasOption("c")) {
            logger.error("Missing Configuration file");
            printUsage(options);
            return;
        } else if (!argsLine.hasOption("b")) {
            logger.error("Missing Benchmark Class to load");
            printUsage(options);
            return;
        }

        // Seconds
        int intervalMonitor = 0;
        if (argsLine.hasOption("im")) {
            intervalMonitor = Integer.parseInt(argsLine.getOptionValue("im"));
        }

        // -------------------------------------------------------------------
        // GET PLUGIN LIST
        // -------------------------------------------------------------------
        String targetBenchmark = argsLine.getOptionValue("b").trim();

        List<BenchmarkModule> benchList = new ArrayList<>();
        
        // Use this list for filtering of the output
        List<TransactionType> activeTXTypes = new ArrayList<>();

        String configFile = argsLine.getOptionValue("c").trim();

        XMLConfiguration xmlConfig = buildConfiguration(configFile);

        WorkloadConfiguration wrkld = loadBenchmark(xmlConfig, pluginConfig, benchList, activeTXTypes, targetBenchmark);

        // Generate the dialect map
        wrkld.init();
        assert benchList.size() == 1;
        // Refresh the catalog.
        for (BenchmarkModule benchmark : benchList) {
            benchmark.refreshCatalog();
        }

        // register transaction templates
        for (BenchmarkModule benchmark : benchList) {
            NettyClientInitializer.benchmark = benchmark;
            EventLoopGroup eventExecutors = new NioEventLoopGroup();
            try {
                System.out.println("connect to txnSails server");
                Bootstrap bootstrap =  new Bootstrap();
                bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new NettyClientInitializer());
                ChannelFuture channelFuture = bootstrap.connect(wrkld.getTxnSailsServerIp(),9876).sync();

                registerTemplateSQLs(channelFuture.channel(), benchmark.getProcedures());
                channelFuture.channel().closeFuture().sync();
            }finally {
                eventExecutors.shutdownGracefully();
            }
        }

        // Execute Workload
        if (isBooleanOptionSet(argsLine, "execute")) {
            try {
                createFlushThread(argsLine, wrkld.getConcurrencyControlType());
                Results r = runWorkload(benchList, intervalMonitor);
                finishFlushThread();
                writeOutputs(r, activeTXTypes, argsLine, xmlConfig);
                writeHistograms(r);

                if (argsLine.hasOption("json-histograms")) {
                    String histogram_json = writeJSONHistograms(r);
                    String fileName = argsLine.getOptionValue("json-histograms");
                    FileUtil.writeStringToFile(new File(fileName), histogram_json);
                    logger.info("Histograms JSON Data: " + fileName);
                }

                if (r.getState() == State.ERROR) {
                    throw new RuntimeException(
                            "Errors encountered during benchmark execution. See output above for details.");
                }
            } catch (Throwable ex) {
                logger.error("Unexpected error when executing config.", ex);
                System.exit(1);
            }

        } else {
            logger.info("Skipping benchmark workload execution");
        }
    }

    private static WorkloadConfiguration loadBenchmark(XMLConfiguration xmlConfig,
                                                       XMLConfiguration pluginConfig,
                                                       List<BenchmarkModule> benchList,
                                                       List<TransactionType> activeTXTypes,
                                                       String targetBenchmark) throws ParseException {
        int lastTxnId = 0;
        // ----------------------------------------------------------------
        // BEGIN LOADING WORKLOAD CONFIGURATION
        // ----------------------------------------------------------------

        WorkloadConfiguration wrkld = new WorkloadConfiguration();
        wrkld.setBenchmarkName(targetBenchmark);
        wrkld.setXmlConfig(xmlConfig);

        if (targetBenchmark.contains("smallbank")) {
            // load smallbank variables
            zipFain_ = zipFain_ > 0 ? zipFain_ : xmlConfig.getDouble("zipf", -1.0);
            hotspotNum_ = hotspotNum_ > 0 ? hotspotNum_ : xmlConfig.getInt("hotspotNumber", -1);
            hotspotProbability_ = hotspotProbability_ > 0 ? hotspotProbability_ : xmlConfig.getDouble("hotspotPercentage", -1.0);
            if (hotspotNum_ > 0) {
                wrkld.setHotspotUseFixedSize(true);
                wrkld.setHotspotFixedSize(hotspotNum_);
                if (hotspotProbability_ > 0)
                    wrkld.setHotspotPercentage(hotspotProbability_);
            }
            if (zipFain_ > 0) {
                wrkld.setZipFainTheta(zipFain_);
            }
        }
        // Pull in database configuration
        wrkld.setDatabaseType(DatabaseType.get(xmlConfig.getString("type")));
        wrkld.setDriverClass(xmlConfig.getString("driver"));
        wrkld.setUrl(xmlConfig.getString("url"));
        wrkld.setUsername(xmlConfig.getString("username"));
        wrkld.setPassword(xmlConfig.getString("password"));
        wrkld.setRandomSeed(xmlConfig.getInt("randomSeed", -1));
        wrkld.setBatchSize(xmlConfig.getInt("batchsize", 128));
        wrkld.setMaxRetries(xmlConfig.getInt("retries", 3));
        wrkld.setNewConnectionPerTxn(xmlConfig.getBoolean("newConnectionPerTxn", false));
        wrkld.setReconnectOnConnectionFailure(
                xmlConfig.getBoolean("reconnectOnConnectionFailure", true));
        if (xmlConfig.containsKey("warehouseSkew")) {
            wrkld.setWarehouseSkew(xmlConfig.getBoolean("warehouseSkew", false));
        }
        if (xmlConfig.containsKey("customerSkew")) {
            wrkld.setWarehouseSkew(xmlConfig.getBoolean("customerSkew", false));
        }

        int terminals = terminals_ > 0 ? terminals_ : xmlConfig.getInt("terminals", 0);
        wrkld.setTerminals(terminals);

        String isolationMode =
                xmlConfig.getString("benchmark[not(@bench)]", "TRANSACTION_SERIALIZABLE");
        wrkld.setIsolationMode(xmlConfig.getString("benchmark", isolationMode));
        wrkld.setScaleFactor(xmlConfig.getDouble("scalefactor", 1.0));
        wrkld.setDataDir(xmlConfig.getString("datadir", "."));
        wrkld.setDDLPath(xmlConfig.getString("ddlpath", null));
        String type = !type_.equals("default") ? type_ : xmlConfig.getString("concurrencyControlType", "SERIALIZABLE");
        wrkld.setConcurrencyControlType(type);
        wrkld.setTxnSailsServerIp(xmlConfig.getString("txnSailsServer", "127.0.0.1"));

        double selectivity = -1;
        try {
            selectivity = xmlConfig.getDouble("selectivity");
            wrkld.setSelectivity(selectivity);
        } catch (NoSuchElementException nse) {
            // Nothing to do here !
        }

        // ----------------------------------------------------------------
        // CREATE BENCHMARK MODULE
        // ----------------------------------------------------------------

        String classname = pluginConfig.getString("/plugin[@name='" + targetBenchmark + "']");

        if (classname == null) {
            throw new ParseException("Plugin " + targetBenchmark + " is undefined in config/plugin.xml");
        }

        BenchmarkModule bench = ClassUtil.newInstance(
                classname, new Object[] {wrkld}, new Class<?>[] {WorkloadConfiguration.class});
        
        // ----------------------------------------------------------------
        // LOAD TRANSACTION DESCRIPTIONS
        // ----------------------------------------------------------------
        int numTxnTypes =
                xmlConfig.configurationsAt("transactiontypes/transactiontype").size();

        List<TransactionType> ttypes = new ArrayList<>();
        ttypes.add(TransactionType.INVALID);
        for (int i = 1; i <= numTxnTypes; i++) {
            String key = "transactiontypes/transactiontype[" + i + "]";
            String txnName = xmlConfig.getString(key + "/name");

            // Get ID if specified; else increment from last one.
            int txnId = i;
            if (xmlConfig.containsKey(key + "/id")) {
                txnId = xmlConfig.getInt(key + "/id");
            }

            long preExecutionWait = 0;
            if (xmlConfig.containsKey(key + "/preExecutionWait")) {
                preExecutionWait = xmlConfig.getLong(key + "/preExecutionWait");
            }

            long postExecutionWait = 0;
            if (xmlConfig.containsKey(key + "/postExecutionWait")) {
                postExecutionWait = xmlConfig.getLong(key + "/postExecutionWait");
            }

            String transactionIsolation = "TRANSACTION_SERIALIZABLE";
            if (xmlConfig.containsKey(key + "/transactionIsolation")) {
                transactionIsolation = xmlConfig.getString(key + "/transactionIsolation");
            }

            // After load
            if (xmlConfig.containsKey("afterload")) {
                bench.setAfterLoadScriptPath(xmlConfig.getString("afterload"));
            }

            TransactionType tmpType =
                    bench.initTransactionType(
                            txnName, txnId + lastTxnId, preExecutionWait, postExecutionWait, transactionIsolation);

            // Keep a reference for filtering
            activeTXTypes.add(tmpType);

            // Add a ref for the active TTypes in this benchmark
            ttypes.add(tmpType);
        }

        // Wrap the list of transactions and save them
        TransactionTypes tt = new TransactionTypes(ttypes);
        wrkld.setTransTypes(tt);
        logger.debug("Using the following transaction types: %s".formatted(tt));
        benchList.add(bench);

        // ----------------------------------------------------------------
        // WORKLOAD CONFIGURATION
        // ----------------------------------------------------------------

        int size = xmlConfig.configurationsAt("/works/work").size();
        for (int i = 1; i < size + 1; i++) {
            final HierarchicalConfiguration<ImmutableNode> work =
                    xmlConfig.configurationAt("works/work[" + i + "]");
            List<String> weight_strings;

            // use a workaround if there are multiple workloads or single
            // attributed workload
            if (weight_ != null && weight_.length > 0) {
                weight_strings = Arrays.stream(weight_).mapToObj(Integer::toString).collect(Collectors.toList());
            } else {
                weight_strings = Arrays.asList(work.getString("weights[not(@bench)]").split("\\s*,\\s*"));
            }
            double rate = 1;
            boolean rateLimited = true;
            boolean disabled = false;
            boolean timed;

            // can be "disabled", "unlimited" or a number
            String rate_string;
            rate_string = work.getString("rate[not(@bench)]", "unlimited");
            if (rate_string.equals(RATE_DISABLED)) {
                disabled = true;
            } else if (rate_string.equals(RATE_UNLIMITED)) {
                rateLimited = false;
            } else if (rate_string.isEmpty()) {
                logger.error(
                        String.format("Please specify the rate for phase %d and workload %s", i, targetBenchmark));
                System.exit(-1);
            } else {
                try {
                    rate = Double.parseDouble(rate_string);
                    if (rate <= 0) {
                        logger.error("Rate limit must be at least 0. Use unlimited or disabled values instead.");
                        System.exit(-1);
                    }
                } catch (NumberFormatException e) {
                    logger.error(
                            String.format(
                                    "Rate string must be '%s', '%s' or a number", RATE_DISABLED, RATE_UNLIMITED));
                    System.exit(-1);
                }
            }
            Phase.Arrival arrival = Phase.Arrival.REGULAR;
            String arrive = work.getString("@arrival", "regular");
            if (arrive.equalsIgnoreCase("POISSON")) {
                arrival = Phase.Arrival.POISSON;
            }

            // We now have the option to run all queries exactly once in
            // a serial (rather than random) order.
            boolean serial = Boolean.parseBoolean(work.getString("serial", Boolean.FALSE.toString()));

            int activeTerminals;
            activeTerminals = work.getInt("active_terminals[not(@bench)]", terminals);
            // If using serial, we should have only one terminal
            if (serial && activeTerminals != 1) {
                logger.warn("Serial ordering is enabled, so # of active terminals is clamped to 1.");
                activeTerminals = 1;
            }
            if (activeTerminals > terminals) {
                logger.error(
                        String.format(
                                "Configuration error in work %d: "
                                        + "Number of active terminals is bigger than the total number of terminals",
                                i));
                System.exit(-1);
            }

            int time = work.getInt("/time", 0);
            int warmup = work.getInt("/warmup", 0);
            timed = (time > 0);
            if (!timed) {
                if (serial) {
                    logger.info("Timer disabled for serial run; will execute" + " all queries exactly once.");
                } else {
                    logger.error(
                            "Must provide positive time bound for"
                                    + " non-serial executions. Either provide"
                                    + " a valid time or enable serial mode.");
                    System.exit(-1);
                }
            } else if (serial) {
                logger.info(
                        "Timer enabled for serial run; will run queries"
                                + " serially in a loop until the timer expires.");
            }
            if (warmup < 0) {
                logger.error("Must provide non-negative time bound for" + " warmup.");
                System.exit(-1);
            }

            ArrayList<Double> weights = new ArrayList<>();

            double totalWeight = 0;

            for (String weightString : weight_strings) {
                double weight = Double.parseDouble(weightString);
                totalWeight += weight;
                weights.add(weight);
            }

            long roundedWeight = Math.round(totalWeight);

            if (roundedWeight != 100) {
                logger.warn(
                        "rounded weight [%d] does not equal 100.  Original weight is [%f]".formatted(
                                roundedWeight,
                                totalWeight));
            }

            wrkld.addPhase(
                    i,
                    time,
                    warmup,
                    rate,
                    weights,
                    rateLimited,
                    disabled,
                    serial,
                    timed,
                    activeTerminals,
                    arrival);
        }

        // CHECKING INPUT PHASES
        int j = 0;
        for (Phase p : wrkld.getPhases()) {
            j++;
            if (p.getWeightCount() != numTxnTypes) {
                logger.error(
                        String.format(
                                "Configuration files is inconsistent, phase %d contains %d weights but you defined %d transaction types",
                                j, p.getWeightCount(), numTxnTypes));
                if (p.isSerial()) {
                    logger.error(
                            "However, note that since this a serial phase, the weights are irrelevant (but still must be included---sorry).");
                }
                System.exit(-1);
            }
        }

        return wrkld;
    }

    private static Options buildOptions(XMLConfiguration pluginConfig) {
        Options options = new Options();
        options.addOption(
                "b",
                "bench",
                true,
                "[required] Benchmark class. Currently supported: "
                        + pluginConfig.getList("/plugin//@name"));
        options.addOption("c", "config", true, "[required] Workload configuration file");
        options.addOption(null, "execute", true, "Execute the benchmark workload");
        options.addOption("h", "help", false, "Print this help");
        options.addOption("s", "sample", true, "Sampling window");
        options.addOption(
                "im", "interval-monitor", true, "Throughput Monitoring Interval in milliseconds");
        options.addOption(
                "d",
                "directory",
                true,
                "Base directory for the result files, default is current directory");
        options.addOption(null, "dialects-export", true, "Export benchmark SQL to a dialects file");
        options.addOption("jh", "json-histograms", true, "Export histograms to JSON file");
        options.addOption("p", "phase", true, "Online predict or offline training");
        return options;
    }

    private static Results runWorkload(List<BenchmarkModule> benchList, int intervalMonitor)
            throws IOException {
        List<Worker<?>> workers = new ArrayList<>();
        List<WorkloadConfiguration> workConfs = new ArrayList<>();
        for (BenchmarkModule bench : benchList) {
            logger.info("Creating %d virtual terminals...".formatted(bench.getWorkloadConfiguration().getTerminals()));
            workers.addAll(bench.makeWorkers());

            int num_phases = bench.getWorkloadConfiguration().getNumberOfPhases();
            logger.info(
                    String.format(
                            "Launching the %s Benchmark with %s Phase%s...",
                            bench.getBenchmarkName().toUpperCase(), num_phases, (num_phases > 1 ? "s" : "")));
            workConfs.add(bench.getWorkloadConfiguration());
        }
        Results r = ThreadBench.runRateLimitedBenchmark(workers, workConfs, intervalMonitor);
        logger.info(SINGLE_LINE);
        logger.info("Rate limited reqs/s: %s".formatted(r));
        return r;
    }

    private static void writeHistograms(Results r) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        sb.append("Completed Transactions:")
                .append("\n")
                .append(r.getSuccess())
                .append("\n\n");

        sb.append("Aborted Transactions:")
                .append("\n")
                .append(r.getAbort())
                .append("\n\n");

        sb.append("Rejected Transactions (Server Retry):")
                .append("\n")
                .append(r.getRetry())
                .append("\n\n");

        sb.append("Rejected Transactions (Retry Different):")
                .append("\n")
                .append(r.getRetryDifferent())
                .append("\n\n");

        sb.append("Unexpected SQL Errors:")
                .append("\n")
                .append(r.getError())
                .append("\n\n");

        sb.append("Unknown Status Transactions:")
                .append("\n")
                .append(r.getUnknown())
                .append("\n\n");

        if (!r.getAbortMessages().isEmpty()) {
            sb.append("\n\n")
                    .append("User Aborts:")
                    .append("\n")
                    .append(r.getAbortMessages());
        }

        System.out.println(SINGLE_LINE);
        System.out.printf("Workload Histograms:\n{%s}%n", sb);
        System.out.println(SINGLE_LINE);
    }

    private static String writeJSONHistograms(Results r) {
        Map<String, JSONSerializable> map = new HashMap<>();
        map.put("completed", r.getSuccess());
        map.put("aborted", r.getAbort());
        map.put("rejected", r.getRetry());
        map.put("unexpected", r.getError());
        return JSONUtil.toJSONString(map);
    }

    private static void createFlushThread(CommandLine argsLine, CCType ccType) {
        String metaDirectory = "metas";
        boolean onlinePredict = false;
        if (argsLine.hasOption("d")) {
            metaDirectory = argsLine.getOptionValue("d").trim();
            if (metaDirectory.contains("results")) {
                metaDirectory = metaDirectory.replace("results", "metas");
                int lastIndex = metaDirectory.lastIndexOf("_");

                if (lastIndex != -1) {
                    metaDirectory = metaDirectory.substring(0, lastIndex - 1);
                    metaDirectory += "/";
                }
            }
        }

        if (argsLine.hasOption("p")) {
            if (argsLine.getOptionValue("p").contains("online")) {
                onlinePredict = true;
            }
        }

        FileUtil.makeDirIfNotExists(metaDirectory);
        flushThread = new Thread(new Flusher(argsLine.getOptionValue("b").trim(), metaDirectory, ccType,onlinePredict));
        flushThread.start();
    }

    private static void finishFlushThread() {
        flushThread.interrupt();
    }

    private static void writeOutputs(
            Results r,
            List<TransactionType> activeTXTypes,
            CommandLine argsLine,
            XMLConfiguration xmlConfig)
            throws Exception {

        // If an output directory is used, store the information
        String outputDirectory = "results";
        String metaDirectory = "metas";

        if (argsLine.hasOption("d")) {
            outputDirectory = argsLine.getOptionValue("d").trim();
            if (outputDirectory.contains("results")) {
                metaDirectory = outputDirectory.replace("results", "metas");
                int lastIndex = metaDirectory.lastIndexOf("_");

                if (lastIndex != -1) {
                    metaDirectory = metaDirectory.substring(0, lastIndex - 1);
                    metaDirectory += "/";
                }
            }
        }

        FileUtil.makeDirIfNotExists(outputDirectory);
        ResultWriter rw = new ResultWriter(r, xmlConfig, argsLine);

        String name = StringUtils.join(StringUtils.split(argsLine.getOptionValue("b"), ','), '-');

        String baseFileName = name + "_" + TimeUtil.getCurrentTimeString();

        int windowSize = Integer.parseInt(argsLine.getOptionValue("s", "5"));

        String rawFileName = baseFileName + ".raw.csv";
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, rawFileName))) {
            logger.info("Output Raw data into file: %s".formatted(rawFileName));
            rw.writeRaw(activeTXTypes, ps);
        }

        String sampleFileName = baseFileName + ".samples.csv";
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, sampleFileName))) {
            logger.info("Output samples into file: %s".formatted(sampleFileName));
            rw.writeSamples(ps);
        }

        String summaryFileName = baseFileName + ".summary.json";
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, summaryFileName))) {
            logger.info("Output summary data into file: %s".formatted(summaryFileName));
            rw.writeSummary(ps);
        }
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(metaDirectory, summaryFileName))) {
            logger.info("Output {meta directory} summary data into file: %s".formatted(summaryFileName));
            rw.writeSummary(ps);
        }

        String paramsFileName = baseFileName + ".params.json";
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, paramsFileName))) {
            logger.info("Output DBMS parameters into file: %s".formatted(paramsFileName));
            rw.writeParams(ps);
        }

        if (rw.hasMetrics()) {
            String metricsFileName = baseFileName + ".metrics.json";
            try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, metricsFileName))) {
                logger.info("Output DBMS metrics into file: %s".formatted(metricsFileName));
                rw.writeMetrics(ps);
            }
        }

        String configFileName = baseFileName + ".config.xml";
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, configFileName))) {
            logger.info("Output benchmark config into file: %s".formatted(configFileName));
            rw.writeConfig(ps);
        }

        String resultsFileName = baseFileName + ".results.csv";
        try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, resultsFileName))) {
            logger.info("Output results into file: %s with window size %s".formatted(resultsFileName, windowSize));
            rw.writeResults(windowSize, ps);
        }

        for (TransactionType t : activeTXTypes) {
            String fileName = baseFileName + ".results." + t.getName() + ".csv";
            try (PrintStream ps = new PrintStream(FileUtil.joinPath(outputDirectory, fileName))) {
                rw.writeResults(windowSize, ps, t);
            }
        }
    }

    private static void printUsage(Options options) {
        HelpFormatter hlpfrmt = new HelpFormatter();
        hlpfrmt.printHelp("benchbase", options);
    }

    public static XMLConfiguration buildConfiguration(String filename) throws ConfigurationException {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<XMLConfiguration> builder =
                new FileBasedConfigurationBuilder<>(XMLConfiguration.class)
                        .configure(
                                params
                                        .xml()
                                        .setFileName(filename)
                                        .setListDelimiterHandler(new DisabledListDelimiterHandler())
                                        .setExpressionEngine(new XPathExpressionEngine()));
        return builder.getConfiguration();
    }

    private static boolean isBooleanOptionSet(CommandLine argsLine, String key) {
        if (argsLine.hasOption(key)) {
            logger.debug("CommandLine has option '%s'. Checking whether set to true".formatted(key));
            String val = argsLine.getOptionValue(key);
            logger.debug(String.format("CommandLine %s => %s", key, val));
            return (val != null && val.equalsIgnoreCase("true"));
        }
        return (false);
    }

    // interact with txnSails server
    public static class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
        public static BenchmarkModule benchmark = null;
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            if (benchmark != null) {
                NettyClientHandler.b = benchmark;
            }
            ChannelPipeline pipeline = ch.pipeline();
            // Decoder
            pipeline.addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4));
            // Encoder
            pipeline.addLast(new LengthFieldPrepender(4));
            pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
            pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
            pipeline.addLast(new NettyClientHandler());
        }
    }


    public static class NettyClientHandler extends SimpleChannelInboundHandler<String> {
        public static BenchmarkModule b = null;
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            System.out.println("response: " + msg);
//            ctx.writeAndFlush("from client " + System.currentTimeMillis());
            // TODO: record the sql index for execution
            buffer = msg;
            // unlock the `waitForResponse` lock
            unlockWaitForResponse();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
//            System.out.println("connect to the server");
        }
    }

    private static void registerTemplateSQLs(Channel ctx,
                                             Map<TransactionType, Procedure> procedures) throws InterruptedException {
        // register sql apis
        for (Map.Entry<TransactionType, Procedure> entry: procedures.entrySet()) {
            if (entry.getKey().equals(TransactionType.INVALID)) {
                continue;
            }
            sendMsgToTxnSailsServer(ctx, "register_begin#" + entry.getKey().getName() + "\n");
            if (entry.getValue().getTemplateSQLMetas() == null) {
                continue;
            }
            for (TemplateSQLMeta t: entry.getValue().getTemplateSQLMetas()) {
                StringBuilder sb = new StringBuilder();
                sb.append("register#");
                sb.append(t.getTemplateName()).append("#");
                sb.append(t.getOp()).append("#");
                sb.append(t.getRelationName()).append("#");
                if (t.getSkipIndex() < 0) {
                    sb.append(t.getOriginSQL());
                } else {
                    sb.append(t.getOriginSQL()).append("#");
                    sb.append(t.getIndexInClientSide());
                }
                sendMsgToTxnSailsServer(ctx, sb.toString() + "\n");
                // update the server side index
                updateTheServerSideIndex(entry.getValue(), t);
            }
            sendMsgToTxnSailsServer(ctx, "register_end#" + entry.getKey().getName() + "\n");
        }

        lockWaitForResponse();
        ctx.close();
        unlockWaitForResponse();
    }

    private static void updateTheServerSideIndex(Procedure proc, TemplateSQLMeta t) {
        lockWaitForResponse();
        String[] parts = buffer.split("#");
        if (parts.length < 2) {
            System.out.println("response not includes  " + buffer);
        }
        proc.updateClientServerIndexMap(t.getIndexInClientSide(), Integer.parseInt(parts[1]));
        unlockWaitForResponse();
    }

    private static List<TemplateSQLMeta> getTemplateSQLMeta(Procedure p) {
        return p.getTemplateSQLMetas();
    }

    private static void sendMsgToTxnSailsServer(Channel ctx, String msg) throws InterruptedException {
        lockWaitForResponse();
        ByteBuf resp = ctx.alloc().buffer(msg.length());
        resp.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(resp).sync();
    }

    private static void lockWaitForResponse() {
        while (!Thread.interrupted() && !waitForRespond.compareAndSet(false, true)) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }
    }

    private static void unlockWaitForResponse() {
        waitForRespond.set(false);
    }
}