package org.dbiir.tristar.benchmarks.workloads.ycsb.procedures;

import static org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants.TABLE_NAME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.adapter.TransactionCollector;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.catalog.RWRecord;
import org.dbiir.tristar.benchmarks.util.StringUtil;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

public class ReadWriteRecord extends Procedure {
    public final SQLStmt readStmt = new SQLStmt(
        "SELECT FIELD1 FROM " + TABLE_NAME + " WHERE YCSB_KEY=?;"
    );

    public final SQLStmt selectForUpdate = new SQLStmt(
        "UPDATE " + TABLE_NAME +
                " AS new SET FIELD1=old.FIELD1, vid = old.vid + 1 FROM " +
                TABLE_NAME +
                " AS old WHERE new.YCSB_KEY = ? " +
                " AND old.YCSB_KEY=new.YCSB_KEY RETURNING new.FIELD1;"
    );

    public final SQLStmt updateStmt = new SQLStmt(
        "UPDATE " + TABLE_NAME + " SET FIELD1=? WHERE YCSB_KEY=?;"
    );

    public final SQLStmt conflictStmt = new SQLStmt(
            "UPDATE ycsb_conflict SET vid=vid+1 WHERE YCSB_KEY=? " +
                    "RETURNING vid;"
    );

    private int[] ops = new int[10];

    static HashMap<Integer, Integer> clientServerIndexMap = new HashMap<>();
    static {
        clientServerIndexMap.put(0, -1);
        clientServerIndexMap.put(1, -1);
    }

    @Override
    public void updateClientServerIndexMap(int clientSideIndex, int serverSideIndex) {
        clientServerIndexMap.put(clientSideIndex, serverSideIndex);
    }

    @Override
    public List<TemplateSQLMeta> getTemplateSQLMetas() {
        List<TemplateSQLMeta> templateSQLMetas = new LinkedList<>();
        templateSQLMetas.add(new TemplateSQLMeta("ReadWrite", 0, TABLE_NAME,
                0, "SELECT FIELD1 FROM " + TABLE_NAME + " WHERE YCSB_KEY=?;"));
        templateSQLMetas.add(new TemplateSQLMeta("ReadWrite", 1, TABLE_NAME,
                1, "UPDATE " + TABLE_NAME + " SET FIELD1=? WHERE YCSB_KEY=?;"));
        return templateSQLMetas;
    }

    /*
     * @param ratio1: <read transaction : write transaction> (read transaction represents read-only)
     * @param ratio2: <read operation : write operation> (in write transaction)
     */
    public void run(Worker worker, Connection conn, int[] keyname, String[][] vals, double ratio1, double ratio2, CCType type) throws SQLException {
        /*
         * if ratio1 = 0, it is a read-only transaction;
         * if ratio2 = 0, the transaction's operations are read operation;
         */
        int[] sortedKeyname = new int[keyname.length];
        Arrays.sort(keyname);
        for (int i = 0; i < keyname.length; i++) {
            sortedKeyname[i] = keyname[keyname.length - i - 1];
        }

        System.arraycopy(sortedKeyname, 0, keyname, 0, keyname.length);

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

        for (int i = 0; i < len; i++) {
            if (worker.useTxnSailsServer()) {
                try {
                    if (ops[i] == 1) {
                        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "ReadWriteRecord", 0, sortedKeyname[i]));
                    } else if (ops[i] == 2) {
                        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "ReadWriteRecord", 1, vals[i][0], sortedKeyname[i]));
                    }
                    worker.parseExecutionResults();
                } catch (InterruptedException ex) {
                    System.out.println("InterruptedException on sending or receiving message");
                }
            } else {
                if (ops[i] == 1) {
                    try (PreparedStatement stmt = switch (type) {
                        case SI_FOR_UPDATE, RC_FOR_UPDATE -> conn.prepareStatement(selectForUpdate.getSQL());
                        default -> conn.prepareStatement(readStmt.getSQL());
                    }) {
                        stmt.setInt(1, sortedKeyname[i]);
                        boolean rs = stmt.execute();
                        if (rs) {
                            try (ResultSet r = stmt.getResultSet()) {
                                while (r.next()) {
                                    // do nothing
                                }
                            }
                        }
                        stmt.getResultSet().close();
                    }
                } else if (ops[i] == 2) {
                    try (PreparedStatement stmt = conn.prepareStatement(updateStmt.getSQL())) {
                        stmt.setString(1, vals[i][0]);
                        stmt.setInt(2, sortedKeyname[i]);
                        boolean rs = stmt.execute();
                        if (rs) {
                            stmt.getResultSet().close();
                        }
                    }
                }
            }
        }
    }

    public void doAfterCommit(int[] keynames, CCType type, boolean success, boolean validateFinished, long[] versions, long tid, boolean old, long latency) {
        return;
    }

    private void doConflictOperations(Connection conn, int[] keynames) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(String.valueOf(conflictStmt.getSQL()).repeat(keynames.length));

        try (PreparedStatement stmt = conn.prepareStatement(stringBuilder.toString())) {
            int idx = 1;
            for (int keyname : keynames) {
                stmt.setInt(idx++, keyname);
            }
            boolean rs = stmt.execute();
            if (rs) {
                stmt.getResultSet().close();
            }
        }
    }
}
