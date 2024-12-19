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
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.FlowRate;
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
              + "   SET bal = bal + ?, tid = tid + 1 "
              + " WHERE custid = ?"
              + " RETURNING tid");

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

  public void run(Worker worker, Connection conn, String custName, double amount, CCType type, long[] versions, long tid, int[] checkout) throws SQLException {
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

    if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR || type == CCType.DYNAMIC) {
      try{
        worker.getChannelFuture().channel().writeAndFlush(joinValuesWithHash(0, GetAccount, custName)).sync();
        // TODO: read custId from above result
        custId = 0;
        worker.getChannelFuture().channel().writeAndFlush(joinValuesWithHash(1, UpdateCheckingBalance, amount, custId)).sync();
      } catch (InterruptedException ex) {
        // pass
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
        try (ResultSet res = stmt1.executeQuery()) {
          if (!res.next()) {
            throw new UserAbortException("unknown exception");
          }
          versions[0] = res.getLong(1);
        }
      } catch (SQLException ex) {
        throw ex;
      }
    }

    
    while (TAdapter.getInstance().isInSwitchPhase() && !TAdapter.getInstance().isAllWorkersReadyForSwitch()) {
      // set current thread ready, block for all thread to ready
      if (!worker.isSwitchPhaseReady()) {
        worker.setSwitchPhaseReady(true);
        System.out.println(Thread.currentThread().getName() + " is ready for switch");
      } else {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
        }
      }
    }
    if (TAdapter.getInstance().isInSwitchPhase()) {
      type = TAdapter.getInstance().getSwitchPhaseCCType();
    }
  }

  public void doAfterCommit(long custId, CCType type, boolean success, long[] versions, long tid, int[] checkout) {
    if (TransactionCollector.getInstance().isSample()) {
      TransactionCollector.getInstance().addTransactionSample(4,
              new RWRecord[]{},
              new RWRecord[]{new RWRecord(SmallBankConstants.TABLENAME_TO_INDEX.get(SmallBankConstants.TABLENAME_CHECKING), (int) custId)},
              success?1:0);
    }

    if (!success) {
      return;
    }
  }

  public static String joinValuesWithHash(Object... values) {
    // If input is empty, return an empty string
    if (values == null || values.length == 0) {
        return "";
    }
    
    // Use StringBuilder to efficiently concatenate strings
    StringBuilder result = new StringBuilder();
    
    // Iterate through each value and join them with #
    for (int i = 0; i < values.length; i++) {
        result.append(String.valueOf(values[i]));  // Convert to string
        // If it's not the last value, append a # separator
        if (i < values.length - 1) {
            result.append("#");
        }
    }
    
    return result.toString();
  }

}
