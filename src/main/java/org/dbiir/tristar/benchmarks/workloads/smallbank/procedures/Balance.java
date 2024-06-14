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

public class Balance extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE name = ? FOR UPDATE");

  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal, tid FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  public final SQLStmt GetSavingsBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " SET bal = bal + 0.0"+
                  " WHERE custid = ?;" +
                  " SELECT bal " +
                  " FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " WHERE custid = ?;");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal, tid FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " SET bal = bal + 0.0" +
                  " WHERE custid = ?;" +
                  " SELECT bal" +
                  " FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " WHERE custid = ?;");

  public double run(Connection conn, String custName, CCType type, long[] versions, long tid) throws SQLException {
    // First convert the acctName to the acctId
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
    int phase = 0;
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid, LockType.SH);
      phase = 1;
    }

    // Then get their account balances
    double savingsBalance = Double.NaN;
    try (
      PreparedStatement balStmt0 = switch (type) {
        case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetSavingsBalanceForUpdate, custId, custId);
        default -> this.getPreparedStatement(conn, GetSavingsBalance, custId);
      }) {

      boolean resultsAvailable0 = balStmt0.execute();
      while (true) {
        if (resultsAvailable0) {
          ResultSet rs = balStmt0.getResultSet();
          if (!rs.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId, tid);
            throw new UserAbortException(msg);
          }
          savingsBalance = rs.getDouble(1);
          if (type == CCType.RC_TAILOR) {
            versions[0] = rs.getLong(2);
          }
        } else if (balStmt0.getUpdateCount() < 0) {
          break;
        }

        resultsAvailable0 = balStmt0.getMoreResults();
      }
      /*
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId, tid);
          throw new UserAbortException(msg);
        }
        savingsBalance = balRes0.getDouble(1);
        if (type == CCType.RC_TAILOR)
          versions[0] = balRes0.getLong(2);
      }*/
    }

    if (type == CCType.RC_TAILOR_LOCK) {
      try {
        LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid, LockType.SH);
        phase = 2;
      } catch (SQLException ex) {
        releaseTailorLock(phase, custId, tid);
        throw ex;
      }
    }

    double checkingBalance = Double.NaN;
    try (PreparedStatement balStmt1 = switch (type) {
      case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetCheckingBalanceForUpdate, custId, custId);
      default -> this.getPreparedStatement(conn, GetCheckingBalance, custId);
    }) {

      boolean resultsAvailable1 = balStmt1.execute();
      while (true) {
        if (resultsAvailable1) {
          ResultSet rs = balStmt1.getResultSet();
          if (!rs.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId, tid);
            throw new UserAbortException(msg);
          }
          checkingBalance = rs.getDouble(1);
          if (type == CCType.RC_TAILOR) {
            versions[1] = rs.getLong(2);
          }
        } else if (balStmt1.getUpdateCount() < 0) {
          break;
        }

        resultsAvailable1 = balStmt1.getMoreResults();
      }
      /*
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
          if (type == CCType.RC_TAILOR_LOCK) {
            releaseTailorLock(phase, custId, tid);
          }
          throw new UserAbortException(msg);
        }

        checkingBalance = balRes1.getDouble(1);
        if (type == CCType.RC_TAILOR)
          versions[1] = balRes1.getLong(2);
      }*/
    }

    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId, LockType.SH, type);
      int validationPhase = 1;
      try {
        LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId, LockType.SH, type);
        validationPhase = 2;
      } catch (SQLException ex) {
        releaseTailorValidationLock(validationPhase, custId);
        throw ex;
      }

      long v = LockTable.getInstance().getHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId);
      if (v >= 0) {
        if (v != versions[0]) {
          String msg = String.format("Validation failed for customer #%d, savings, Balance", custId);
          releaseTailorValidationLock(validationPhase, custId);
          throw new SQLException(msg, "500");
        }
      } else {
        try (PreparedStatement balStmt0 = this.getPreparedStatement(conn, GetSavingsBalance, custId)) {
          try (ResultSet balRes0 = balStmt0.executeQuery()) {
            if (!balRes0.next()) {
              releaseTailorValidationLock(validationPhase, custId);
              String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId);
              throw new UserAbortException(msg);
            }
            v = balRes0.getLong(2);
            LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId, v);
            if (v != versions[0]) {
              releaseTailorValidationLock(validationPhase, custId);
              String msg = String.format("Validation failed for customer #%d, savings, Balance", custId);
              throw new SQLException(msg, "500");
            }
          }
        }
      }

      v = LockTable.getInstance().getHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId);
      if (v >= 0) {
       if (v != versions[1]) {
         releaseTailorValidationLock(validationPhase, custId);
         String msg = String.format("Validation failed for customer #%d, checking, Balance", custId);
         throw new SQLException(msg, "500");
       }
      } else {
        try (PreparedStatement balStmt1 = this.getPreparedStatement(conn, GetCheckingBalance, custId)) {
          try (ResultSet balRes1 = balStmt1.executeQuery()) {
            if (!balRes1.next()) {
              releaseTailorValidationLock(validationPhase, custId);
              String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
              throw new UserAbortException(msg);
            }
            v = balRes1.getLong(2);
            LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId, v);
            if (v != versions[1]) {
              releaseTailorValidationLock(validationPhase, custId);
              String msg = String.format("Validation failed for customer #%d, checking, Balance", custId);
              throw new SQLException(msg, "500");
            }
          }
        }
      }
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

  private void releaseTailorValidationLock(int phase, long custId) {
    if (phase == 1) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.SH);
    } else if (phase == 2) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId, LockType.SH);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.SH);
    }
  }

  public void doAfterCommit(long custId, CCType type, boolean success, long[] versions, long tid) {
    if (!success)
      return;

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId, tid);
    }
    if (type == CCType.RC_TAILOR) {
      // release validation lock on savings and checking
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId, LockType.SH);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId, LockType.SH);
    }
  }
}
