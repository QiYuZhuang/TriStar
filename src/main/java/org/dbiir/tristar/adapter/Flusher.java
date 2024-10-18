package org.dbiir.tristar.adapter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.dbiir.tristar.common.CCType;

import lombok.Setter;
import lombok.SneakyThrows;

public class Flusher implements Runnable {
    private static final String ip = "localhost";
    private static final int port = 7654;
    private static final CCType[] types = new CCType[]{CCType.SER, CCType.SI_TAILOR, CCType.RC_TAILOR};

    private final String workload;
    @Setter
    private String outputFilePrefix;
    private CCType ccType;
    private final boolean online;
    private final Socket socket;
    private int flushCount;

    public Flusher(String workload, String prefix, CCType ccType, boolean online) {
        try {
            this.workload = workload;
            this.outputFilePrefix = prefix;
            this.ccType = ccType;
            this.online = online;
            this.flushCount = 0;
            if (online && (ccType == CCType.DYNAMIC || ccType == CCType.DYNAMIC_B || ccType == CCType.DYNAMIC_A))
                this.socket = new Socket(ip, port);
            else
                this.socket = new Socket();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    private boolean needFlush(CCType type) {
        if (type == CCType.DYNAMIC || type == CCType.DYNAMIC_B || type == CCType.DYNAMIC_A) {
            return true;
        }
        for (CCType t: types) {
            if (type == t && !online)
                return true;
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long timestamp = System.currentTimeMillis();
            if (TransactionCollector.getInstance().isNeedFlush() && needFlush(ccType)) {
                System.out.println("flash 2");
                if (flushCount == 0) {
                    flushCount++;
                    TransactionCollector.getInstance().refreshMetas();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
                String fileName = outputFilePrefix + "sample_" + timestamp;
                System.out.println(fileName);
                try (FileWriter fileWriter = new FileWriter(fileName, true)) {
                    for (int i = 0; i < TransactionCollector.TRANSACTION_BATCH; i++) {
                        fileWriter.write(i + ",");
                        fileWriter.write(TransactionCollector.getInstance().getTransactionNodeFeature(i));
                        fileWriter.write(TransactionCollector.getInstance().getTransactionEdgeFeature(i));
                        fileWriter.write("\n");
                    }
                    fileWriter.close();
                    System.out.println("Content appended to the file.");
                } catch (IOException ex) {
                    System.out.println("An error occurred: " + ex.getMessage());
                }

                if (online) {
                    try {
                        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        long startPrediction = System.currentTimeMillis();
                        out.println("online,predict," + fileName);
                        System.out.println("Send the file name to the server: " + fileName);
                        String data = in.readLine();
                        System.out.println("Receive the prediction result: " + data + "; time consume: " + (System.currentTimeMillis() - startPrediction) + " ms");
                        TAdapter.getInstance().setNextCCType(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Flush time cost: " + (System.currentTimeMillis() - timestamp) + " ms");

                try {
                    if (online) {
                        Thread.sleep(Math.max((500 - (System.currentTimeMillis() - timestamp)), 0));
                    } else {
                        Thread.sleep(10000);
                    }
                } catch (InterruptedException ignored) {
                    return;
                }
                TransactionCollector.getInstance().refreshMetas();
                // try {
                //     if (online) {
                //         Thread.sleep(1000);
                //     }
                // } catch (InterruptedException ignored) {
                //     return;
                // }
            }
        }
    }
}
