package org.dbiir.tristar.adapter;

import lombok.Setter;
import lombok.SneakyThrows;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Flusher implements Runnable {
    @Setter
    private String outputFile;

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            if (Adapter.getInstance().isNeedFlush()) {
                String transactionTypes = Adapter.getInstance().getTransactionTypes();
                String distributions = Adapter.getInstance().getRecordDistribution();
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true));
                writer.append(transactionTypes).append(distributions);
                writer.newLine();
                writer.close();
                Adapter.getInstance().refreshMetas();
                Adapter.getInstance().setNeedFlush(false);
            }
            Thread.sleep(10);
        }
    }
}
