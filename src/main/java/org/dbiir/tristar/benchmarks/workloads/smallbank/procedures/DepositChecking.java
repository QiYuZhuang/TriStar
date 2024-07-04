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

import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.SortedMap;
import java.util.concurrent.locks.Lock;

/**
 * DepositChecking Procedure Original version by Mohammad Alomari and Michael Cahill
 *
 * @author pavlo
 */
public class DepositChecking extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CONFLICT + " SET name = name" + " WHERE custid = ?");

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt UpdateCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + " SET bal = bal + ?, tid = tid + 1 "
              + " WHERE custid = ?;"
              + "SELECT"
              + " tid"
              + " FROM "
              + SmallBankConstants.TABLENAME_CHECKING
              + " WHERE custid = ?;");

  public void run(Connection conn, String custName, double amount, CCType type, long[] versions, long tid) throws SQLException {
    // First convert the custName to the custId
    long custId;

    try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, custName)) {
      try (ResultSet r0 = stmt0.executeQuery()) {
        if (!r0.next()) {
          String msg = "Invalid account '" + custName + "'";
          throw new UserAbortException(msg);
        }
        custId = r0.getLong(1);
      }
    }

    if (type == CCType.RC_ELT) {
      try (PreparedStatement stmtc = this.getPreparedStatement(conn, writeConflict, custId)) {
        int res = stmtc.executeUpdate();
        if (res == 0) {
          String msg = "Invalid account '" + custName + "'";
          throw new UserAbortException(msg);
        }
       /*
        try (ResultSet r0 = stmtc.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custName + "'";
            throw new UserAbortException(msg);
          }
        }
        */
      }
    }
    // Then update their checking balance
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid, LockType.EX);
    }
    try (PreparedStatement stmt1 = conn.prepareStatement((UpdateCheckingBalance.getSQL()))) {
      // fill the field
      stmt1.setDouble(1, amount);
      stmt1.setLong(2, custId);
      stmt1.setLong(3, custId);

      boolean resultsAvailable = stmt1.execute();
      while (true) {
        if (resultsAvailable) {
          ResultSet rs = stmt1.getResultSet();
          if (!rs.next()) {
            throw new UserAbortException("unknown exception");
          }
          versions[0] = rs.getLong(1);
        } else if (stmt1.getUpdateCount() < 0) {
          break;
        }

        resultsAvailable = stmt1.getMoreResults();
      }
    }

    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId, LockType.EX, type);
    }
  }

  public void doAfterCommit(long custId, CCType type, boolean success, long[] versions, long tid) {
    if (!success)
      return;
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid);
    }
    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId, LockType.EX);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId, versions[0]);
    }
  }
}
