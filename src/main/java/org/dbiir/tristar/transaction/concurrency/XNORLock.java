package org.dbiir.tristar.transaction.concurrency;

import org.dbiir.tristar.common.LockType;

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

    public XNORLock() {
        this.type = LockType.NoneType;
        this.waitList = new LinkedList<>();
        this.lockList = new LinkedList<>();
        this.count = 0;
        lock = new ReentrantLock();
    }

    public Iterator<LockEntry> RequestLockDebug(String transactionId, LockType type) {
        lock.lock();
        try {
            if (!waitList.isEmpty() || (type != LockType.NoneType && this.type != type)) {
                waitList.add(new LockEntry(transactionId, type));
                return null;
            } else {
//                count++;
                this.type = type;
                lockList.add(new LockEntry(transactionId, type));
                return lockList.listIterator(lockList.size());
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean RequestLock(String transactionId, LockType type) {
        lock.lock();
        try {
            if (!waitList.isEmpty() || (type != LockType.NoneType && this.type != type)) {
                waitList.add(new LockEntry(transactionId, type));
                return false;
            } else {
                count++;
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean ReleaseLockDebug(Iterator<LockEntry> lockEntry) {
        lock.lock();
        try {
            count--;
            if (count > 0)
                return true;

            if (waitList.isEmpty()) {
                this.type = LockType.NoneType;
                return true;
            } else {
                this.type = waitList.get(0).lockType;
                Iterator<LockEntry> waitListIterator = waitList.iterator();
                while (waitListIterator.hasNext()) {
                    LockEntry llock = waitListIterator.next();
                    if (llock.lockType == this.type) {
                        lockList.add(llock);
                        waitListIterator.remove();
                        // TODO: invoke
                    } else {
                        break;
                    }
                }
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean ReleaseLock(Iterator<LockEntry> lockEntry) {
        lock.lock();
        try {
            count--;
            if (count > 0)
                return true;

            if (waitList.isEmpty()) {
                this.type = LockType.NoneType;
                return true;
            } else {
                this.type = waitList.get(0).lockType;
                Iterator<LockEntry> waitListIterator = waitList.iterator();
                while (waitListIterator.hasNext()) {
                    LockEntry llock = waitListIterator.next();
                    if (llock.lockType == this.type) {
                        count++;
                        waitListIterator.remove();
                        // TODO: invoke
                    } else {
                        break;
                    }
                }
                return true;
            }
        } finally {
            lock.unlock();
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
        private final String transactionId;
        private final LockType lockType;
        private final long enterTime;

        public LockEntry(String transactionId, LockType type) {
            this.transactionId = transactionId;
            this.lockType = type;
            this.enterTime = System.currentTimeMillis();
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
