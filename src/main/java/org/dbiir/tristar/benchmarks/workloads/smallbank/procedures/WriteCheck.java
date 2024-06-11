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

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.locks.Lock;

/**
 * WriteCheck Procedure Original version by Mohammad Alomari and Michael Cahill
 *
 * @author pavlo
 */
public class WriteCheck extends Procedure {
  public final SQLStmt writeConflict =
          new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_CONFLICT + " WHERE name = ? FOR UPDATE");
  public final SQLStmt GetAccount =
      new SQLStmt("SELECT * FROM " + SmallBankConstants.TABLENAME_ACCOUNTS + " WHERE name = ?");

  public final SQLStmt GetSavingsBalance =
      new SQLStmt("SELECT bal, tid FROM " + SmallBankConstants.TABLENAME_SAVINGS + " WHERE custid = ?");

  /*
  public final SQLStmt GetSavingsBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " AS new SET bal = old.bal FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid=new.custid RETURNING old.bal");
  */
  public final SQLStmt GetSavingsBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_SAVINGS +
                  " SET bal = bal " +
                  " WHERE custid = ?;" +
                  " SELECT bal " +
                  " FROM " +
                  SmallBankConstants.TABLENAME_SAVINGS +
                  " WHERE custid = ?;");

  public final SQLStmt GetCheckingBalance =
      new SQLStmt("SELECT bal, tid FROM " + SmallBankConstants.TABLENAME_CHECKING + " WHERE custid = ?");

  public final SQLStmt GetCheckingBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " SET bal = bal " +
                  " WHERE custid = ?;" +
                  " SELECT bal" +
                  " FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " WHERE custid = ?;");

  /*
  public final SQLStmt GetCheckingBalanceForUpdate =
          new SQLStmt("UPDATE " + SmallBankConstants.TABLENAME_CHECKING +
                  " AS new SET bal = old.bal FROM " +
                  SmallBankConstants.TABLENAME_CHECKING +
                  " AS old WHERE new.custid = ? " +
                  " AND old.custid=new.custid RETURNING old.bal");
  */

  public final SQLStmt UpdateCheckingBalanceDel =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal - ?, tid = tid + 1"
              + " WHERE custid = ?;"
              + "SELECT tid + 1"
              +  " FROM "
              + SmallBankConstants.TABLENAME_CHECKING
              + " WHERE custid = ?;");

  public void run(Connection conn, String custName, long custId1, double amount, CCType type, Connection conn2, long[] versions, long tid) throws SQLException {
    // First convert the custName to the custId
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

    try (PreparedStatement stmt0 = this.getPreparedStatement(conn, GetAccount, custName)) {
      try (ResultSet r0 = stmt0.executeQuery()) {
        if (!r0.next()) {
          String msg = "Invalid account '" + custName + "'";
          throw new UserAbortException(msg);
        }
        custId = r0.getLong(1);
      }
    }

    // Then get their account balances
    double savingsBalance = Double.NaN;
    int phase = 0; // the phase of RC_TAILOR_LOCK
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid, LockType.SH);
      phase = 1;
    }
    try (PreparedStatement balStmt0 = switch (type) {
      case RC_FOR_UPDATE, SI_FOR_UPDATE -> this.getPreparedStatement(conn, GetSavingsBalanceForUpdate, custId1, custId1);
        default -> this.getPreparedStatement(conn, GetSavingsBalance, custId1);
    }) {

      boolean resultsAvailable1 = balStmt0.execute();
      while (true) {
        if (resultsAvailable1) {
          ResultSet rs = balStmt0.getResultSet();
          if (!rs.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId1);
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId, custId1, tid);
            throw new UserAbortException(msg);
          }

          savingsBalance = rs.getDouble(1);
          if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR)
            versions[0] = rs.getLong(2);
        } else if (balStmt0.getUpdateCount() < 0) {
          break;
        }

        resultsAvailable1 = balStmt0.getMoreResults();
      }
      /*
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId1);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId, custId1, tid);
          throw new UserAbortException(msg);
        }

        savingsBalance = balRes0.getDouble(1);
        if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR)
          versions[0] = balRes0.getLong(2);
      }
       */
    }

    double checkingBalance = Double.NaN;
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid, LockType.EX);
    }
    phase = 2;
    try (PreparedStatement balStmt1 = switch (type) {
      case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetCheckingBalanceForUpdate, custId, custId);
      default -> this.getPreparedStatement(conn, GetCheckingBalance, custId);
    }) {

      boolean resultsAvailable0 = balStmt1.execute();
      while (true) {
        if (resultsAvailable0) {
          ResultSet rs = balStmt1.getResultSet();
          if (!rs.next()) {
            String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId, custId1, tid);
            throw new UserAbortException(msg);
          }
          savingsBalance = rs.getDouble(1);
          if (type == CCType.RC_TAILOR) {
            versions[1] = rs.getLong(2);
          }
        } else if (balStmt1.getUpdateCount() < 0) {
          break;
        }

        resultsAvailable0 = balStmt1.getMoreResults();
      }
      /*
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId, custId1, tid);
          throw new UserAbortException(msg);
        }
        checkingBalance = balRes1.getDouble(1);
        if (type == CCType.RC_TAILOR)
          versions[1] = balRes1.getLong(2);
      }

       */
    }

    double total = checkingBalance + savingsBalance;

    if (total < amount) {
      try (PreparedStatement updateStmt1 =
                   this.getPreparedStatement(conn, UpdateCheckingBalanceDel, amount - 1, custId, custId)) {
        boolean resultsAvailable = updateStmt1.execute();
        while (true) {
          if (resultsAvailable) {
            ResultSet rs = updateStmt1.getResultSet();
            if (!rs.next()) {
              if (type == CCType.RC_TAILOR_LOCK)
                releaseTailorLock(phase, custId, custId1, tid);
              throw new UserAbortException("unknown exception");
            }
            versions[2] = rs.getLong(1);
          } else if (updateStmt1.getUpdateCount() < 0) {
            break;
          }

          resultsAvailable = updateStmt1.getMoreResults();
        }
      }
    } else {
      try (PreparedStatement updateStmt2 =
                   this.getPreparedStatement(conn, UpdateCheckingBalanceDel, amount, custId, custId)) {
        boolean resultsAvailable = updateStmt2.execute();
        while (true) {
          if (resultsAvailable) {
            ResultSet rs = updateStmt2.getResultSet();
            if (!rs.next()) {
              if (type == CCType.RC_TAILOR_LOCK)
                releaseTailorLock(phase, custId, custId1, tid);
              throw new UserAbortException("unknown exception");
            }
            versions[2] = rs.getLong(1);
          } else if (updateStmt2.getUpdateCount() < 0) {
            break;
          }

          resultsAvailable = updateStmt2.getMoreResults();
        }
      }
    }
    if (type == CCType.RC_TAILOR && versions[2] != (versions[1] + 1)) {
      String msg = String.format("Validation failed for customer #%d, checking, WriteCheck", custId);
      throw new SQLException(msg, "500");
    }

    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      // first validation lock
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId1, LockType.SH, type);
      long v = LockTable.getInstance().getHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId1);
      if (v >= 0) {
        // hotspot, no need to fetch from underlying database
        if (v != versions[0]) {
//          System.out.println(custId + ": " + v + " <- " + versions[0]);
          String msg = String.format("Validation failed for customer #%d, savings, WriteCheck", custId1);
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
          throw new SQLException(msg, "500");
        }
      } else {
        try (PreparedStatement balStmt = this.getPreparedStatement(conn2, GetSavingsBalance, custId1)) {
          try (ResultSet balRes = balStmt.executeQuery()) {
            if (!balRes.next()) {
              String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId1);
              LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
              throw new UserAbortException(msg);
            }
            v = balRes.getLong(2);
            LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId1, v);
            if (v != versions[0]) {
//              System.out.println("custid: " + custId + v + " <- " + versions[0]);
              String msg = String.format("Validation failed for customer #%d, savings, WriteCheck", custId1);
              LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
              throw new SQLException(msg, "500");
            }
          }
        }
      }

      if (type == CCType.RC_TAILOR) {
        // second validation lock
        try {
          LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId, LockType.EX, type);
        } catch (SQLException ex) {
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
          throw ex;
        }
      }
    }
  }

  private void releaseTailorLock(int phase, long custId0, long custId1, long tid) {
    if (phase == 1) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid);
    } else if (phase == 2) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid);
    }
  }

  public void doAfterCommit(long custId0, long custId1, CCType type, boolean success, long[] versions, long tid) {
    /*
     * there still some optimization space in write-after-read, it may be better that change the rw-dependency
     * into ww-dependency
     */
    if (!success)
      return;

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid);
    }

    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId0, versions[2]);
    }
    if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR)
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
  }
}
