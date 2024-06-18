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

  /*  */
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

  public final SQLStmt UpdateCheckingBalanceDel =
      new SQLStmt(
          "UPDATE "
              + SmallBankConstants.TABLENAME_CHECKING
              + "   SET bal = bal - ?, tid = tid + 1"
              + " WHERE custid = ?"
              + " RETURNING tid");

  public void run(Connection conn, String custName, long custId1, double amount, CCType type, Connection conn2, long[] versions, long tid, int[] checkout) throws SQLException {
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
    double savingsBalance;
    int phase = 0; // the phase of RC_TAILOR_LOCK
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid, LockType.SH);
      phase = 1;
    }
    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      if (!FlowRate.getInstance().readOperationAdmission(SmallBankConstants.TABLENAME_SAVINGS, custId)) {
        String msg = String.format("Read too much update for customer #%d, checking, WriteCheck", custId);
        throw new SQLException(msg, "500");
      }
    }
    try (PreparedStatement balStmt0 = switch (type) {
      case RC_FOR_UPDATE, SI_FOR_UPDATE -> this.getPreparedStatement(conn, GetSavingsBalanceForUpdate, custId1);
      default -> this.getPreparedStatement(conn, GetSavingsBalance, custId1);
    }) {
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
    }

    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      LOG.debug("WriteCheck #" + tid + " acquire validation lock on savings #"+custId1);
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId1, LockType.SH, type);
      LOG.debug("WriteCheck #" + tid + " acquired validation lock on savings #"+custId1);
      long v = LockTable.getInstance().getHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId1);
      if (v >= 0) {
        // hotspot, no need to fetch from underlying database
        if (v != versions[0]) {
          // System.out.println(custId + ": " + v + " <- " + versions[0]);
          String msg = String.format("Validation failed for customer #%d, savings, WriteCheck", custId1);
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
          throw new SQLException(msg, "500");
        }
      }
    }
    // try {
    //   Thread.sleep(5);
    // } catch (InterruptedException e) {
    // }

    double checkingBalance;
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, custId, tid, LockType.EX);
    }
    if (type == CCType.RC_TAILOR) {
      // flow rate control
      int count = 0;
      while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId)) {
        System.out.println("WC 140 custId: " + custId);
        count++;
        if (count > 10) {
          String msg = String.format("Too much concurrent update for customer #%d, checking, WriteCheck", custId);
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
          throw new SQLException(msg, "500");
        }
      }
      checkout[0]++;
    }
    phase = 2;
    try (PreparedStatement balStmt1 = switch (type) {
      case RC_FOR_UPDATE -> this.getPreparedStatement(conn, GetCheckingBalanceForUpdate, custId);
      default -> this.getPreparedStatement(conn, GetCheckingBalance, custId);
    }) {
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
    } catch (SQLException ex) {
      if (type == CCType.RC_TAILOR) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId, false);
        checkout[0]--;
      }
      if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR)
        LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
      throw ex;
    }

    double total = checkingBalance + savingsBalance;

    if (total < amount) {
      try (PreparedStatement updateStmt =
          this.getPreparedStatement(conn, UpdateCheckingBalanceDel, amount - 1, custId)) {
        try (ResultSet res = updateStmt.executeQuery()) {
          if (!res.next()) {
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId, custId1, tid);
            throw new UserAbortException("unknown exception");
          }
          versions[2] = res.getLong(1);
        }
      } catch (SQLException ex) {
        if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR)
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
        throw ex;
      }
    } else {
      try (PreparedStatement updateStmt =
          this.getPreparedStatement(conn, UpdateCheckingBalanceDel, amount, custId)) {
        try (ResultSet res = updateStmt.executeQuery()) {
          if (!res.next()) {
            if (type == CCType.RC_TAILOR_LOCK)
              releaseTailorLock(phase, custId, custId1, tid);
            throw new UserAbortException("unknown exception");
          }
          versions[2] = res.getLong(1);
        }
      } catch (SQLException ex) {
        if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR)
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
        throw ex;
      }
    }
    if (type == CCType.RC_TAILOR && versions[2] != (versions[1] + 1)) {
      String msg = String.format("Transaction #%d Validation failed for customer #%d, checking, WriteCheck", tid, custId);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
      throw new SQLException(msg, "500");
    }

    LOG.debug("WriteCheck #" + tid + " enter validation");
    if (type == CCType.SI_TAILOR || type == CCType.RC_TAILOR) {
      // first validation lock
      long v = LockTable.getInstance().getHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId1);
      if (v >= 0) {
        // hotspot, no need to fetch from underlying database
        if (v != versions[0]) {
          // System.out.println(custId + ": " + v + " <- " + versions[0]);
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
          LOG.debug("WriteCheck #" + tid + " acquire validation lock on checking #" + custId);
          LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId, LockType.EX, type);
          LOG.debug("WriteCheck #" + tid + " acquired validation lock on checking #" + custId);
        } catch (SQLException ex) {
          LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
          LOG.debug("WriteCheck #" + tid + " release validation lock on savings #" + custId1);
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

  public void doAfterCommit(long custId0, long custId1, CCType type, boolean success, long[] versions, long tid, int[] checkout) {
    /*
     * there still some optimization space in write-after-read, it may be better that change the rw-dependency
     * into ww-dependency
     */
    if (!success) {
      if (type == CCType.RC_TAILOR && versions[1] >= 0) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId0, true);
        checkout[0]--;
      }
      return;
    }

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, tid);
    }

    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LOG.debug("WriteCheck #" + tid + " release validation lock on checking #" + custId0);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId0, versions[2]);
      FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId0, true);
      checkout[0]--;
      if (checkout[0] != 0)
        System.out.println("WriteCheck Transaction #" + tid + " checkout: " + checkout[0]);
    }
    if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId1, LockType.SH);
      LOG.debug("WriteCheck #" + tid + " release validation lock on savings #" + custId1);
    }
  }
}
