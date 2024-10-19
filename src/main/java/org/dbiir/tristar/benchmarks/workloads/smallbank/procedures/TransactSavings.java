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

/**
 * TransactSavings Procedure Original version by Mohammad Alomari and Michael Cahill
 *
 * @author pavlo
 */
public class TransactSavings extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE name = ? FOR UPDATE");

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalanceForUpdate =
          new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ? FOR UPDATE");

  public final SQLStmt UpdateSavingsBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_SAVINGS
              + "   SET bal = bal + ?, tid = tid + 1 "
              + " WHERE custid = ? "
              + " RETURNING tid");

  public void run(Worker worker, Connection conn, String custName, double amount, CCType type, long[] versions, long tid, int[] checkout) throws SQLException {
    // First convert the custName to the acctId
    long custId;

    if (type == CCType.RC_ELT || type == CCType.SI_ELT) {
      try (PreparedStatement stmtc = this.getPreparedStatement(conn, writeConflict, custName)) {
        try (ResultSet r0 = stmtc.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custName + "'";
            throw new UserAbortException(msg);
          }
        }
      }
    }

    try (PreparedStatement stmt = this.getPreparedStatement(conn, GetAccount, custName)) {
      try (ResultSet result = stmt.executeQuery()) {
        if (!result.next()) {
          String msg = "Invalid account '" + custName + "'";
          throw new UserAbortException(msg);
        }
        custId = result.getLong(1);
      }
    }

    // Get Balance Information

    // Then update their savings balance
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid, LockType.EX);
    }

    try (PreparedStatement stmt =
        this.getPreparedStatement(conn, UpdateSavingsBalance, amount, custId)) {
      // TODO: return the savings version for validation
      try (ResultSet res = stmt.executeQuery()) {
        if (!res.next()) {
          String msg = "can not find the checking version for customer #%d".formatted(custId);
          throw new UserAbortException(msg);
        }
        versions[0] = res.getLong(1);
      } 
    } catch (SQLException ex) {
      throw ex;
    }

    if (type == CCType.RC_TAILOR && versions[0] < 0)
      System.out.println("custome error 5");

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

    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      LOG.debug("TransactSavings #" + tid + " acquire EX validation lock - checking #"+custId);
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId, LockType.EX, type);
      LOG.debug("TransactSavings #" + tid + " acquired EX validation lock - checking #"+custId);
    }
  }

  public void doAfterCommit(long custId, CCType type, boolean success, long[] versions, long tid, int[] checkout) {
    if (TransactionCollector.getInstance().isSample()) {
      TransactionCollector.getInstance().addTransactionSample(8,
              new RWRecord[]{},
              new RWRecord[]{new RWRecord(SmallBankConstants.TABLENAME_TO_INDEX.get(SmallBankConstants.TABLENAME_SAVINGS), (int) custId)},
              success?1:0);
    }
    if (!success) {
      return;
    }
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid);
    }
    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.EX);
      LOG.debug("TransactSavings #" + tid + " release EX validation lock - savings #"+custId);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId, versions[0]);
    }
  }
}
