package benchmark;

import org.dbiir.tristar.TriStar;
import org.dbiir.tristar.common.CCType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class YCSB {
    private String configPrefix = "config/ycsb/";
    private String resultPrefix = "results/ycsb/";

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
            System.out.println("used memory: " + usedMemory * 1.0 / 1024 / 1024 / 1024);
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
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create directory.");
                return;
            }
        }
        if (!file.exists() && file.createNewFile())
            System.out.println("Create new output file: " + filepath);;
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
                "-b ycsb",
                "--execute=true",
                "-d", genResultPathWithTimestamp(resultPath)};
        testCase(args, resultPath, "sample");
    }
}
