package org.dbiir.tristar.transaction.concurrency;

import lombok.Getter;
import lombok.Setter;
import org.dbiir.tristar.common.LockStrategy;
import org.dbiir.tristar.common.LockType;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class XNORLock {
    private LockType type;
    private final List<LockEntry> waitList;
    // debugfinal
    private final List<LockEntry> lockList;
    private int count;
    private final Lock lock;
    private final LockStrategy strategy; // 1 for no-wait, 2 for wait-die
    @Getter
    private final long key;
    static private long lockWaitTimeout = 10;

    public XNORLock() {
        this.type = LockType.NoneType;
        this.waitList = new LinkedList<>();
        this.lockList = new LinkedList<>();
        this.count = 0;
        this.key = 0;
        lock = new ReentrantLock();
        strategy = LockStrategy.NO_WAIT;
    }

    public XNORLock(long key) {
        this.type = LockType.NoneType;
        this.waitList = new LinkedList<>();
        this.lockList = new LinkedList<>();
        this.count = 0;
        this.key = key;
        lock = new ReentrantLock();
        strategy = LockStrategy.NO_WAIT;
    }

    public void tryLock(long transactionId, LockType type) throws SQLException {
        lock.lock();
        try {
            if (!waitList.isEmpty() || !tolerate(this.type, type)) {
                if (strategy == LockStrategy.NO_WAIT) {
                    lock.unlock();
                    String msg = "Abort due to no wait, transaction id: " + transactionId;
                    throw new SQLException(msg, "500");
                }
                if (!admitByWaitDieStrategy(transactionId)) {
                    lock.unlock();
                    String msg = "Abort due to wait die, transaction id: " + transactionId;
                    throw new SQLException(msg, "500");
                }
                LockEntry entry = new LockEntry(transactionId, type);
                waitList.add(entry);
                lock.unlock();
                long startTime = System.currentTimeMillis();
                while (!entry.isGrantee()) {
                    Thread.sleep(0, 1000);
                    if (System.currentTimeMillis() - startTime > lockWaitTimeout) {
                        lock.lock();
                        waitList.removeIf(e -> e.transactionId == transactionId);
                        lock.unlock();
                        String msg = "Abort due to wait die and timeout, transaction id: " + transactionId;
                        throw new SQLException(msg, "500");
                    }
                }
            } else {
                this.type = type;
                lockList.add(new LockEntry(transactionId, type, true));
                lock.unlock();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean releaseLock(long tid) {
        lock.lock();
        try {
            lockList.removeIf(entry -> entry.transactionId == tid);
            if (!lockList.isEmpty())
                return true;

            this.type = LockType.NoneType;
            if (!waitList.isEmpty()) {
                Iterator<LockEntry> waitListIterator = waitList.iterator();
                while (waitListIterator.hasNext()) {
                    LockEntry llock = waitListIterator.next();
                    if (tolerate(this.type, llock.lockType)) {
                        lockList.add(llock);
                        waitListIterator.remove();
                        llock.setGrantee(true);
                    } else {
                        break;
                    }
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    private boolean tolerate(LockType type1, LockType type2) {
        if (type1 == LockType.NoneType || type2 == LockType.NoneType)
            return true;
//        if (type1 == LockType.EX || type2 == LockType.EX)
//            return false;
        return type1 == type2;
    }

    private boolean admitByWaitDieStrategy(long tid) {
        for (LockEntry entry: lockList) {
            assert (tid != entry.transactionId);
            if (tid > entry.transactionId)
                return false;
        }

        for (LockEntry entry: waitList) {
            assert (tid != entry.transactionId);
            if (tid > entry.transactionId)
                return false;
        }
        return true;
    }

    public boolean free() {
        if (lock.tryLock())
            return false;
        if (lockList.isEmpty() && waitList.isEmpty()) {
            lock.unlock();
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{type: { ")
                .append(type).append(" }, \nlock list: { \n");
        for (LockEntry entry: lockList) {
            str.append(entry);
        }
        str.append(" }, wait list: { \n");
        for (LockEntry entry: waitList) {
            str.append(entry);
        }
        str.append(" }}");
        return str.toString();
    }

    public class LockEntry {
        private final long transactionId;
        private final LockType lockType;
        @Getter
        @Setter
        private boolean grantee;
        private final long enterTime;

        public LockEntry(long transactionId, LockType type) {
            this.transactionId = transactionId;
            this.lockType = type;
            this.enterTime = System.currentTimeMillis();
            this.grantee = false;
        }

        public LockEntry(long transactionId, LockType type, boolean grantee) {
            this.transactionId = transactionId;
            this.lockType = type;
            this.enterTime = System.currentTimeMillis();
            this.grantee = grantee;
        }

        public boolean Timeout() {
            return (System.nanoTime() - enterTime) / 1000000 > 5000;
        }

        public String toString() {
            return "{transaction id: { " +
                    transactionId +
                    " }, type: { " +
                    lockType +
                    " }, enter time: { " +
                    enterTime + "}}";
        }
    }
}
