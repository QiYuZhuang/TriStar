package benchmark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.dbiir.tristar.TriStar;
import org.dbiir.tristar.common.CCType;
import org.junit.jupiter.api.Test;

public class SmallBank {
    private String configPrefix = "config/smallbank/";
    private String resultPrefix = "results/smallbank/";

    private void testCase(String[] args, String resultPath, String caseName) throws IOException {
        String stdoutPath = resultPath + "/stdout.log";
        createFileIfNotExist(stdoutPath);
        File outputFile = new File(stdoutPath);
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        PrintStream printStream = new PrintStream(fileOutputStream);
        System.setOut(printStream);
        try {
            TriStar.main(args);
            System.setOut(System.out);
            System.out.printf("Execute {%30s} with no exception!%n", caseName);
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory(); // JVM的总内存
            long freeMemory = runtime.freeMemory(); // JVM的空闲内存
            long usedMemory = totalMemory - freeMemory; // 使用的内存
            //System.out.println("used memory: " + usedMemory * 1.0 / 1024 / 1024 / 1024);
        } catch (Exception e) {
            System.setOut(System.out);
            e.printStackTrace();
            System.out.printf("Execute {%30s} with exceptions!%n", caseName);
        }
    }

    private void createFileIfNotExist(String filepath) throws IOException {
        File file = new File(filepath);
        if (!file.getParentFile().exists()) {
            boolean created = file.getParentFile().mkdirs();
            if (created) {
                //System.out.println("Directory created successfully.");
            } else {
                //System.out.println("Failed to create directory.");
                return;
            }
        }
        if (!file.exists() && file.createNewFile())
            //System.out.println("Create new output file: " + filepath);;
    }

    private String genResultPathWithTimestamp(String oPath) {
        String currTime = LocalDateTime
                .now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd-HH-mm-ss"))
                .trim() + "/";
        return oPath + currTime;
    }

    private String genResultPathWithTimestamp(String oPath, String tag) {
        String currTime = LocalDateTime
                .now()
                .format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd-HH-mm-ss"))
                .trim() + "-" + tag + "/";
        return oPath + currTime;
    }

    private String genStringWithTimestamp() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"))
                .trim();
    }

    private String getStrategyName(CCType type) {
        return switch (type) {
            case SER -> "SER";
            case SI_ELT -> "SI+E";
            case RC_ELT -> "RC+E";
            case SI_FOR_UPDATE -> "SI+P";
            case RC_FOR_UPDATE -> "RC+P";
            case SI_TAILOR -> "SI+TV";
            case RC_TAILOR -> "RC+TV";
            case RC_TAILOR_LOCK -> "RC+TL";
            case DYNAMIC -> "DYNAMIC";
            case RC -> "RC";
            case SI -> "SI";
            case NUM_CC -> null;
        };
    }

    @Test
    void TestSample() throws IOException {
        String resultPath = resultPrefix + "sample/";
        String configPath = configPrefix + "sample.xml";
        String[] args = {"-c", configPath,
                "-b smallbank",
                "--execute=true",
                "-d", genResultPathWithTimestamp(resultPath)};
        testCase(args, resultPath, "sample");
    }

    @Test
    void TestScalability() throws IOException {
        // figure1: strategy + terminals
        String resultPath = resultPrefix + "scalability/" + genStringWithTimestamp() + "/";
        String configPath = configPrefix + "sample.xml";

        int[] terminalList = new int[]{4, 8, 16, 32, 64, 128, 256};
        CCType[] types = new CCType[]{CCType.SER,
                CCType.SI_ELT, CCType.SI_FOR_UPDATE,
                CCType.RC_ELT, CCType.RC_FOR_UPDATE,
                CCType.SI_TAILOR};
        for (CCType type: types) {
            String typeName = getStrategyName(type);
            TriStar.setType_(type.getName());
            for (int t: terminalList) {
                TriStar.setTerminals_(t);
                String resultDir = resultPath + String.format("terminals_%03d_", t) + typeName;
                String[] args = {"-c", configPath,
                        "-b smallbank",
                        "--execute=true",
                        "-d", resultDir};
                testCase(args, resultDir, "terminals_" + t + "_" + typeName);
            }
        }
    }

    @Test
    void TestHotspot() throws IOException {
        // figure2: strategy + hotspot + percentage
        String resultPath = resultPrefix + "hotspot-128/" + genStringWithTimestamp() + "/";
        String configPath = configPrefix + "sample.xml";

        // default variable
        int terminal = 128;
        TriStar.setTerminals_(terminal);
        TriStar.setZipFain_(-1);
        // flexible variable
        int[] hotspots = new int[]{1000};
        double[] percentages = new double[]{0.1, 0.3, 0.5, 0.7, 0.9};
        CCType[] types = new CCType[]{CCType.SER,
                CCType.SI_ELT, CCType.SI_FOR_UPDATE,
                CCType.RC_ELT, CCType.RC_FOR_UPDATE,
                CCType.SI_TAILOR};
        for (int h: hotspots) {
            TriStar.setHotspotNum_(h);
            for (double p: percentages) {
                TriStar.setHotspotProbability_(p);
                for (CCType type: types) {
                    TriStar.setType_(type.getName());
                    String caseName = String.format("hotspot_%05d_pro_%05.2f_", h, p) + getStrategyName(type);;
                    String resultDir = resultPath + caseName;
                    String[] args = {"-c", configPath,
                            "-b smallbank",
                            "--execute=true",
                            "-d", resultDir};
                    testCase(args, resultDir, caseName);
                }
            }
        }
    }

    @Test
    void TestZipFain() throws IOException {
        // figure2: strategy + skew
        String resultPath = resultPrefix + "skew-128/" + genStringWithTimestamp() + "/";
        String configPath = configPrefix + "sample.xml";

        // default variable
        int terminal = 128;
        TriStar.setTerminals_(terminal);
        TriStar.setHotspotNum_(-1);
        // flexible variable
        double[] skews = new double[]{0.1, 0.3, 0.5, 0.7, 0.9, 1.1, 1.3};
        CCType[] types = new CCType[]{CCType.SER,
                CCType.SI_ELT, CCType.SI_FOR_UPDATE,
                CCType.RC_ELT, CCType.RC_FOR_UPDATE,
                CCType.SI_TAILOR};
        for (double s: skews) {
            TriStar.setZipFain_(s);
            for (CCType type: types) {
                TriStar.setType_(type.getName());
                String caseName = String.format("skew_%03.2f_", s) + getStrategyName(type);;
                String resultDir = resultPath + caseName;
                String[] args = {"-c", configPath,
                        "-b smallbank",
                        "--execute=true",
                        "-d", resultDir};
                testCase(args, resultDir, caseName);
            }
        }
    }

    @Test
    void TestNothing() {
        int[] a = new int[]{10, 20, 30};
        System.out.println(Arrays.stream(a)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(",")));
    }

}
