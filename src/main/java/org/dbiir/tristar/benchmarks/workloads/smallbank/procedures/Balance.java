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

public class Balance extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE name = ? FOR UPDATE");

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " AS new SET bal = old.bal FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid=new.custid RETURNING old.bal");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " AS new SET bal = old.bal FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid=new.custid RETURNING old.bal");

  public double run(Connection conn, String custName, CCType type) throws SQLException {
    // First convert the acctName to the acctId
    long tid = (System.nanoTime() << 10) | (Thread.currentThread().getId() & 0x3ff);
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

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, String.valueOf(custId), tid, LockType.SH);
    }

    // Then get their account balances
    double savingsBalance;
    try (
      PreparedStatement balStmt0 = switch (type) {
        case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetSavingsBalanceForUpdate, custId);
        default -> this.getPreparedStatement(conn, GetSavingsBalance, custId);
      }) {
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg =
              String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
          System.out.println("UserAbortException: " + msg);
          throw new UserAbortException(msg);
        }
        savingsBalance = balRes0.getDouble(1);
      }
    }

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, String.valueOf(custId), tid, LockType.SH);
    }
    double checkingBalance;
    try (PreparedStatement balStmt1 = switch (type) {
      case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetCheckingBalanceForUpdate, custId);
      default -> this.getPreparedStatement(conn, GetCheckingBalance, custId);
    }) {
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg =
              String.format(
                  "No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
          System.out.println("UserAbortException: " + msg);
          throw new UserAbortException(msg);
        }

        checkingBalance = balRes1.getDouble(1);
      }
    }

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, String.valueOf(custId), tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, String.valueOf(custId), tid);
    }

    if (type == CCType.RC_TAILOR) {
      try (PreparedStatement balStmt0 = this.getPreparedStatement(conn, GetSavingsBalance, custId)) {
        try (ResultSet balRes0 = balStmt0.executeQuery()) {
          if (!balRes0.next()) {
            String msg =
                    String.format(
                            "No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
            throw new UserAbortException(msg);
          }
          double savingsNow = balRes0.getDouble(1);
          if (Math.abs(savingsBalance - savingsNow) > 1e-5) {
            String msg = String.format("Validation failed for customer #%d, savings, Balance", custId);
            throw new SQLException(msg);
          }
        }
      }

      try (PreparedStatement balStmt1 = this.getPreparedStatement(conn, GetCheckingBalance, custId)) {
        try (ResultSet balRes1 = balStmt1.executeQuery()) {
          if (!balRes1.next()) {
            String msg =
                    String.format(
                            "No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
            throw new UserAbortException(msg);
          }
          double checkingNow = balRes1.getDouble(1);
          if (Math.abs(checkingBalance  - checkingNow) > 1e-5) {
            String msg = String.format("Validation failed for customer #%d, checking, Balance", custId);
            throw new SQLException(msg);
          }
        }
      }
    }

    return checkingBalance + savingsBalance;
  }
}
