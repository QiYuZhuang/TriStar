package transaction;

import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.dbiir.tristar.transaction.concurrency.XNORLock;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;

public class TestLockTable {

    @Test
    void TestSHLock() {
        String tableName = "savings";
        try {
            LockTable.getInstance().tryLock(tableName, 1, 1, LockType.SH);
        } catch (SQLException ex) {

        }

        LockTable.getInstance().releaseLock(tableName, 1, 1);
    }

    @Test
    void TestEXLock() {
        String tableName = "savings";
        try {
            LockTable.getInstance().tryLock(tableName, 1, 1, LockType.EX);
        } catch (SQLException ex) {

        }

        LockTable.getInstance().releaseLock(tableName, 1, 1);
    }

    @Test
    void TestEXLock2() {
        String tableName = "savings";
        try {
            LockTable.getInstance().tryLock(tableName, 1, 1, LockType.EX);
            LockTable.getInstance().tryLock(tableName, 1, 2, LockType.EX);
        } catch (SQLException ex) {

        }

        LockTable.getInstance().releaseLock(tableName, 1, 1);
        LockTable.getInstance().releaseLock(tableName, 1, 2);

        try {
            LockTable.getInstance().tryLock(tableName, 1, 3, LockType.SH);
        } catch (SQLException ex) {

        }
        LockTable.getInstance().releaseLock(tableName, 1, 3);
    }
}
