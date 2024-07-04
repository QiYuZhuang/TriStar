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
import java.util.concurrent.locks.Lock;

/**
 * Amalgamate Procedure Original version by Mohammad Alomari and Michael Cahill
 *
 * @author pavlo
 */
public class Amalgamate extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CONFLICT + " SET name = name" + " WHERE custid = ?");
  /*
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE custid = ? FOR UPDATE");
  */
  // 2013-05-05
  // In the original version of the benchmark, this is suppose to be a look up
  // on the customer's name. We don't have fast implementation of replicated
  // secondary indexes, so we'll just ignore that part for now.
  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal, tid + 1 FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal, tid + 1 FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

    public final SQLStmt UpdateSavingsBalance =
          new SQLStmt(
                  "UPDATE "
                          + SmallBankConstants.TABLENAME_SAVINGS
                          + " SET bal = bal + ?, tid = tid + 1 "
                          + " WHERE custid = ?;"
                          + "SELECT"
                          + " tid"
                          + " FROM "
                          + SmallBankConstants.TABLENAME_SAVINGS
                          + " WHERE custid = ?;");
  /*
  public final SQLStmt UpdateSavingsBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_SAVINGS
              + "   SET bal = bal - ?, tid = tid + 1 "
              + " WHERE custid = ?"
      + " RETURNING tid");
  */
  public final SQLStmt UpdateCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal + ? "
              + " WHERE custid = ?");

  public final SQLStmt GetAndZeroCheckingBalance =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " AS new SET bal = 0.0, tid = old.tid + 1 FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid = new.custid " +
                  "RETURNING old.bal, new.tid");

  public final SQLStmt GetAndZeroSavingsBalance =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " AS new SET bal = 0.0, tid = old.tid + 1 FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid = new.custid " +
                  "RETURNING old.bal, new.tid");

  public final SQLStmt ZeroCheckingBalance =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = 0.0, tid = tid + 1"
              + " WHERE custid = ?");

  public final SQLStmt ZeroSavingsBalance =
          new SQLStmt(
                  "UPDATE "
                          + SmallBankConstants.TABLENAME_SAVINGS
                          + "   SET bal = 0.0, tid = tid + 1 "
                          + " WHERE custid = ?");

  public void run(Connection conn, long custId0, long custId1, CCType type, long[] versions, long tid, Connection conn2) throws SQLException {
    if (type == CCType.RC_ELT) {
      try (PreparedStatement stmtc0 = this.getPreparedStatement(conn, writeConflict, custId0)) {
      int rs0 = stmtc0.executeUpdate();
      if (rs0 == 0) {
        String msg = "Invalid account '" + custId0 + "'";
        throw new UserAbortException(msg);
      }
/*        try (ResultSet r0 = stmtc0.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custId0 + "'";
            throw new UserAbortException(msg);
          }
        }*/
      }
      try (PreparedStatement stmtc1 = this.getPreparedStatement(conn, writeConflict, custId1)) {
        int rs1 = stmtc1.executeUpdate();
        if (rs1 == 0) {
          String msg = "Invalid account '" + custId1 + "'";
          throw new UserAbortException(msg);
        }
/*        try (ResultSet r0 = stmtc1.executeQuery()) {
          if (!r0.next()) {
            String msg = "Invalid account '" + custId0 + "'";
            throw new UserAbortException(msg);
          }
        }*/
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
    int phase = 0;
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid, LockType.EX);
      phase = 1;
    }
    double savingsBalance;
    try (PreparedStatement balStmt0 = this.getPreparedStatement(conn, ZeroSavingsBalance, custId0);
         PreparedStatement balStmt1 = this.getPreparedStatement(conn2, GetSavingsBalance, custId0)){
      //get savingsBalance
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId0);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId0, custId1, tid);
          throw new UserAbortException(msg);
        }
        savingsBalance = balRes1.getDouble(1);
        versions[0] = balRes1.getLong(2);
      }

      int balRes0 = balStmt0.executeUpdate();
      if (balRes0 == 0) {
        String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId0);
        if (type == CCType.RC_TAILOR_LOCK)
          releaseTailorLock(phase, custId0, custId1, tid);
        throw new UserAbortException(msg);
      }

    }

    if (type == CCType.RC_TAILOR_LOCK) {
      try {
        LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid, LockType.EX);
        phase = 2;
      } catch (SQLException ex) {
        releaseTailorLock(phase, custId0, custId1, tid);
        throw ex;
      }
    }

    double checkingBalance;
    try (PreparedStatement balStmt2 = this.getPreparedStatement(conn, ZeroCheckingBalance, custId0);
         PreparedStatement balStmt3 = this.getPreparedStatement(conn2, GetCheckingBalance, custId0)) {
      //get Checkingbalance
      try (ResultSet balRes3 = balStmt3.executeQuery()) {
        if (!balRes3.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId0);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId0, custId1, tid);
          throw new UserAbortException(msg);
        }

        checkingBalance = balRes3.getDouble(1);
        versions[1] = balRes3.getLong(2);
      }
      // zero Checkingbalance
      int balRes2 = balStmt2.executeUpdate();
      if (balRes2 == 0) {
        String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId0);
        if (type == CCType.RC_TAILOR_LOCK)
          releaseTailorLock(phase, custId0, custId1, tid);
        throw new UserAbortException(msg);
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
    if (type == CCType.RC_TAILOR_LOCK) {
      try {
        LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid, LockType.EX);
        phase = 3;
      } catch (SQLException ex) {
        releaseTailorLock(phase, custId0, custId1, tid);
        throw ex;
      }

    }
    try (PreparedStatement updateStmt1 =
        this.getPreparedStatement(conn, UpdateSavingsBalance, total, custId1, custId1)) {

      boolean resultsAvailable = updateStmt1.execute();
      while (true) {
        if (resultsAvailable) {
          ResultSet rs = updateStmt1.getResultSet();
          if (!rs.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId1);
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId0, custId1, tid);
            throw new UserAbortException(msg);
          }
          versions[2] = rs.getLong(1);
        } else if (updateStmt1.getUpdateCount() < 0) {
          break;
        }
        resultsAvailable = updateStmt1.getMoreResults();
      }

      /*
      try (ResultSet res = updateStmt1.executeQuery()) {
        if (!res.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId1);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId0, custId1, tid);
          throw new UserAbortException(msg);
        }

        versions[2] = res.getLong(1);
      }*/
    }

    if (type == CCType.RC_TAILOR) {
      int validationPhase = 0;
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId0, LockType.EX, type);
      validationPhase = 1;
      try {
        LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId0, LockType.EX, type);
        validationPhase = 2;
      } catch (SQLException ex) {
        releaseTailorValidationLock(validationPhase, custId0, custId1);
        throw ex;
      }
      try {
        LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId1, LockType.EX, type);
        validationPhase = 3;
      } catch (SQLException ex) {
        releaseTailorValidationLock(validationPhase, custId0, custId1);
        throw ex;
      }
    }
  }

  private void releaseTailorLock(int phase, long custId0, long custId1, long tid) {
    if (phase == 1) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid);
    } else if (phase == 2) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid);
    } else if (phase == 3) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId1, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid);
    }
  }

  private void releaseTailorValidationLock(int phase, long custId0, long custId1) {
    if (phase == 1) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
    } else if (phase == 2) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
    } else if (phase == 3) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId1, LockType.EX);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
    }
  }

  public void doAfterCommit(long custId0, long custId1, CCType type, boolean success, long[] versions, long tid) {
    if (!success)
      return;

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid);
    }
    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId1, LockType.EX);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
      // update the
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId0, versions[0]);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId0, versions[1]);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId1, versions[2]);
    }
  }
}
