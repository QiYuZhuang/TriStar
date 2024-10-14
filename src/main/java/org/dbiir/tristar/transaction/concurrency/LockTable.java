package org.dbiir.tristar.transaction.concurrency;

import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
import org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;

public class LockTable {
    private static final LockTable INSTANCE;
    private static final int SMALL_BANK_HASH_SIZE = 4000000;
    private static final int YCSB_HASH_SIZE = 100000;
    private static final int TPCC_WAREHOUSE_HASH_SIZE = 32;
    private static final int TPCC_STOCK_HASH_SIZE = 3200000;
    private static final int TPCC_CUSTOMER_HASH_SIZE = 960000;
    private static final int TPCC_ORDERS_HASH_SIZE = 960000;
    private static final int TPCC_ORDERLINE_HASH_SIZE = 960000;
    private int LOAD_THREAD = 16;
    private int HASH_SIZE;
    private HashMap<String, LinkedList<XNORLock>[]> ccLocks = new HashMap<>(4);
    private HashMap<String, ReentrantReadWriteLock[]> ccBucketLocks = new HashMap<>(4);
    private HashMap<String, LinkedList<ValidationLock>[]> validationLocks = new HashMap<>(4);
    private HashMap<String, ReentrantReadWriteLock[]> validationBucketLocks = new HashMap<>(4);
    private final long lockWaitTimeout = 10;
    private final int maxRetry = 5;
    public final SQLStmt GetSavingsBalance =
            new SQLStmt("SELECT tid FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");
    public final SQLStmt GetCheckingBalance =
            new SQLStmt("SELECT tid FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

    public final SQLStmt GetUserTable =
            new SQLStmt("SELECT vid FROM " + YCSBConstants.TABLE_NAME + " WHERE  ycsb_key = ?");

    public final SQLStmt GetWarehouseTable =
            new SQLStmt("SELECT vid FROM " + TPCCConstants.TABLENAME_WAREHOUSE + " WHERE w_id = ?");

    public final SQLStmt GetCustomerTable =
            new SQLStmt("SELECT vid FROM " + TPCCConstants.TABLENAME_CUSTOMER + " WHERE c_w_id = ? AND c_d_id = ? AND c_id = ?");

    public final SQLStmt GetStockTable =
            new SQLStmt("SELECT vid FROM " + TPCCConstants.TABLENAME_STOCK + " WHERE s_w_id = ? AND s_i_id = ?");

    public final SQLStmt GetOrdersTable =
            new SQLStmt("SELECT vid FROM " + TPCCConstants.TABLENAME_OPENORDER + " WHERE o_w_id = ? AND o_d_id = ? AND o_id = ?");

    public final SQLStmt GetOrderLineTbale =
            new SQLStmt("SELECT vid FROM " + TPCCConstants.TABLENAME_ORDERLINE + " WHERE ol_w_id = ? AND ol_d_id = ? AND ol_o_id = ? LIMIT 1");

    static {
        INSTANCE = new LockTable();
    }

    public LockTable() {

    }

    public void initHotspot(String workload, List<Connection> connections) throws SQLException {
        if (workload.equals("smallbank")) {
            this.HASH_SIZE = SMALL_BANK_HASH_SIZE;
            this.LOAD_THREAD = 1;
            ccLocks.put("savings", new LinkedList[HASH_SIZE]);
            ccLocks.put("checking", new LinkedList[HASH_SIZE]);
            ccBucketLocks.put("savings", new ReentrantReadWriteLock[HASH_SIZE]);
            ccBucketLocks.put("checking", new ReentrantReadWriteLock[HASH_SIZE]);

            // init validation locks
            validationLocks.put("savings", new LinkedList[HASH_SIZE]);
            validationLocks.put("checking", new LinkedList[HASH_SIZE]);
            validationBucketLocks.put("savings", new ReentrantReadWriteLock[HASH_SIZE]);
            validationBucketLocks.put("checking", new ReentrantReadWriteLock[HASH_SIZE]);
            for (int i = 0; i < HASH_SIZE; i++) {
                ccLocks.get("savings")[i] = new LinkedList<>();
                ccLocks.get("checking")[i] = new LinkedList<>();
                ccLocks.get("savings")[i].add(new XNORLock(i));
                ccLocks.get("checking")[i].add(new XNORLock(i));
                ccBucketLocks.get("savings")[i] = new ReentrantReadWriteLock();
                ccBucketLocks.get("checking")[i] = new ReentrantReadWriteLock();

                validationLocks.get("savings")[i] = new LinkedList<>();
                validationLocks.get("checking")[i] = new LinkedList<>();
                validationLocks.get("savings")[i].add(new ValidationLock(i));
                validationLocks.get("checking")[i].add(new ValidationLock(i));
                validationBucketLocks.get("savings")[i] = new ReentrantReadWriteLock();
                validationBucketLocks.get("checking")[i] = new ReentrantReadWriteLock();
            }

            ExecutorService executor = Executors.newFixedThreadPool(LOAD_THREAD);

            for (int i = 0; i < LOAD_THREAD; i++) {
                int index = i;

                executor.submit(() -> {
                    PreparedStatement getSavings = null;
                    PreparedStatement getChecking = null;
                    try {
                        getSavings = connections.get(index).prepareStatement(GetSavingsBalance.getSQL());
                        getChecking = connections.get(index).prepareStatement(GetCheckingBalance.getSQL());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    for (int j = index; j < HASH_SIZE; j += LOAD_THREAD) {
                        try {
                            if (!validationLocks.get("savings")[j].isEmpty()) {
                                getSavings.setLong(1, index);
                                try (ResultSet res = getSavings.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid custid #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("savings", j, res.getLong(1));
                                }
                            }

                            if (!validationLocks.get("checking")[j].isEmpty()) {
                                getChecking.setLong(1, j);
                                try (ResultSet res = getChecking.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid custid #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("checking", j, res.getLong(1));
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }

            executor.shutdown();
        } else if (workload.equals("ycsb")) {
            this.HASH_SIZE = YCSB_HASH_SIZE;
            ccLocks.put("usertable", new LinkedList[HASH_SIZE]);
            ccBucketLocks.put("usertable", new ReentrantReadWriteLock[HASH_SIZE]);
            // init validation locks
            validationLocks.put("usertable", new LinkedList[HASH_SIZE]);
            validationBucketLocks.put("usertable", new ReentrantReadWriteLock[HASH_SIZE]);
            for (int i = 0; i < HASH_SIZE; i++) {
                ccLocks.get("usertable")[i] = new LinkedList<>();
                ccLocks.get("usertable")[i].add(new XNORLock(i));
                ccBucketLocks.get("usertable")[i] = new ReentrantReadWriteLock();
                validationLocks.get("usertable")[i] = new LinkedList<>();
                validationLocks.get("usertable")[i].add(new ValidationLock(i));
                validationBucketLocks.get("usertable")[i] = new ReentrantReadWriteLock();
            }
            ExecutorService executor = Executors.newFixedThreadPool(LOAD_THREAD);

            for (int i = 0; i < LOAD_THREAD; i++) {
                int index = i;

                executor.submit(() -> {
                    PreparedStatement getUserTable = null;
                    try {
                        getUserTable = connections.get(index).prepareStatement(GetUserTable.getSQL());
                        for (int j = index; j < HASH_SIZE; j += LOAD_THREAD) {
                            if (!validationLocks.get("usertable")[j].isEmpty()) {
                                getUserTable.setInt(1, j);
                                try (ResultSet rs = getUserTable.executeQuery()) {
                                    if (!rs.next()) {
                                        String msg = "Invalid ycsb key: #" + j;
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("usertable", j, rs.getLong(1));
                                }
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            executor.shutdown();
        } else if (workload.equals("tpcc")) {
            //System.out.println("tpcc_load");
            this.HASH_SIZE = TPCC_STOCK_HASH_SIZE;
            this.LOAD_THREAD = connections.size();

            ccLocks.put("warehouse", new LinkedList[TPCC_WAREHOUSE_HASH_SIZE]);
            ccLocks.put("customer", new LinkedList[TPCC_CUSTOMER_HASH_SIZE]);
            ccLocks.put("stock", new LinkedList[TPCC_STOCK_HASH_SIZE]);
            ccLocks.put("orders", new LinkedList[TPCC_ORDERS_HASH_SIZE]);
            ccLocks.put("order_line", new LinkedList[TPCC_ORDERLINE_HASH_SIZE]);

            ccBucketLocks.put("warehouse", new ReentrantReadWriteLock[TPCC_WAREHOUSE_HASH_SIZE]);
            ccBucketLocks.put("customer", new ReentrantReadWriteLock[TPCC_CUSTOMER_HASH_SIZE]);
            ccBucketLocks.put("stock", new ReentrantReadWriteLock[TPCC_STOCK_HASH_SIZE]);
            ccBucketLocks.put("orders", new ReentrantReadWriteLock[TPCC_ORDERS_HASH_SIZE]);
            ccBucketLocks.put("order_line", new ReentrantReadWriteLock[TPCC_ORDERLINE_HASH_SIZE]);

            // init validation locks
            validationLocks.put("warehouse", new LinkedList[TPCC_WAREHOUSE_HASH_SIZE]);
            validationLocks.put("customer", new LinkedList[TPCC_CUSTOMER_HASH_SIZE]);
            validationLocks.put("stock", new LinkedList[TPCC_STOCK_HASH_SIZE]);
            validationLocks.put("orders", new LinkedList[TPCC_ORDERS_HASH_SIZE]);
            validationLocks.put("order_line", new LinkedList[TPCC_ORDERLINE_HASH_SIZE]);

            validationBucketLocks.put("warehouse", new ReentrantReadWriteLock[TPCC_WAREHOUSE_HASH_SIZE]);
            validationBucketLocks.put("customer", new ReentrantReadWriteLock[TPCC_CUSTOMER_HASH_SIZE]);
            validationBucketLocks.put("stock", new ReentrantReadWriteLock[TPCC_STOCK_HASH_SIZE]);
            validationBucketLocks.put("orders", new ReentrantReadWriteLock[TPCC_ORDERS_HASH_SIZE]);
            validationBucketLocks.put("order_line", new ReentrantReadWriteLock[TPCC_ORDERLINE_HASH_SIZE]);

            for (int i = 0; i < TPCC_WAREHOUSE_HASH_SIZE; i++) {
                ccLocks.get("warehouse")[i] = new LinkedList<>();

                ccLocks.get("warehouse")[i].add(new XNORLock(i));

                ccBucketLocks.get("warehouse")[i] = new ReentrantReadWriteLock();

                validationLocks.get("warehouse")[i] = new LinkedList<>();

                validationLocks.get("warehouse")[i].add(new ValidationLock(i));;

                validationBucketLocks.get("warehouse")[i] = new ReentrantReadWriteLock();
            }

            for (int i = 0; i < TPCC_CUSTOMER_HASH_SIZE; i++) {
                ccLocks.get("customer")[i] = new LinkedList<>();

                ccLocks.get("customer")[i].add(new XNORLock(i));

                ccBucketLocks.get("customer")[i] = new ReentrantReadWriteLock();

                validationLocks.get("customer")[i] = new LinkedList<>();

                validationLocks.get("customer")[i].add(new ValidationLock(i));;

                validationBucketLocks.get("customer")[i] = new ReentrantReadWriteLock();
            }

            for (int i = 0; i < TPCC_STOCK_HASH_SIZE; i++) {
                ccLocks.get("stock")[i] = new LinkedList<>();

                ccLocks.get("stock")[i].add(new XNORLock(i));

                ccBucketLocks.get("stock")[i] = new ReentrantReadWriteLock();

                validationLocks.get("stock")[i] = new LinkedList<>();

                validationLocks.get("stock")[i].add(new ValidationLock(i));;

                validationBucketLocks.get("stock")[i] = new ReentrantReadWriteLock();
            }

            for (int i = 0; i < TPCC_ORDERS_HASH_SIZE; i++) {
                ccLocks.get("orders")[i] = new LinkedList<>();

                ccLocks.get("orders")[i].add(new XNORLock(i));

                ccBucketLocks.get("orders")[i] = new ReentrantReadWriteLock();

                validationLocks.get("orders")[i] = new LinkedList<>();

                validationLocks.get("orders")[i].add(new ValidationLock(i));;

                validationBucketLocks.get("orders")[i] = new ReentrantReadWriteLock();
            }

            for (int i = 0; i < TPCC_ORDERLINE_HASH_SIZE; i++) {
                ccLocks.get("order_line")[i] = new LinkedList<>();

                ccLocks.get("order_line")[i].add(new XNORLock(i));

                ccBucketLocks.get("order_line")[i] = new ReentrantReadWriteLock();

                validationLocks.get("order_line")[i] = new LinkedList<>();

                validationLocks.get("order_line")[i].add(new ValidationLock(i));;

                validationBucketLocks.get("order_line")[i] = new ReentrantReadWriteLock();
            }

            ExecutorService executor = Executors.newFixedThreadPool(LOAD_THREAD);

            for (int i = 0; i < LOAD_THREAD; i++) {
                int index = i;

                executor.submit(() -> {
                    PreparedStatement getwarehouse = null;
                    PreparedStatement getcustomer = null;
                    PreparedStatement getstock = null;
                    PreparedStatement getorders = null;
                    PreparedStatement getorderline = null;

                    try {
                        getwarehouse = connections.get(index).prepareStatement(GetWarehouseTable.getSQL());
                        getcustomer = connections.get(index).prepareStatement(GetCustomerTable.getSQL());
                        getstock = connections.get(index).prepareStatement(GetStockTable.getSQL());
                        getorders = connections.get(index).prepareStatement(GetOrdersTable.getSQL());
                        getorderline = connections.get(index).prepareStatement(GetOrderLineTbale.getSQL());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    for (int j = index; j < TPCC_WAREHOUSE_HASH_SIZE; j += LOAD_THREAD) {
                        try {
                            if (!validationLocks.get("warehouse")[j].isEmpty()) {
                                getwarehouse.setLong(1, j + 1);
                                try (ResultSet res = getwarehouse.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid warehouseid #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("warehouse", j, res.getLong(1));
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    for (int j = index; j < TPCC_CUSTOMER_HASH_SIZE; j += LOAD_THREAD) {
                        try {
                            if (!validationLocks.get("customer")[j].isEmpty()) {
                                int[] ids  = getCustomerIdsFromIndex(j);
                                getcustomer.setInt(1, ids[0]);
                                getcustomer.setInt(2, ids[1]);
                                getcustomer.setInt(3, ids[2]);
                                try (ResultSet res = getcustomer.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid customerid #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("customer", j, res.getInt(1));
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    for (int j = index; j < TPCC_STOCK_HASH_SIZE; j += LOAD_THREAD) {
                        try {
                            if (!validationLocks.get("stock")[j].isEmpty()) {
                                int[] ids = getStockIdsFromIndex(j);
                                getstock.setInt(1, ids[0]);
                                getstock.setInt(2, ids[1]);
                                try (ResultSet res = getstock.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid stock #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("stock", j, res.getInt(1));
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    for (int j = index; j < TPCC_ORDERS_HASH_SIZE; j += LOAD_THREAD) {
                        try {
                            if (!validationLocks.get("orders")[j].isEmpty()) {
                                int[] ids = getCustomerIdsFromIndex(j);
                                getorders.setInt(1, ids[0]);
                                getorders.setInt(2, ids[1]);
                                getorders.setInt(3, ids[2]);
                                try (ResultSet res = getorders.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid orders #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("orders", j, res.getInt(1));
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    for (int j = index; j < TPCC_ORDERLINE_HASH_SIZE; j += LOAD_THREAD) {
                        try {
                            if (!validationLocks.get("order_line")[j].isEmpty()) {
                                int[] ids = getCustomerIdsFromIndex(j);
                                getorderline.setInt(1, ids[0]);
                                getorderline.setInt(2, ids[1]);
                                getorderline.setInt(3, ids[2]);
                                try (ResultSet res = getorderline.executeQuery()) {
                                    if (!res.next()) {
                                        String msg = "Invalid orderline #%d".formatted(j);
                                        throw new RuntimeException(msg);
                                    }
                                    updateHotspotVersion("order_line", j, res.getInt(1));
                                }
                            }
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                });
            }

            executor.shutdown();
            try{
                executor.awaitTermination(1, TimeUnit.HOURS);
            } catch (InterruptedException ex) {
                System.out.println("[unexpected error]: " + ex);
                throw new RuntimeException(ex);
            }

        }

        for (int i = 0; i < LOAD_THREAD; i++) {
            connections.get(i).close();
        }
    }

    public void tryValidationLock(String table, long tid, long key, LockType type, CCType ccType) throws SQLException {
        if (!validationLocks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }

        if (checkAndTryValidationLock(table, tid, key, type, ccType))
            return;
        try {
            tryAndAddValidationLock(table, tid, key, type, ccType);
        } catch (SQLException ex){
            throw ex;
        }
    }

    public void releaseValidationLock(String table, long key, LockType type) {
        if (!validationLocks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }
        int bucketNum = (int)(key % HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].readLock().lock();
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                lock.releaseLock(type);
                break;
            }
        }
        validationBucketLocks.get(table)[bucketNum].readLock().unlock();

        // shrink the validation lock list
        if (validationBucketLocks.get(table)[bucketNum].writeLock().tryLock()) {
            lockList.removeIf(lock -> lock.getId() > HASH_SIZE && lock.free());
            validationBucketLocks.get(table)[bucketNum].writeLock().unlock();
        }
    }

    public void trySwitchValidation(String table, long tid, long key, long version, boolean old, boolean read) throws SQLException {
        int bucketNum = (int)(key % HASH_SIZE);
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                if (old) {
                    if (!read) {
                        if (version >= lock.versionNewRead) {
                            String msg = "Old transaction #" + tid + " can not keep the sequence of rw dependency in switch phase, case1 key: " + key;
                            throw new SQLException(msg, "502");
                        }
                    }
                    if (version >= lock.versionNewWrite) {
                        String msg = "Old transaction #" + tid + " can not keep the sequence of rw dependency in switch phase, case2 key: " + key;
                        throw new SQLException(msg, "502");
                    }
                } else {
                    if (!read) {
                        if (version < lock.versionOldRead) {
                            String msg = "New transaction #" + tid + " can not keep the sequence of rw dependency in switch phase, case3 key: " + key;
                            throw new SQLException(msg, "502");
                        }
                    }
                    if (version < lock.versionOldWrite) {
                        String msg = "New transaction #" + tid + " can not keep the sequence of rw dependency in switch phase, case4 key: " + key;
                        throw new SQLException(msg, "502");
                    }
                }
            }
        }
    }

    public void updateSwitchValidationRead(String table, long tid, long key, long version, boolean old) {
        int bucketNum = (int)(key % HASH_SIZE);
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                if (old) {
                    lock.setVersionOldRead(Math.max(version, lock.getVersionOldRead()));
                } else {
                    lock.setVersionNewRead(Math.min(version, lock.getVersionNewRead()));
                }
            }
        }
    }

    public void updateSwitchValidationWrite(String table, long tid, long key, long version, boolean old) {
        int bucketNum = (int)(key % HASH_SIZE);
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                if (old) {
                    lock.setVersionOldWrite(Math.max(version, lock.getVersionOldWrite()));
                } else {
                    lock.setVersionNewWrite(Math.min(version, lock.getVersionNewWrite()));
                }
            }
        }
    }

    /*
     * invoke this method after all transactions finish their switch phase
     */
    public void refreshSwitchValidation(String workload) {
        System.out.println("refresh switch validation: " + workload);
        String table = "";
        if (workload.equals("ycsb")) {
            table = "usertable";
        } else {
            System.out.println("not implemented  workload: " + workload);
        }
        if (!validationLocks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }
        for (int i = 0; i < HASH_SIZE; i++) {
            // validationBucketLocks.get(table)[i].writeLock().lock();
            List<ValidationLock> lockList = validationLocks.get(table)[i];
            for (ValidationLock lock: lockList) {
                lock.setVersionOldRead(0);
                lock.setVersionOldWrite(0);
                lock.setVersionNewRead(Long.MAX_VALUE);
                lock.setVersionNewWrite(Long.MAX_VALUE);
            }
            // validationBucketLocks.get(table)[i].writeLock().unlock();
        }
    }

    /*
     * @return: true if you get validation lock, otherwise can not find the validation lock
     */
    private boolean checkAndTryValidationLock(String table, long tid, long key, LockType type, CCType ccType) throws SQLException {
        int bucketNum = (int)(key % HASH_SIZE);
        long currentTime = System.currentTimeMillis();
        validationBucketLocks.get(table)[bucketNum].readLock().lock();
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                int res = 0;
                int count = 0;
                while((res = lock.tryLock(tid, type, ccType)) == 0)  {
                    try {
                        Thread.sleep(0, 100);
                        // System.out.println(Thread.currentThread().getName() + " wait for lock " + key + " " + table + ", lock type is " + type);
                    } catch (InterruptedException ex) {
                        System.out.println("out of the max retry count, lock type is " + type);
                        res = -1;
                        break;
                    }
                    count++;
                }
                if (res > 0) {
                    validationBucketLocks.get(table)[bucketNum].readLock().unlock();
                    return true;
                } else {
                    // can not keep the sequence of read and write
                    String msg = "Transaction #" + tid + " can not keep the sequence of rw dependency, there maybe rw-anti-dependency";
                    validationBucketLocks.get(table)[bucketNum].readLock().unlock();
                    throw new SQLException(msg, "500");
                }
            }
        }
        validationBucketLocks.get(table)[bucketNum].readLock().unlock();
        return false;
    }

    public void tryAndAddValidationLock(String table, long tid, long key, LockType type, CCType ccType) throws SQLException {
        int bucketNum = (int)(key % HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].writeLock().lock();
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                int res;
                int count = 0;
                while((res = lock.tryLock(tid, type, ccType)) == 0)  {
                    try {
                        Thread.sleep(0, 10000);
                        System.out.println("xxxxxxxxxxxxxxxxxxxxx");
                    } catch (InterruptedException ex) {
                        System.out.println("out of the max retry count, lock type is " + type);
                        res = -1;
                        break;
                    }
                    count++;
                }
                validationBucketLocks.get(table)[bucketNum].writeLock().unlock();
                if (res <= 0) {
                    // can not keep the sequence of read and write
                    String msg = "can not keep the sequence of rw dependency, there maybe rw-anti-dependency";
                    throw new SQLException(msg, "500");
                }
                return;
            }
        }
        ValidationLock newLock = new ValidationLock(key);
        validationLocks.get(table)[bucketNum].add(newLock);
        newLock.tryLock(tid, type, ccType);
        validationBucketLocks.get(table)[bucketNum].writeLock().unlock();
    }

    private boolean checkAndTryLock(String table, long tid, long key, LockType type) throws SQLException {
        int bucketNum = (int)(key % HASH_SIZE);
        ccBucketLocks.get(table)[bucketNum].readLock().lock();
        List<XNORLock> lockList = ccLocks.get(table)[bucketNum];
        for (XNORLock lock: lockList) {
            if (lock.getKey() == key) {
                try {
                    lock.tryLock(tid, type);
                    ccBucketLocks.get(table)[bucketNum].readLock().unlock();
                    return true;
                } catch (SQLException ex){
                    // can not keep the sequence of read and write
                    ccBucketLocks.get(table)[bucketNum].readLock().unlock();
                    throw ex;
                }
            }
        }
        ccBucketLocks.get(table)[bucketNum].readLock().unlock();
        return false;
    }

    private void tryAndAddLock(String table, long tid, long key, LockType type) throws SQLException {
        int bucketNum = (int)(key % HASH_SIZE);
        ccBucketLocks.get(table)[bucketNum].writeLock().lock();
        List<XNORLock> lockList = ccLocks.get(table)[bucketNum];
        for (XNORLock lock: lockList) {
            if (lock.getKey() == key) {
                try {
                    lock.tryLock(tid, type);
                    ccBucketLocks.get(table)[bucketNum].readLock().unlock();
                    return;
                } catch (SQLException ex){
                    // can not keep the sequence of read and write
                    ccBucketLocks.get(table)[bucketNum].readLock().unlock();
                    throw ex;
                }
            }
        }
        XNORLock newLock = new XNORLock(key);
        ccLocks.get(table)[bucketNum].add(newLock);
        newLock.tryLock(tid, type);
        ccBucketLocks.get(table)[bucketNum].writeLock().unlock();
    }

    public void tryLock(String table, long key, long tid, LockType type) throws SQLException {
        if (!ccLocks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }

        if (checkAndTryLock(table, tid, key, type))
            return;
        try {
            tryAndAddLock(table, tid, key, type);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void releaseLock(String table, long key, long tid) {
        if (!ccLocks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }
        int bucketNum = (int)(key % HASH_SIZE);
        ccBucketLocks.get(table)[bucketNum].readLock().lock();
        List<XNORLock> lockList = ccLocks.get(table)[bucketNum];
        for (XNORLock lock: lockList) {
            if (lock.getKey() == key) {
                lock.releaseLock(tid);
                break;
            }
        }
        ccBucketLocks.get(table)[bucketNum].readLock().unlock();

        // shrink the lock list for concurrency control
        if (ccBucketLocks.get(table)[bucketNum].writeLock().tryLock()) {
            lockList.removeIf(lock -> lock.getKey() > HASH_SIZE && lock.free());
            ccBucketLocks.get(table)[bucketNum].writeLock().unlock();
        }
    }

    public void updateHotspotVersion(String table, long key, long vid) {
        int bucketNum = (int)(key % HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].readLock().lock();
        for (ValidationLock lock: validationLocks.get(table)[bucketNum]) {
            if (lock.getId() == key) {
                lock.updateVersion(vid);
                break;
            }
        }
        validationBucketLocks.get(table)[bucketNum].readLock().unlock();
    }

    public long getHotspotVersion(String table, long key) {
        if (!validationLocks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }
        int bucketNum = (int)(key % HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].readLock().lock();
        for (ValidationLock lock: validationLocks.get(table)[bucketNum]) {
            if (lock.getId() == key) {
                validationBucketLocks.get(table)[bucketNum].readLock().unlock();
                return lock.getVersion();
            }
        }
        validationBucketLocks.get(table)[bucketNum].readLock().unlock();
        return -1;
    }

    public static LockTable getInstance() {
        return INSTANCE;
    }

    public static int[] getCustomerIdsFromIndex(int index) {
        int w_id = (index / 30000) + 1;
        int d_id = ((index % 30000) / 3000) + 1;
        int c_id = (index % 3000) + 1;
        return new int[]{w_id, d_id, c_id};
    }

    public static int[] getStockIdsFromIndex(int index) {
        int w_id = (index / 100000) + 1;
        int i_id = (index % 100000) + 1;
        return new int[]{w_id, i_id};
    }

}
