package org.dbiir.tristar.benchmarks.workloads.ycsb.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.adapter.TransactionCollector;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import static org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants.TABLE_NAME;

import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.catalog.RWRecord;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;

public class ReadWriteRecord extends Procedure {
    public final SQLStmt readStmt = new SQLStmt(
        "SELECT vid, FIELD1 FROM " + TABLE_NAME + " WHERE YCSB_KEY=?;"
    );

    public final SQLStmt selectForUpdate = new SQLStmt(
        "UPDATE " + TABLE_NAME +
                " AS new SET FIELD1=old.FIELD1 FROM " +
                TABLE_NAME +
                " AS old WHERE new.YCSB_KEY = ? " +
                " AND old.YCSB_KEY=new.YCSB_KEY RETURNING new.vid, new.FIELD1;"
    );

    public final SQLStmt updateStmt = new SQLStmt(
        "UPDATE " + TABLE_NAME + " SET vid=vid+1, FIELD1=?,FIELD2=?,FIELD3=?,FIELD4=?,FIELD5=?," +
            "FIELD6=?,FIELD7=?,FIELD8=?,FIELD9=?,FIELD10=? WHERE YCSB_KEY=?;"
    );

    public final SQLStmt conflictStmt = new SQLStmt(
            "UPDATE ycsb_conflict SET vid=vid+1 WHERE YCSB_KEY=? " +
                    "RETURNING vid;"
    );

    private int[] ops = new int[10];

