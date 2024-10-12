package org.dbiir.tristar.adapter;

import lombok.Getter;
import lombok.Setter;
import org.dbiir.tristar.transaction.concurrency.XNORLock;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Adapter {
    private static final Adapter INSTANCE;
    private static final int SEQUENCE_NUM = 1000;
    private static final double SAMPLE_PROBABILITY = 0.1;
    private static final int MAX_TRANSACTION_TYPE = 10;
    private final Random random = new Random();
    @Setter
    private boolean offline;
    @Setter
    private boolean use;
    @Setter
    private String outputPath;
    @Getter
    @Setter
    private boolean needFlush = false;
    private int sampleCount = 0;
    private final HashMap<String, Integer> transactionTypes = new HashMap<>(4);
    private final int[] transactionCount = new int[MAX_TRANSACTION_TYPE];
    private final HashMap<String, ArrayList<Long>> sequenceToTable = new HashMap<>(4);
    private final HashMap<String, ReentrantLock[]> locks = new HashMap<>(4);
    private final HashMap<String, Integer> integerToTable = new HashMap<>(4);

    static {
        INSTANCE = new Adapter();
    }

    public static Adapter getInstance() {
        return INSTANCE;
    }

    public void initAdapter(String workload) {
        if (workload.equals("smallbank")) {
            sequenceToTable.put("checking", new ArrayList<>(SEQUENCE_NUM + 1));
            sequenceToTable.put("savings", new ArrayList<>(SEQUENCE_NUM + 1));
            locks.put("checking", new ReentrantLock[SEQUENCE_NUM + 1]);
            locks.put("savings", new ReentrantLock[SEQUENCE_NUM + 1]);
            integerToTable.put("checking", 0);
            integerToTable.put("savings", 0);
        } else if (workload.equals("ycsb")) {
            sequenceToTable.put("usertable", new ArrayList<>(SEQUENCE_NUM + 1));
            locks.put("usertable", new ReentrantLock[SEQUENCE_NUM + 1]);
            integerToTable.put("usertable", 0);
        } else if (workload.equals("tpcc")) {

        } else {
            throw new RuntimeException("not implemented now");
        }
        initTransactionTypes(workload);
    }

    public void addSampleKey(String table, long id) {
        if (!locks.get(table)[0].tryLock())
            return;
        int index = integerToTable.get(table);

        integerToTable.put(table, index + 1);
        // id \in [0, x]
        sequenceToTable.get(table).set(index, id + 1);
    }

    public void refreshMetas() {
        // reset transactionTypes
        Arrays.fill(transactionCount, 0);
        // reset the sample
        for (ArrayList<Long> value: sequenceToTable.values()) {
            for (int i = 0; i < value.size(); i++) {
                value.set(i, 0L);
            }
        }
        for (Map.Entry<String, Integer> entry: integerToTable.entrySet()) {
            entry.setValue(0);
        }
    }

    public String getTransactionTypes() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < sequenceToTable.size(); i++) {
            res.append(transactionCount[i]).append(",");
        }
        return res.toString();
    }

    public String getRecordDistribution() {
        StringBuilder res = new StringBuilder();
        for (ArrayList<Long> value: sequenceToTable.values()) {
            for (Long l: value) {
                res.append(l).append(",");
            }
        }
        return res.toString();
    }

    private void initTransactionTypes(String workload) {
        switch (workload) {
            case "smallbank" -> {
                transactionTypes.put("Amalgamate", 0);
                transactionTypes.put("Balance", 1);
                transactionTypes.put("DepositChecking", 2);
                transactionTypes.put("TransactSavings", 3);
                transactionTypes.put("WriteCheck", 4);
            }
            case "ycsb" -> {
                transactionTypes.put("ReadRecord", 1);
                transactionTypes.put("InsertRecord", 2);
                transactionTypes.put("ScanRecord", 3);
                transactionTypes.put("UpdateRecord", 4);
                transactionTypes.put("DeleteRecord", 5);
                transactionTypes.put("ReadModifyWriteRecord", 6);
                transactionTypes.put("ReadWriteRecord", 7);
            }
            case "tpcc" -> {
                transactionTypes.put("NewOrder", 0);
                transactionTypes.put("Payment", 1);
                transactionTypes.put("OrderStatus", 2);
                transactionTypes.put("Delivery", 3);
                transactionTypes.put("StockLevel", 4);
            }
        }
    }

    synchronized public boolean addSampleType(String transactionType) {
        sampleCount++;
        if (sampleCount >= SEQUENCE_NUM) {
            needFlush = true;
            return false;
        }
        int idx = transactionTypes.get(transactionType);
        transactionCount[idx]++;
        return true;
    }

    public boolean isSample() {
        return !needFlush && random.nextDouble() < SAMPLE_PROBABILITY;
    }
}
