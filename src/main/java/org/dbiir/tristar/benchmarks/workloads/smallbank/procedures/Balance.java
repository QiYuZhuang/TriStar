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
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

public class Balance extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE name = ? FOR UPDATE");

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal, tid FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " AS new SET bal = old.bal FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid=new.custid RETURNING old.bal");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal, tid FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " AS new SET bal = old.bal FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid=new.custid RETURNING old.bal");

  static HashMap<Integer, Integer> clientServerIndexMap = new HashMap<>();
  static {
    clientServerIndexMap.put(0, -1);
    clientServerIndexMap.put(1, -1);
    clientServerIndexMap.put(2, -1);
  }

  @Override
  public void updateClientServerIndexMap(int clientSideIndex, int serverSideIndex) {
    clientServerIndexMap.put(clientSideIndex, serverSideIndex);
  }

  @Override
  public List<TemplateSQLMeta> getTemplateSQLMetas() {
    List<TemplateSQLMeta> templateSQLMetas = new LinkedList<>();
    templateSQLMetas.add(new TemplateSQLMeta("Balance", 0, SmallBankConstants.TABLENAME_ACCOUNTS,
            0, "SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("Balance", 0, SmallBankConstants.TABLENAME_SAVINGS,
            1, "SELECT * FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE name = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("Balance", 0, SmallBankConstants.TABLENAME_CHECKING,
            2, "SELECT * FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE name = ?"));
    return templateSQLMetas;
  }

  public double run(Worker worker, Connection conn, String custName, CCType type, long[] versions, long tid) throws SQLException {
    // First convert the acctName to the acctId
    long custId;
    double savingsBalance = 0;
    double checkingBalance = 0;
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
        worker.getChannelFuture().channel().writeAndFlush(joinValuesWithHash(0, GetAccount, custName)).sync();
        // TODO: get custId from above result
        custId = 0;
        worker.getChannelFuture().channel().writeAndFlush(joinValuesWithHash(1,  GetSavingsBalanceForUpdate, custId)).sync();
        worker.getChannelFuture().channel().writeAndFlush(joinValuesWithHash(2, GetCheckingBalanceForUpdate, custId)).sync();
        // TODO: get savingsBalance and savingsBalance from above result
        savingsBalance = 0;
        checkingBalance = 0;
        } catch (InterruptedException ex) {
        // pass
      }
    } else {
      try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, custName)) {
        try (ResultSet r0 = stmt0.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custName + "'";
            System.out.println("UserAbortException: " + msg);
            throw new UserAbortException(msg);
          }
          custId = r0.getLong(1);
        }
      }
  
      // Then get their account balances
      try (
        PreparedStatement balStmt0 = switch (type) {
          case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetSavingsBalanceForUpdate, custId);
          default -> this.getPreparedStatement(conn, GetSavingsBalance, custId);
        }) {
        try (ResultSet balRes0 = balStmt0.executeQuery()) {
          if (!balRes0.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
            throw new UserAbortException(msg);
          }
          savingsBalance = balRes0.getDouble(1);
        }
      }
  
      try (PreparedStatement balStmt1 = switch (type) {
        case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetCheckingBalanceForUpdate, custId);
        default -> this.getPreparedStatement(conn, GetCheckingBalance, custId);
      }) {
        try (ResultSet balRes1 = balStmt1.executeQuery()) {
          if (!balRes1.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
            throw new UserAbortException(msg);
          }
          checkingBalance = balRes1.getDouble(1);
        }
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

    return checkingBalance + savingsBalance;
  }

  private void releaseTailorLock(int phase, long custId, long tid) {
    if (phase == 1) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid);
    } else if (phase == 2) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid);
    }
  }

  private void releaseTailorValidationLock(int phase, long custId, long tid) {
    if (phase == 1) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.SH);
      LOG.debug("Balance #" + tid + " release SH validation lock - savings #"+custId);
    } else if (phase == 2) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId, LockType.SH);
      LOG.debug("Balance #" + tid + " release SH validation lock - checking #"+custId);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.SH);
      LOG.debug("Balance #" + tid + " release SH validation lock - savings #"+custId);
    }
  }

  public void doAfterCommit(long custId, CCType type, boolean success, long[] versions, long tid, long latency) {
    // transaction sample
    if (TransactionCollector.getInstance().isSample()) {
      TransactionCollector.getInstance().addTransactionSample(TAdapter.getInstance().getTypesByName("Balance").getId(),
              new RWRecord[]{new RWRecord(1, SmallBankConstants.TABLENAME_TO_INDEX.get(SmallBankConstants.TABLENAME_SAVINGS), (int) custId),
                            new RWRecord(2, SmallBankConstants.TABLENAME_TO_INDEX.get(SmallBankConstants.TABLENAME_CHECKING), (int) custId)},
              new RWRecord[]{}, success?1:0, latency);
    }

    if (!success)
      return;
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
