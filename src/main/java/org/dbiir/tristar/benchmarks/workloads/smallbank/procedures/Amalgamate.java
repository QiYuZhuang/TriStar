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
              + "   SET bal = bal - ?, tid = tid + 1 "
              + " WHERE custid = ?"
      + " RETURNING tid");

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
              + "   SET bal = 0.0 "
              + " WHERE custid = ?");

  public final SQLStmt ZeroSavingsBalance =
          new SQLStmt(
                  "UPDATE "
                          + SmallBankConstants.TABLENAME_SAVINGS
                          + "   SET bal = 0.0 "
                          + " WHERE custid = ?");

  public void run(Connection conn, long custId0, long custId1, CCType type, long[] versions, long tid, int[] checkout) throws SQLException {
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
    int phase = 0;
    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid, LockType.EX);
      phase = 1;
    }
    if (type == CCType.RC_TAILOR) {
      while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_SAVINGS, custId0)) {
        
      }
      // System.out.println("Amalgamate transaction #" + tid + " acquire savings #" + custId0);
      checkout[0] ++;
    }
    if (type == CCType.SI_TAILOR) {
      if (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_SAVINGS, custId0, true)) {
        String msg = String.format("concurrent update, Amg", custId0);
        throw new SQLException(msg, "500");
      }
      // System.out.println("Amalgamate transaction #" + tid + " acquire savings #" + custId0);
      checkout[0] ++;
    }
    double savingsBalance;
    try (PreparedStatement balStmt0 = this.getPreparedStatement(conn, GetAndZeroSavingsBalance, custId0)) {
      try (ResultSet balRes0 = balStmt0.executeQuery()) {
        if (!balRes0.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_SAVINGS, custId0);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId0, custId1, tid);
          throw new UserAbortException(msg);
        }
        savingsBalance = balRes0.getDouble(1);
        versions[0] = balRes0.getLong(2);
      } 
    } catch (SQLException ex) {
      if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId0, false);
        checkout[0]--;
      }
      throw ex;
    }

    if (type == CCType.RC_TAILOR && versions[0] < 0)
      System.out.println("custome error 1");
    if (type == CCType.RC_TAILOR_LOCK) {
      try {
        LockTable.getInstance().tryLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid, LockType.EX);
        phase = 2;
      } catch (SQLException ex) {
        releaseTailorLock(phase, custId0, custId1, tid);
        throw ex;
      }
    }
    if (type == CCType.RC_TAILOR) {
      int count = 0;
      if (custId0 < custId1) {
        while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId0)) {
          System.out.println("Amg 189 custId0: " + custId0);
        }
        checkout[0]++;
        while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId1)) {
          System.out.println("Amg 192 custId1: " + custId1 + " - locked savings-" + custId0 + ", checking-" + custId0);
        }
        checkout[0]++;
      } else {
        if (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId1, true)) {
          String msg = String.format("Too much concurrent update for customer #%d, checking, Amalgamate", custId1);
          throw new SQLException(msg, "500");
        }
        checkout[0]++;
        if (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId0, true)) {
          FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId1);
          checkout[0]--;
          String msg = String.format("Too much concurrent update for customer #%d, checking, Amalgamate", custId0);
          throw new SQLException(msg, "500");
        }
        // while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId1)) {
        //   System.out.println("Amg 196 custId1: " + custId1);
        //   count++;
        //   if (count > 10) {
        //     String msg = String.format("Too much concurrent update for customer #%d, checking, Amalgamate", custId1);
        //     throw new SQLException(msg, "500");
        //   }
        // }
        checkout[0]++;
        // count = 0;
        // while (!FlowRate.getInstance().writeOperationAdmission(SmallBankConstants.TABLENAME_CHECKING, custId0)) {
        //   System.out.println("Amg 199 custId0: " + custId0 + " - locked savings-" + custId0 + ", checking-" + custId1);
        //   count++;
        //   if (count > 10) {
        //     String msg = String.format("Too much concurrent update for customer #%d, checking, Amalgamate", custId0);
        //     FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId1);
        //     checkout[0]--;
        //     throw new SQLException(msg, "500");
        //   }
        // }
        // checkout[0]++;
      }
    }
    double checkingBalance;
    try (PreparedStatement balStmt1 = this.getPreparedStatement(conn, GetAndZeroCheckingBalance, custId0)) {
      try (ResultSet balRes1 = balStmt1.executeQuery()) {
        if (!balRes1.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId0);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId0, custId1, tid);
          throw new UserAbortException(msg);
        }

        checkingBalance = balRes1.getDouble(1);
        versions[1] = balRes1.getLong(2);
      } 
    } catch (SQLException ex) {
      if (type == CCType.RC_TAILOR) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId0, false);
        checkout[0]--;
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId1);
        checkout[0]--;
      }
      throw ex;
    }
    if (type == CCType.RC_TAILOR && versions[1] < 0)
      System.out.println("custome error 2");

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
        this.getPreparedStatement(conn, UpdateSavingsBalance, total, custId1)) {
      try (ResultSet res = updateStmt1.executeQuery()) {
        if (!res.next()) {
          String msg = String.format("No %s for customer #%d", SmallBankConstants.TABLENAME_CHECKING, custId1);
          if (type == CCType.RC_TAILOR_LOCK)
            releaseTailorLock(phase, custId0, custId1, tid);
          throw new UserAbortException(msg);
        }

        versions[2] = res.getLong(1);
      } 
    } catch (SQLException ex) {
      if (type == CCType.RC_TAILOR) {
        FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId1, false);
        checkout[0]--;
      }
      throw ex;
    }
    if (type == CCType.RC_TAILOR && versions[2] < 0)
      System.out.println("custome error 3");
    if (type == CCType.RC_TAILOR) {
      int validationPhase;
      LOG.debug("Amalgamate #" + tid + " acquire EX validation lock - savings #"+custId0);
      LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_SAVINGS, tid, custId0, LockType.EX, type);
      LOG.debug("Amalgamate #" + tid + " acquired EX validation lock - savings #"+custId0);
      validationPhase = 1;
      if (custId0 < custId1) {
        try {
          LOG.debug("Amalgamate #" + tid + " acquire EX validation lock - checking #"+custId0);
          LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId0, LockType.EX, type);
          LOG.debug("Amalgamate #" + tid + " acquired EX validation lock - checking #"+custId0);
          validationPhase = 2;
        } catch (SQLException ex) {
          releaseTailorValidationLock(validationPhase, custId0, custId1, tid);
          throw ex;
        }
        try {
          LOG.debug("Amalgamate #" + tid + " acquire EX validation lock - checking #"+custId1);
          LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId1, LockType.EX, type);
          LOG.debug("Amalgamate #" + tid + " acquired EX validation lock - checking #"+custId1);
          validationPhase = 3;
        } catch (SQLException ex) {
          releaseTailorValidationLock(validationPhase, custId0, custId1, tid);
          throw ex;
        }
      } else {
        try {
          LOG.debug("Amalgamate #" + tid + " acquire EX validation lock - checking #"+custId1);
          LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId1, LockType.EX, type);
          LOG.debug("Amalgamate #" + tid + " acquired EX validation lock - checking #"+custId1);
          validationPhase = 2;
        } catch (SQLException ex) {
          releaseTailorValidationLock(validationPhase, custId0, custId1, tid);
          throw ex;
        }
        try {
          LOG.debug("Amalgamate #" + tid + " acquire EX validation lock - checking #"+custId0);
          LockTable.getInstance().tryValidationLock(SmallBankConstants.TABLENAME_CHECKING, tid, custId0, LockType.EX, type);
          LOG.debug("Amalgamate #" + tid + " acquired EX validation lock - checking #"+custId0);
          validationPhase = 3;
        } catch (SQLException ex) {
          releaseTailorValidationLock(validationPhase, custId0, custId1, tid);
          throw ex;
        }
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

  private void releaseTailorValidationLock(int phase, long custId0, long custId1, long tid) {
    if (phase == 1) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - savings #"+custId0);
    } else if (phase == 2) {
      if (custId0 < custId1) {
        LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
        LOG.debug("Amalgamate #" + tid + " release EX validation lock - checking #"+custId0);
      } else {
        LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId1, LockType.EX);
        LOG.debug("Amalgamate #" + tid + " release EX validation lock - checking #"+custId1);
      }
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - savings #"+custId0);
    } else if (phase == 3) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId1, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - checking #"+custId1);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - checking #"+custId0);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - savings #"+custId0);
    }
  }

  public void doAfterCommit(long custId0, long custId1, CCType type, boolean success, long[] versions, long tid, int[] checkout) {
    if (!success) {
      if (type == CCType.RC_TAILOR || type == CCType.SI_TAILOR) {
        if (versions[0] >= 0) {
          FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId0, true);
          checkout[0]--;
        }
      }
      if (type == CCType.RC_TAILOR) {
        if (versions[1] >= 0) {
          FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId0, true);
          checkout[0]--;
        }
        if (versions[2] >= 0) {
          FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId1, true);
          checkout[0]--;
        }
      }
      return;
    }

    if (type == CCType.RC_TAILOR_LOCK) {
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId1, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_CHECKING, custId0, tid);
      LockTable.getInstance().releaseLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, tid);
    }
    if (type == CCType.RC_TAILOR) {
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId1, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - checking #"+custId1);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_CHECKING, custId0, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - checking #"+custId0);
      LockTable.getInstance().releaseValidationLock(SmallBankConstants.TABLENAME_SAVINGS, custId0, LockType.EX);
      LOG.debug("Amalgamate #" + tid + " release EX validation lock - savings #"+custId0);
      // update the
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_SAVINGS, custId0, versions[0]);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId0, versions[1]);
      LockTable.getInstance().updateHotspotVersion(SmallBankConstants.TABLENAME_CHECKING, custId1, versions[2]);
      FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId0, true);
      checkout[0]--;
      FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId0, true);
      checkout[0]--;
      FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_CHECKING, custId1, true);
      checkout[0]--;
      if (checkout[0] != 0)
        LOG.debug("Amg Transaction #" + tid + " checkout: " + checkout[0]);
    }
    if (type == CCType.SI_TAILOR) {
      FlowRate.getInstance().writeOperationFinish(SmallBankConstants.TABLENAME_SAVINGS, custId0, true);
      checkout[0]--;
      if (checkout[0] != 0)
        LOG.debug("Amg Transaction #" + tid + " checkout: " + checkout[0]);
    }
  }
}
