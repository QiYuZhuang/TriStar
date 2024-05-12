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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Amalgamate Procedure Original version by Mohammad Alomari and Michael Cahill
 *
 * @author pavlo
 */
public class Amalgamate extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE custid = ? FOR UPDATE");

  // 2013-05-05
  // In the original version of the benchmark, this is suppose to be a look up
  // on the customer's name. We don't have fast implementation of replicated
  // secondary indexes, so we'll just ignore that part for now.
  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt UpdateSavingsBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_SAVINGS
              + "   SET bal = bal - ? "
              + " WHERE custid = ?");

  public final SQLStmt UpdateCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal + ? "
              + " WHERE custid = ?");

  public final SQLStmt GetAndZeroCheckingBalance =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " AS new SET bal = 0.0 FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid = new.custid " +
                  "RETURNING old.bal");

  public final SQLStmt GetAndZeroSavingsBalance =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " AS new SET bal = 0.0 FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid = new.custid " +
                  "RETURNING old.bal");

  public final SQLStmt ZeroCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = 0.0 "
              + " WHERE custid = ?");

  public final SQLStmt ZeroSavingsBalance =
          new SQLStmt(
                  "UPDATE "
                          + SmallBankConstants.TABLENAME_SAVINGS
                          + "   SET bal = 0.0 "
                          + " WHERE custid = ?");

  public void run(Connection conn, long custId0, long custId1, CCType type) throws SQLException {
    if (type == CCType.RC_ELT) {
      try (PreparedStatement stmtc0 = this.getPreparedStatement(conn, writeConflict, custId0)) {
        try (ResultSet r0 = stmtc0.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custId0 + "'";
            throw new UserAbortException(msg);
          }
        }
      }
      try (PreparedStatement stmtc1 = this.getPreparedStatement(conn, writeConflict, custId1)) {
        try (ResultSet r0 = stmtc1.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custId0 + "'";
            throw new UserAbortException(msg);
          }
        }
      }
    }

    // Get Account Information
    try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, custId0)) {
      try (ResultSet r0 = stmt0.executeQuery()) {
        if (!r0.next()) {
          String msg = "Invalid account '" + custId0 + "'";
          throw new UserAbortException(msg);
        }
      }
    }

    try (PreparedStatement stmt1 = this.getPreparedStatement(conn, GetAccount, custId1)) {
      try (ResultSet r1 = stmt1.executeQuery()) {
        if (!r1.next()) {
          String msg = "Invalid account '" + custId1 + "'";
          throw new UserAbortException(msg);
        }
      }
    }

    // Get Balance Information
    double savingsBalance;
    try (PreparedStatement balStmt0 =
                 this.getPreparedStatement(conn, GetAndZeroSavingsBalance, custId0)) {
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg =
              String.format(
                  "No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId0);
          throw new UserAbortException(msg);
        }
        savingsBalance = balRes0.getDouble(1);
      }
    }

    double checkingBalance;
    try (PreparedStatement balStmt1 =
        this.getPreparedStatement(conn, GetAndZeroCheckingBalance, custId0)) {
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg =
              String.format(
                  "No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId0);
          throw new UserAbortException(msg);
        }

        checkingBalance = balRes1.getDouble(1);
      }
    }

    double total = checkingBalance + savingsBalance;
    // assert(total >= 0);

    // Update Balance Information
//    try (PreparedStatement updateStmt0 =
//        this.getPreparedStatement(conn, ZeroCheckingBalance, custId0)) {
//      updateStmt0.executeUpdate();
//    }
//
//    try (PreparedStatement updateStmt0 =
//                 this.getPreparedStatement(conn, ZeroSavingsBalance, custId0)) {
//      updateStmt0.executeUpdate();
//    }

    try (PreparedStatement updateStmt1 =
        this.getPreparedStatement(conn, UpdateSavingsBalance, total, custId1)) {
      updateStmt1.executeUpdate();
    }
  }
}
