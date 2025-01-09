/***************************************************************************
 *  Copyright (C) 2013 by H-Store Project                                  *
 *  Brown University                                                       *
 *  Massachusetts Institute of Technology                                  *
 *  Yale University                                                        *
 *                                                                         *
 *  Permission is hereby granted, free of charge, to any person obtaining  *
 *  a copy of this software and associated documentation files (the        *
 *  "Software"), to deal in the Software without restriction, including    *
 *  without limitation the rights to use, copy, modify, merge, publish,    *
 *  distribute, sublicense, and/or sell copies of the Software, and to     *
 *  permit persons to whom the Software is furnished to do so, subject to  *
 *  the following conditions:                                              *
 *                                                                         *
 *  The above copyright notice and this permission notice shall be         *
 *  included in all copies or substantial portions of the Software.        *
 *                                                                         *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,        *
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF     *
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. *
 *  IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR      *
 *  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,  *
 *  ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR  *
 *  OTHER DEALINGS IN THE SOFTWARE.                                        *
 ***************************************************************************/
package org.dbiir.tristar.benchmarks.workloads.smallbank.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.adapter.TransactionCollector;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.catalog.RWRecord;
import org.dbiir.tristar.benchmarks.util.StringUtil;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

/**
 * DepositChecking Procedure Original version by Mohammad Alomari and Michael Cahill
 *
 * @author pavlo
 */
public class DepositChecking extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE name = ? FOR UPDATE");

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt UpdateCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal + ?"
              + " WHERE custid = ?");

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
    templateSQLMetas.add(new TemplateSQLMeta("DepositChecking", 0, SmallBankConstants.TABLENAME_ACCOUNTS,
            0, "SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("DepositChecking", 1, SmallBankConstants.TABLENAME_CHECKING,
            1, "UPDATE " + SmallBankConstants.TABLENAME_CHECKING
            + " SET bal = bal + ? WHERE custid = ?"));
    return templateSQLMetas;
  }

  public void run(Worker worker, Connection conn, String custName, double amount, CCType type) throws SQLException {
    // First convert the custName to the custId
    long custId;
    if (type == CCType.RC_ELT) {
      try (PreparedStatement stmtc = this.getPreparedStatement(conn, writeConflict, custName)) {
        try (ResultSet r0 = stmtc.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custName + "'";
            throw new UserAbortException(msg);
          }
        }
      }
    }

    if (worker.useTxnSailsServer()) {
      try{
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "DepositChecking", clientServerIndexMap.get(0), custName));
        List<List<String>> result = worker.parseExecutionResults();
        try {
          custId = Long.parseLong(result.get(0).get(0));
        } catch (Exception ex) {
          String msg = "Invalid account '" + custName + "'";
          throw new UserAbortException(msg);
        }
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "DepositChecking", clientServerIndexMap.get(1), amount, custId));
        worker.parseExecutionResults();
      } catch (InterruptedException ex) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
      try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, custName)) {
        try (ResultSet r0 = stmt0.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custName + "'";
            throw new UserAbortException(msg);
          }
          custId = r0.getLong(1);
        }
      }

      // Then update their checking balance
      try (PreparedStatement stmt1 =
          this.getPreparedStatement(conn, UpdateCheckingBalance, amount, custId)) {
        int status = stmt1.executeUpdate();
      }
    }
  }

  public void doAfterCommit() {

  }
}
