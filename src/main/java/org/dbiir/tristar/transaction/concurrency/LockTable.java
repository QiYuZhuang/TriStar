package org.dbiir.tristar.transaction.concurrency;

import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.common.LockType;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LockTable {
    private static LockTable INSTANCE = new LockTable();
    HashMap<String, ConcurrentHashMap<String, XNORLock>> locks = new HashMap<>(4);

    static {
        INSTANCE = new LockTable();
    }

    public LockTable() {
        locks.put("accounts", new ConcurrentHashMap<>(256));
        locks.put("savings", new ConcurrentHashMap<>(256));
        locks.put("checking", new ConcurrentHashMap<>(256));
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

    public static LockTable getInstance() {
        return INSTANCE;
    }
}