    /*
     * @param ratio1: <read transaction : write transaction> (read transaction represents read-only)
     * @param ratio2: <read operation : write operation> (in write transaction)
     */
    public void run(Worker worker, Connection conn, int[] keyname, String[][] vals, double ratio1, double ratio2, long tid, long[] versions, CCType type) throws SQLException {
        /*
         * if ratio1 = 0, it is a read-only transaction;
         * if ratio2 = 0, the transaction's operations are read operation;
         */
        int[] sortedKeyname = new int[keyname.length];
        Arrays.sort(keyname);
        for (int i = 0; i < keyname.length; i++) {
            sortedKeyname[i] = keyname[keyname.length - i - 1];
        }

        int len = keyname.length;
        StringBuilder finalStmt = new StringBuilder();

        double rand1 = new Random().nextDouble();
        for (int i = 0; i < len; i++) {
            double rand2 = new Random().nextDouble();

            if (rand1 >= ratio1 || rand2 >= ratio2) {
                // read operation
                ops[i] = 1;
                // if (type == CCType.RC_FOR_UPDATE || type == CCType.SI_FOR_UPDATE)
                //     finalStmt.append(selectForUpdate.getSQL());
                // else
                //     finalStmt.append(readStmt.getSQL());
            } else {
                // write operation
                ops[i] = 2;
                // finalStmt.append(updateStmt.getSQL());
            }
        }
        Arrays.sort(ops);

        for (int i = 0; i < len; i++) {
            if (ops[i] == 1) {
                if (type == CCType.RC_FOR_UPDATE || type == CCType.SI_FOR_UPDATE)
                    finalStmt.append(selectForUpdate.getSQL());
                else
                    finalStmt.append(readStmt.getSQL());
            } else if (ops[i] == 2) {
                finalStmt.append(updateStmt.getSQL());
            }
        }

        if (type == CCType.RC_ELT || type == CCType.SI_ELT) {
            try {
                doConflictOperations(conn, sortedKeyname);
            } catch (SQLException ex) {
                throw ex;
            }
        }

        int phase = 0;
        if (type == CCType.RC_TAILOR_LOCK) {
            for (int i = 0; i < len; i++) {
                try {
                    if (ops[i] == 1)
                        LockTable.getInstance().tryLock(TABLE_NAME, sortedKeyname[i], tid, LockType.SH);
                    else
                        LockTable.getInstance().tryLock(TABLE_NAME, sortedKeyname[i], tid, LockType.EX);
                    phase++;
                } catch (SQLException ex) {
                    releaseTailorCCLock(phase, sortedKeyname, tid);
                    throw ex;
                }
            }
        }

        SQLStmt sqlStmt = new SQLStmt(finalStmt.toString());
        try (PreparedStatement stmt = conn.prepareStatement(sqlStmt.getSQL())) {
            // fill the field
            int idx = 1;
            for (int i = 0; i < len; i++) {
                if (ops[i] == 1) {
                    stmt.setInt(idx++, sortedKeyname[i]);
                } else if (ops[i] == 2) {
                    for (int j = 0; j < vals[i].length; j++) {
                        stmt.setString(idx++, vals[i][j]);
                    }
                    stmt.setInt(idx++, sortedKeyname[i]);
                }
            }

            boolean resultsAvailable = stmt.execute();
            idx = 0;
            while (true) {
                if (resultsAvailable) {
                    ResultSet rs = stmt.getResultSet();
                    rs.next();
                    versions[idx++] = rs.getLong(1);
                } else if (stmt.getUpdateCount() < 0) {
                    idx++;
                    break;
                }

                resultsAvailable = stmt.getMoreResults();
            }
        } catch (SQLException ex) {
            releaseTailorCCLock(phase, sortedKeyname, tid);
            throw ex;
        }

        if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR) {
            int validationPhase = 0;
            while (TAdapter.getInstance().isInSwitchPhase() && !TAdapter.getInstance().isAllWorkersReadyForSwitch()) {
                // set current thread ready, block for all thread to ready
                worker.setSwitchPhaseReady(true);
            }
            for (int i = 0; i < len; i++) {
                try {
                    if (ops[i] == 1)
                        LockTable.getInstance().tryValidationLock(TABLE_NAME, tid, sortedKeyname[i], LockType.SH, type);
                    else
                        LockTable.getInstance().tryValidationLock(TABLE_NAME, tid, sortedKeyname[i], LockType.EX, type);
                    validationPhase++;
                } catch (SQLException ex) {
                    releaseTailorValidationLock(validationPhase, sortedKeyname);
                    throw ex;
                }
            }
        }
    }

    private void releaseTailorCCLock(int phase, int[] keynames, long tid) {
        if (phase == 0)
            return;
        for (int i = phase; i > 0; i--) {
            LockTable.getInstance().releaseLock(TABLE_NAME, keynames[i - 1], tid);
        }
    }

    private void releaseTailorValidationLock(int phase, int[] keynames) {
        if (phase == 0)
            return;
        for (int i = phase; i > 0; i--) {
            if (ops[i - 1] == 1)
                LockTable.getInstance().releaseValidationLock(TABLE_NAME, keynames[i - 1], LockType.SH);
            else
                LockTable.getInstance().releaseValidationLock(TABLE_NAME, keynames[i - 1], LockType.EX);
        }
    }

    public void doAfterCommit(int[] keynames, CCType type, boolean success, long[] versions, long tid) {
        if (TransactionCollector.getInstance().isSample()) {
            int rset_idx = 0;
            int wset_idx = 0;
            int write_cnt = 0;
            for (int i = 0; i < ops.length; i++) {
                if (ops[i] == 2) write_cnt++;
            }

            RWRecord[] reads = new RWRecord[ops.length - write_cnt];
//                    {new RWRecord(YCSBConstants.TABLENAME_TO_INDEX.get(TABLE_NAME), (int) custId)};
            RWRecord[] writes = new RWRecord[write_cnt];
            for (int i = 0; i < ops.length; i++) {
                if (ops[i] == 1) {
                    reads[rset_idx++] = new RWRecord(YCSBConstants.TABLENAME_TO_INDEX.get(TABLE_NAME), keynames[i]);
                } else {
                    writes[wset_idx++] = new RWRecord(YCSBConstants.TABLENAME_TO_INDEX.get(TABLE_NAME), keynames[i]);
                }
            }
            TransactionCollector.getInstance().addTransactionSample(1, reads, writes, success?1:0);
        }
        if (!success)
            return;
        if (type == CCType.RC_TAILOR_LOCK) {
            // todo:
            for (int i = ops.length - 1; i >= 0; i--) {
                if (ops[i] == 1) {
                    LockTable.getInstance().releaseLock(TABLE_NAME, keynames[i], tid);
                }
            }
        }
        if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR) {
            for (int i = ops.length - 1; i >= 0; i--) {
                if (ops[i] == 1) {
                    LockTable.getInstance().releaseValidationLock(TABLE_NAME, keynames[i], LockType.SH);
                } else {
                    LockTable.getInstance().releaseValidationLock(TABLE_NAME, keynames[i], LockType.EX);
                    LockTable.getInstance().updateHotspotVersion(TABLE_NAME, keynames[i], versions[i]);
                }
            }
        }
    }

    private void doConflictOperations(Connection conn, int[] keynames) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < keynames.length; i++) {
            stringBuilder.append(conflictStmt.getSQL());
        }

        try (PreparedStatement stmt = conn.prepareStatement(stringBuilder.toString())) {
            int idx = 1;
            for (int i = 0; i < keynames.length; i++) {
                stmt.setInt(idx++, keynames[i]);
            }
            boolean rs = stmt.execute();
            if (rs) {
                stmt.getResultSet().close();
            }
        }
    }
}
