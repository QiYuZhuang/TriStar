/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dbiir.tristar.benchmarks.workloads.ycsb.procedures;

import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.adapter.TransactionCollector;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.catalog.RWRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static org.dbiir.tristar.benchmarks.workloads.ycsb.YCSBConstants.TABLE_NAME;


public class ReadRecord extends Procedure {
    private int txnLength = 5; 
    public final SQLStmt readStmt = new SQLStmt("SELECT vid, FIELD1 FROM " + TABLE_NAME + " WHERE YCSB_KEY=?;");

    // FIXME: The value in ysqb is a byteiterator
    public void run(Connection conn, int[] keyname) throws SQLException {
        int[] sortedKeyname = new int[keyname.length];
        Arrays.sort(keyname);
        for (int i = 0; i < keyname.length; i++) {
            sortedKeyname[i] = keyname[keyname.length - i - 1];
        }

        System.arraycopy(sortedKeyname, 0, keyname, 0, keyname.length);

        StringBuilder finalStmt = new StringBuilder();

        for (int i = 0; i < txnLength; i++) {
            finalStmt.append(readStmt.getSQL());
        }
        
        SQLStmt sqlStmt = new SQLStmt(finalStmt.toString());
        try (PreparedStatement stmt = this.getPreparedStatement(conn, sqlStmt)) {
            int idx = 1;
            for (int i = 0; i < txnLength; i++) {
                stmt.setInt(idx++, sortedKeyname[i]);
            }

            boolean resultsAvailable = stmt.execute();
            idx = 0;
            while (true) {
                if (resultsAvailable) {
                    ResultSet rs = stmt.getResultSet();
                    rs.next();
                } else if (stmt.getUpdateCount() < 0) {
                    idx++;
                    break;
                }

                resultsAvailable = stmt.getMoreResults();
            }
        }
    }

    public void doAfterCommit(int[] keynames, boolean success, long latency) {
        if (TransactionCollector.getInstance().isSample()) {
            int rset_idx = 0;
            RWRecord[] reads = new RWRecord[txnLength];
            for (int i = 0; i < txnLength; i++) {
                reads[rset_idx++] = new RWRecord(i + 1, YCSBConstants.TABLENAME_TO_INDEX.get(TABLE_NAME), keynames[i]);
            }
            TransactionCollector.getInstance().addTransactionSample(TAdapter.getInstance().getTypesByName("ReadRecord").getId(), reads, new RWRecord[0], success ? 1 : 0, latency);
        }
    }
}
