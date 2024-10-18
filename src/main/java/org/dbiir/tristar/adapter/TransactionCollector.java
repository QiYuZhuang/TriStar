package org.dbiir.tristar.adapter;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dbiir.tristar.benchmarks.catalog.RWRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.ReadWriteRecord;

import lombok.Getter;
import lombok.Setter;

public class TransactionCollector {
    private static final TransactionCollector INSTANCE;
    private static long LOW_BIT_MASK = 0xFFFFFFFFL;
    private static int MAX_TABLE_NUM = 100;
    static public int TRANSACTION_BATCH = 8;
    private static final double SAMPLE_PROBABILITY = 0.1;
    private final Random random = new Random();
    static String edgeFormat = "#%d,%d,%d";
    private final Lock lock;
    @Getter
    @Setter
    private String workload; 
    @Getter
    @Setter
    private boolean needFlush = false;
    private TransactionMeta[] transactionMetas;
    private int sampleCount;

    /*
     * encode: 
     *      <(TxnID1 << 32) | (TxnID2 & 0xFFFFFFFFL), (id1 << 32) | (id2 & 0xFFFFFFFFL)>
     *      TXN1 -- rw --> TXN2
     * decode: 
     *      l -> (int) (l >>> 32), (int) l
     */ 
    private static final HashMap<String, HashMap<Long, Long>> WorkloadToRWDetection;

    static {
        WorkloadToRWDetection = new HashMap<>();
        WorkloadToRWDetection.put("ycsb", new HashMap<>());
        WorkloadToRWDetection.put("smallbank", new HashMap<>());
        WorkloadToRWDetection.put("tpcc", new HashMap<>());
    }

    static {
        INSTANCE = new TransactionCollector();
    }

    public TransactionCollector() {
        this.lock = new ReentrantLock();
        this.transactionMetas = new TransactionMeta[TRANSACTION_BATCH + 1];
        this.sampleCount = 0;
    }

    public void addTransactionSample(int transactionType, RWRecord[] reads, RWRecord[] writes, float processing) {
        lock.lock();
        if (!needFlush) {
            transactionMetas[sampleCount++] = new TransactionMeta(transactionType, reads, writes, processing, 0);
            if (sampleCount >= TRANSACTION_BATCH)
                needFlush = true;
        }
        lock.unlock();
    }

    public void addTransactionSample(int transactionType, RWRecord[] reads, RWRecord[] writes, float processing, long latency) {
        lock.lock();
        if (!needFlush) {
            transactionMetas[sampleCount++] = new TransactionMeta(transactionType, reads, writes, processing, latency);
            if (sampleCount >= TRANSACTION_BATCH)
                needFlush = true;
        }
        lock.unlock();
    }

    public void refreshMetas() {
        lock.lock();
        sampleCount = 0;
        needFlush = false;
        lock.unlock();
    }

    public String getTransactionNodeFeature(int idx) {
        return transactionMetas[idx].transactionFeature();
    }

    public String getTransactionEdgeFeature(int idx) {
        // todo: 
        StringBuilder builder = new StringBuilder();
        int table_idx;
        for (int i = 0; i < TRANSACTION_BATCH; i++) {
            if (i == idx) continue;
            if ((table_idx = rrDependency(idx, i)) > 0) {
                builder.append(edgeFormat.formatted(i, 1, table_idx));
            }
            if ((table_idx = rwDependency(idx, i)) > 0) {
                if (table_idx < MAX_TABLE_NUM)
                    builder.append(edgeFormat.formatted(i, 2, table_idx));
                else 
                    builder.append(edgeFormat.formatted(i, 4, table_idx - MAX_TABLE_NUM));
            }
            if ((table_idx = wwDependency(idx, i)) > 0) {
                builder.append(edgeFormat.formatted(i, 8, table_idx));
            }
        }
        return builder.toString();
    }

    private int rrDependency(int idx1, int idx2) {
        for (RWRecord r: transactionMetas[idx2].rset()) {
            for (RWRecord r2: transactionMetas[idx1].rset()) {
                if (r.table_idx() == r2.table_idx() && r.key_id() == r2.key_id())
                    return r.table_idx();
            }
        }
        return -1;
    }

    private int rwDependency(int idx1, int idx2) {
        for (RWRecord w: transactionMetas[idx2].wset()) {
            for (RWRecord r: transactionMetas[idx1].rset()) {
                if (w.table_idx() == r.table_idx() && w.key_id() == r.key_id()) {
                    long searchKey = ((long) idx1 << 32) | (idx2 & LOW_BIT_MASK);
                    if (WorkloadToRWDetection.get(workload).containsKey(searchKey)) {
                        long rel = WorkloadToRWDetection.get(workload).get(searchKey);
                        int rel1 = (int) (rel >>> 32);
                        int rel2 = (int) rel;
                        if ((rel1 == r.idx() || rel1 == LOW_BIT_MASK) && (rel2 == r.idx() || rel2 == LOW_BIT_MASK)) {
                            return w.table_idx() + MAX_TABLE_NUM;
                        }
                    }
                    return w.table_idx();
                }
            }
        }
        for (RWRecord w: transactionMetas[idx1].wset()) {
            for (RWRecord r: transactionMetas[idx2].rset()) {
                if (w.table_idx() == r.table_idx() && w.key_id() == r.key_id()) {
                    long searchKey = ((long) idx2 << 32) | (idx1 & LOW_BIT_MASK);
                    if (WorkloadToRWDetection.get(workload).containsKey(searchKey)) {
                        long rel = WorkloadToRWDetection.get(workload).get(searchKey);
                        int rel1 = (int) (rel >>> 32);
                        int rel2 = (int) rel;
                        if ((rel1 == r.idx() || rel1 == LOW_BIT_MASK) && (rel2 == r.idx() || rel2 == LOW_BIT_MASK)) {
                            return w.table_idx() + MAX_TABLE_NUM;
                        }
                    }
                    return w.table_idx();
                }
            }
        }
        return -1;
    }

    private int wwDependency(int idx1, int idx2) {
        for (RWRecord w1: transactionMetas[idx2].wset()) {
            for (RWRecord w2: transactionMetas[idx1].wset()) {
                if (w1.table_idx() == w2.table_idx() && w1.key_id() == w2.key_id())
                    return w1.table_idx();
            }
        }
        return -1;
    }

    public void initDetectedRWDependency() {
        if (workload.equals("ycsb")) {
            int updateStmtId = TAdapter.getInstance().getTypesByName("UpdateRecord".toUpperCase()).getId();
            int readStmtId = TAdapter.getInstance().getTypesByName("ReadRecord".toUpperCase()).getId();
            int readWriteStmtId = TAdapter.getInstance().getTypesByName("ReadWriteRecord".toUpperCase()).getId();

            WorkloadToRWDetection.get("ycsb").put(((long) readWriteStmtId << 32) | (readWriteStmtId & LOW_BIT_MASK), (LOW_BIT_MASK << 32) | LOW_BIT_MASK);
        } else if (workload.equals("smallbank")) {
            // SMALLBANK
        } else if (workload.equals("tpcc")) {
            // TPCC
        }
    }

    public boolean isSample() {
        return !needFlush && random.nextDouble() < SAMPLE_PROBABILITY;
    }

    public static TransactionCollector getInstance() {
        return INSTANCE;
    }

    public boolean isNeedFlush() {
        return needFlush;
    }
}
