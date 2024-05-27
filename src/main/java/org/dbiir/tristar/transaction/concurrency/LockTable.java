package org.dbiir.tristar.transaction.concurrency;

import lombok.Getter;
import lombok.Setter;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockTable {
    private static final LockTable INSTANCE;
    private static final int SMALL_BANK_HASH_SIZE = 10000;
    private HashMap<String, ConcurrentHashMap<String, XNORLock>> locks = new HashMap<>(4);
    private HashMap<String ,LinkedList<ValidationLock>[]> validationLocks = new HashMap<>(4);
    private HashMap<String, ReentrantReadWriteLock[]> validationBucketLocks = new HashMap<>(4);
    private final long lockWaitTimeout = 10;
    private final int maxRetry = 5;
    public final SQLStmt GetSavingsBalance =
            new SQLStmt("SELECT tid FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");
    public final SQLStmt GetCheckingBalance =
            new SQLStmt("SELECT tid FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

    static {
        INSTANCE = new LockTable();
    }

    public LockTable() {

    }

    public void initHotspot(String workload, Connection connection) throws SQLException {
        if (workload.equals("smallbank")) {
            locks.put("accounts", new ConcurrentHashMap<>(256));
            locks.put("savings", new ConcurrentHashMap<>(256));
            locks.put("checking", new ConcurrentHashMap<>(256));

            // init validation locks
            validationLocks.put("savings", new LinkedList[SMALL_BANK_HASH_SIZE]);
            validationLocks.put("checking", new LinkedList[SMALL_BANK_HASH_SIZE]);
            validationBucketLocks.put("savings", new ReentrantReadWriteLock[SMALL_BANK_HASH_SIZE]);
            validationBucketLocks.put("checking", new ReentrantReadWriteLock[SMALL_BANK_HASH_SIZE]);
            for (int i = 0; i < SMALL_BANK_HASH_SIZE; i++) {
                validationLocks.get("savings")[i] = new LinkedList<>();
                validationLocks.get("checking")[i] = new LinkedList<>();
                validationLocks.get("savings")[i].add(new ValidationLock(i));
                validationLocks.get("checking")[i].add(new ValidationLock(i));
                validationBucketLocks.get("savings")[i] = new ReentrantReadWriteLock();
                validationBucketLocks.get("checking")[i] = new ReentrantReadWriteLock();
            }
            PreparedStatement getSavings = connection.prepareStatement(GetSavingsBalance.getSQL());
            PreparedStatement getChecking = connection.prepareStatement(GetCheckingBalance.getSQL());

            for (int i= 0; i < SMALL_BANK_HASH_SIZE; i++) {
               if (!validationLocks.get("savings")[i].isEmpty()) {
                   getSavings.setLong(1, i);
                   try (ResultSet res = getSavings.executeQuery()) {
                       if (!res.next()) {
                           String msg = "Invalid custid #%d".formatted(i);
                           throw new RuntimeException(msg);
                       }
                       updateHotspotVersion("savings", i, res.getLong(1));
                   }
               }

                if (!validationLocks.get("checking")[i].isEmpty()) {
                    getChecking.setLong(1, i);
                    try (ResultSet res = getChecking.executeQuery()) {
                        if (!res.next()) {
                            String msg = "Invalid custid #%d".formatted(i);
                            throw new RuntimeException(msg);
                        }
                        updateHotspotVersion("checking", i, res.getLong(1));
                    }
                }
            }
        }
    }

    public void tryValidationLock(String table, long tid, long key, long vid, LockType type, CCType ccType) throws SQLException {
        if (!locks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }

        if (checkAndTryValidationLock(table, tid, key, type, ccType))
            return;
        try {
            tryAndAddValidationLock(table, tid, key, vid, type, ccType);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public void tryValidationLock(String table, long tid, long key, LockType type, CCType ccType) throws SQLException {
        if (!locks.containsKey(table)) {
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
        if (!locks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }
        int bucketNum = (int)(key % SMALL_BANK_HASH_SIZE);
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
            lockList.removeIf(lock -> lock.getId() > SMALL_BANK_HASH_SIZE && lock.free());
            validationBucketLocks.get(table)[bucketNum].writeLock().unlock();
        }
    }

    /*
     * @return: true if you get validation lock, otherwise can not find the validation lock
     */
    private boolean checkAndTryValidationLock(String table, long tid, long key, LockType type, CCType ccType) throws SQLException {
        int bucketNum = (int)(key % SMALL_BANK_HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].readLock().lock();
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                int res = 0;
                int count = 0;
                while((res = lock.tryLock(tid, type, ccType)) == 0)  {
                    try {
                        Thread.sleep(0, 10000);
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
                    String msg = "can not keep the sequence of rw dependency, there maybe rw-anti-dependency";
                    validationBucketLocks.get(table)[bucketNum].readLock().unlock();
                    throw new SQLException(msg, "500");
                }
            }
        }
        validationBucketLocks.get(table)[bucketNum].readLock().unlock();
        return false;
    }

    public void tryAndAddValidationLock(String table, long tid, long key, LockType type, CCType ccType) throws SQLException {
        int bucketNum = (int)(key % SMALL_BANK_HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].writeLock().lock();
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                int res;
                int count = 0;
                while((res = lock.tryLock(tid, type, ccType)) == 0)  {
                    try {
                        Thread.sleep(0, 10000);
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
            }
        }
        ValidationLock newLock = new ValidationLock(key);
        validationLocks.get(table)[bucketNum].add(newLock);
        newLock.tryLock(tid, type, ccType);
        validationBucketLocks.get(table)[bucketNum].writeLock().unlock();
    }

    public void tryAndAddValidationLock(String table, long tid, long key, long vid, LockType type, CCType ccType) throws SQLException {
        int bucketNum = (int)(key % SMALL_BANK_HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].writeLock().lock();
        List<ValidationLock> lockList = validationLocks.get(table)[bucketNum];
        for (ValidationLock lock: lockList) {
            if (lock.getId() == key) {
                int res;
                int count = 0;
                while((res = lock.tryLock(tid, type, ccType)) == 0 && count < maxRetry)  {
                    try {
                        Thread.sleep(0, 10000);
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
            }
        }
        ValidationLock newLock = new ValidationLock(key);
        newLock.updateVersion(vid);
        validationLocks.get(table)[bucketNum].add(newLock);
        newLock.tryLock(tid, type, ccType);
        validationBucketLocks.get(table)[bucketNum].writeLock().unlock();
    }

    public boolean tryLock(String table, String key, long tid, LockType type) throws SQLException {
        if (!locks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }

        ConcurrentHashMap<String, XNORLock> lockSet = locks.get(table);
        if (!lockSet.containsKey(key)) {
            lockSet.putIfAbsent(key, new XNORLock());
        }
        XNORLock lock = lockSet.get(key);
        return lock.requestLock(tid, type);
    }

    public boolean releaseLock(String table, String key, long tid) {
        if (!locks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }

        ConcurrentHashMap<String, XNORLock> lockSet = locks.get(table);
        if (!lockSet.containsKey(key)) {
            String msg = "unknown key name: " + key;
            throw new RuntimeException(msg);
        }
        XNORLock lock = lockSet.get(key);
        return lock.releaseLock(tid);
    }

    public void updateHotspotVersion(String table, long key, long tid) {
        int bucketNum = (int)(key % SMALL_BANK_HASH_SIZE);
        validationBucketLocks.get(table)[bucketNum].readLock().lock();
        for (ValidationLock lock: validationLocks.get(table)[bucketNum]) {
            if (lock.getId() == key) {
                lock.updateVersion(tid);
                break;
            }
        }
        validationBucketLocks.get(table)[bucketNum].readLock().unlock();
    }

    public long getHotspotVersion(String table, long key) {
        if (!locks.containsKey(table)) {
            String msg = "unknown table name: " + table;
            throw new RuntimeException(msg);
        }
        int bucketNum = (int)(key % SMALL_BANK_HASH_SIZE);
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
}
