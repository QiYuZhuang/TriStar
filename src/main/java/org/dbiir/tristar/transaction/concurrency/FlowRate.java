package org.dbiir.tristar.transaction.concurrency;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;

public class FlowRate {
    private static final FlowRate INSTANCE;
    private static final int SMALL_BANK_HASH_SIZE = 4000000;
    private static final int YCSB_HASH_SIZE = 1000000;
    private static final boolean use = false;
    private int HASH_SIZE;
    private static final int THRESHOLDS = 128;
    private static final double P = 0.3;
    private HashMap<String, RecordMeta[]> recordMetas = new HashMap<>(4);
    private HashMap<String, ReentrantLock[]> ccBucketLocks = new HashMap<>(4);
    private final long lockWaitTimeout = 10;
    private final int maxRetry = 5;

    static {
        INSTANCE = new FlowRate();
    }

    public static FlowRate getInstance() {
        return INSTANCE;
    }

    public void init(String workload) {
        if (workload.equals("smallbank")) {
            HASH_SIZE = SMALL_BANK_HASH_SIZE;
            recordMetas.put("checking", new RecordMeta[HASH_SIZE]);
            recordMetas.put("savings", new RecordMeta[HASH_SIZE]);
            for (int i = 0; i < HASH_SIZE; i++) {
                recordMetas.get("checking")[i] = new RecordMeta();
                recordMetas.get("savings")[i] = new RecordMeta();
            }
        } else if (workload.equals("ycsb")) {
            HASH_SIZE = YCSB_HASH_SIZE;
            recordMetas.put("usertable", new RecordMeta[HASH_SIZE]);
            for (int i = 0; i < HASH_SIZE; i++) {
                recordMetas.get("usertable")[i] = new RecordMeta();
            }
        } else if (workload.equals("tpcc")) {

        }
    }

    public boolean readOperationAdmission(String table, long key) {
        if (key >= recordMetas.get(table).length) {
            System.out.println("readOperationAdmission: key is out of range. #" + key);
            return false;
        }
        if (!use)
            return true;
        RecordMeta meta = recordMetas.get(table)[(int) key];
        // meta.incReadInProcessing();
        if (meta.writeInProcessing * (1 - meta.getWriteHistoryFailed()) > 1.75) {
            return false;
        }
        return true;
    }

    public boolean writeOperationAdmission(String table, long key) {
        if (key >= recordMetas.get(table).length) {
            System.out.println(Thread.currentThread().getName() + " writeOperationAdmission: key is out of range. #" + key);
            return false;
        }
        if (!use)
            return true;
        RecordMeta meta = recordMetas.get(table)[(int) key];
        // double fp = meta.writeFailureProbability();
        int writeInProcessing = meta.getWriteInProcessing();
        // System.out.println("key #" + key + " threshold: " + meta.getThreshold() + " in processing: " + writeInProcessing);
        if (writeInProcessing + 1 <= meta.getThreshold()) {
            meta.incWriteInProcessing();
            return true;
        } else {
            return false;
        }
    }

    public boolean writeOperationAdmission(String table, long key, boolean SI) {
        if (key >= recordMetas.get(table).length) {
            System.out.println(Thread.currentThread().getName() + " writeOperationAdmission: key is out of range. #" + key);
            return false;
        }
        if (!use)
            return true;
        RecordMeta meta = recordMetas.get(table)[(int) key];
        double fp = meta.writeFailureProbability();
        int writeInProcessing = meta.getWriteInProcessing();
        if (SI) {
            if ((int) writeInProcessing * (1 - fp) > 1) {
                return false;
            }
        }
        if (writeInProcessing + 1 <= meta.getThreshold()) {
            meta.incWriteInProcessing();
            return true;
        } else {
            return false;
        }
    }

    public void readOperationFinish(String table, long key, boolean success) {
        if (key >= recordMetas.get(table).length) {
            System.out.println("readOperationFinish: key is out of range. #" + key);
        }
        if (!use)
            return;
        RecordMeta meta = recordMetas.get(table)[(int) key];
        meta.decReadInProcessing();
        meta.incReadHistory(success);
    }

    public void writeOperationFinish(String table, long key, boolean success) {
        if (key >= recordMetas.get(table).length) {
            System.out.println("writeOperationFinish: key is out of range. #" + key);
        }
        if (!use)
            return;
        RecordMeta meta = recordMetas.get(table)[(int) key];
        meta.decWriteInProcessing();
        meta.incWriteHistory(success);
    }

    public void writeOperationFinish(String table, long key) {
        if (key >= recordMetas.get(table).length) {
            System.out.println("writeOperationFinish: key is out of range. #" + key);
        }
        if (!use)
            return;
        RecordMeta meta = recordMetas.get(table)[(int) key];
        meta.decWriteInProcessing();
    }

    @Getter
    private class RecordMeta {
        private int readInProcessing;
        private int writeInProcessing;
        private int readHistoryTotal;
        private int writeHistoryTotal;
        private int readHistoryFailed;
        private int writeHistoryFailed;
        private int threshold;
        private Lock latch;

        public RecordMeta() {
            readInProcessing = 0;
            writeInProcessing = 0;
            readHistoryTotal = 3;
            writeHistoryTotal = 3;
            readHistoryFailed = 0;
            writeHistoryFailed = 0;
            threshold = 16;
           latch = new ReentrantLock(true);
        }

        public void incWriteInProcessing() {
            latch.lock();
            this.writeInProcessing++;
            latch.unlock();
        }

        public void decWriteInProcessing() {
            latch.lock();
            this.writeInProcessing--;
            latch.unlock();
        }

        synchronized public void incWriteHistory(boolean success) {
            this.writeHistoryTotal++;
            if (!success)
                this.writeHistoryFailed++;

            if (writeHistoryTotal > 8 && (writeHistoryFailed * 1.0 / writeHistoryTotal) > P) {
                if (threshold >= 2) {
                    threshold >>= 1;
                    this.writeHistoryTotal = 3;
                    this.writeHistoryFailed = 0;
                }
            } else {
                threshold++;
            }

            if (this.writeHistoryTotal >= THRESHOLDS) {
                this.writeHistoryTotal >>= 1;
                this.writeHistoryFailed >>= 1;
            }
        }

        public void incReadInProcessing() {
            latch.lock();
            this.readInProcessing++;
            latch.unlock();
        }

        public void decReadInProcessing() {
            latch.lock();
            this.readInProcessing--;
            latch.unlock();
        }

        synchronized public void incReadHistory(boolean success) {
            this.readHistoryTotal++;
            if (!success)
                this.readHistoryFailed++;
            if (this.readHistoryTotal >= THRESHOLDS) {
                this.readHistoryTotal >>= 1;
                this.readHistoryFailed >>= 1;
            }
        }

        public double readFailureProbability() {
            return (this.readHistoryFailed / (this.readHistoryTotal * 1.0));
        }

        public double writeFailureProbability() {
            return (this.writeHistoryFailed / (this.writeHistoryTotal * 1.0));
        }
    }
}
