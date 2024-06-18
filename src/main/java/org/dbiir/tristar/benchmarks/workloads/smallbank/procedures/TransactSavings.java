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

import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.FlowRate;
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

  public void run(Connection conn, String custName, double amount, CCType type, long[] versions, long tid, int[] checkout) throws SQLException {
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

//    double balance;

//    try (PreparedStatement stmt = switch (type) {
//      case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetSavingsBalanceForUpdate, custId);
//      default -> this.getPreparedStatement(conn, GetSavingsBalance, custId);
//    }) {
//      try (ResultSet result = stmt.executeQuery()) {
//        if (!result.next()) {
//          String msg =
//              String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
//          throw new UserAbortException(msg);
//        }
//        if (System.currentTimeMillis() % 2 == 0)
//          balance = result.getDouble(1) - amount;
//        else
//          balance = result.getDouble(1) + amount;
//      }
//    }
//
//    // Make sure that they have enough
//    if (balance < 0) {
//      String msg =
//          String.format(
//              "Negative %s balance for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
//      throw new UserAbortException(msg);
//    }

    // Then update their savings balance
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid, LockType.EX);
    }
    if (type == CCType.RC_TAILOR) {
      // flow rate control
      while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_SAVINGS, custId)) {
        // System.out.println("TS 127 custId: " + custId);
      }
      // System.out.println("TransactSavings transaction #" + tid + " acquire savings #" + custId);
      checkout[0]++;
    }
    if (type == CCType.SI_TAILOR) {
      if (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_SAVINGS, custId, true)) {
        String msg = String.format("concurrent update, TS", custId);
        throw new SQLException(msg, "500");
      } else {
        checkout[0]++;
      }
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
      if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId, false);
        checkout[0]--;
      }
      throw ex;
    }

    if (type == CCType.RC_TAILOR && versions[0] < 0)
      System.out.println("custome error 5");

    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      LOG.debug("TransactSavings #" + tid + " acquire EX validation lock - checking #"+custId);
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId, LockType.EX, type);
      LOG.debug("TransactSavings #" + tid + " acquired EX validation lock - checking #"+custId);
    }
  }

  public void doAfterCommit(long custId, CCType type, boolean success, long[] versions, long tid, int[] checkout) {
    if (!success) {
      if ((type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) && versions[0] >= 0) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId, true);
        checkout[0]--;
      }
      return;
    }
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid);
    }
    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.EX);
      LOG.debug("TransactSavings #" + tid + " release EX validation lock - savings #"+custId);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId, versions[0]);
      FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId, true);
      checkout[0]--;
      if (checkout[0] != 0)
        System.out.println("TS Transaction #" + tid + " checkout: " + checkout[0]);
    }
  }
}
