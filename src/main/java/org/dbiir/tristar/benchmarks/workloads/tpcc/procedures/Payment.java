/*
 * Copyright 2020 by OLTPBenchmark Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.dbiir.tristar.benchmarks.workloads.tpcc.procedures;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.util.StringUtil;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;

public class Payment extends TPCCProcedure {

  public SQLStmt payUpdateWhseSQL =
          new SQLStmt(
                  """
                UPDATE %s
                   SET W_YTD = W_YTD + ?
                 WHERE W_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_WAREHOUSE));

  public SQLStmt payUpdateDistSQL =
          new SQLStmt(
                  """
                UPDATE %s
                   SET D_YTD = D_YTD + ?
                 WHERE D_W_ID = ?
                   AND D_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_DISTRICT));

  public SQLStmt payUpdateCustBalSQL =
          new SQLStmt(
                  """
                UPDATE %s
                   SET C_BALANCE = C_BALANCE + ?
                 WHERE C_W_ID = ?
                   AND C_D_ID = ?
                   AND C_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_CUSTOMER));

  public final SQLStmt stmtUpdateConflictCSQL =
          new SQLStmt(
                  """
                SELECT *
                 FROM %s
                 WHERE C_W_ID = ?
                   AND C_D_ID = ?
                   AND C_ID = ?
                 FOR UPDATE
            """
                          .formatted(TPCCConstants.TABLENAME_CONFLICT_CUSTOMER));

  public final SQLStmt stmtUpdateConflictWSQL =
          new SQLStmt(
                  """
                SELECT *
                 FROM %s
                 WHERE W_ID = ?
                 FOR UPDATE
            """
                          .formatted(TPCCConstants.TABLENAME_CONFLICT_WAREHOUSE));
  static HashMap<Integer, Integer> clientServerIndexMap = new HashMap<>();
  static {
    clientServerIndexMap.put(0, -1);
    clientServerIndexMap.put(1, -1);
    clientServerIndexMap.put(2, -1);
  }
  @Override
  public void updateClientServerIndexMap(int clientSideIndex, int serverSideIndex) {
    clientServerIndexMap.put(clientSideIndex, serverSideIndex);
  }
  @Override
  public List<TemplateSQLMeta> getTemplateSQLMetas() {
    List<TemplateSQLMeta> templateSQLMetas = new LinkedList<>();
    templateSQLMetas.add(new TemplateSQLMeta("Payment", 1, TPCCConstants.TABLENAME_WAREHOUSE,
            0, "UPDATE " + TPCCConstants.TABLENAME_WAREHOUSE + " SET W_YTD = W_YTD + ? WHERE W_ID = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("Payment", 1, TPCCConstants.TABLENAME_DISTRICT,
            1, "UPDATE " + TPCCConstants.TABLENAME_DISTRICT + " SET D_YTD = D_YTD + ? WHERE D_W_ID = ? AND D_ID = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("Payment", 1, TPCCConstants.TABLENAME_CUSTOMER,
            2, "UPDATE " + TPCCConstants.TABLENAME_CUSTOMER +" SET C_BALANCE = C_BALANCE + ? WHERE C_W_ID = ? AND C_D_ID = ? AND C_ID = ?"));
    return templateSQLMetas;
  }

  public void run(
          Connection conn,
          Random gen,
          int w_id,
          int numWarehouses,
          int terminalDistrictLowerID,
          int terminalDistrictUpperID,
          CCType ccType,
          long[] keys,
          TPCCWorker worker)
          throws SQLException {

    int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);

    float paymentAmount = (float) (TPCCUtil.randomNumber(-500000, 500000, gen) / 100.0);
    int customerDistrictID = districtID;
    w_id = (w_id % 17) + 1;
    int customerWarehouseID = w_id;
    int customerID;
    customerID = TPCCUtil.getCustomerID(gen);

    if (ccType == CCType.RC_ELT) {
      setConflictC(conn, w_id, customerDistrictID, customerID);
      setConflictW(conn, w_id);
    }

    // U[Warehouse]
    updateWarehouse(worker, conn, customerWarehouseID, paymentAmount);

    keys[0] = customerWarehouseID - 1;

    updateDistrict(worker, conn, customerWarehouseID, districtID, paymentAmount);

    // U[Customer]
    updateBalance(worker, conn, customerDistrictID, customerWarehouseID, customerID, BigDecimal.valueOf(paymentAmount));

//  keys[1] = ((long) (customerWarehouseID - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
//         + (long) (customerDistrictID - 1) * TPCCConfig.configCustPerDist + (long) (customerID - 1));
//
//  keys[2] = ((long) (customerWarehouseID - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
//         + (long) (customerDistrictID - 1) * TPCCConfig.configCustPerDist);
//
//
//  if (ccType == CCType.RC_TAILOR) {
//   // lock WAREHOUSE
//   LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, tid, keys[0], LockType.EX, ccType);
//  }
//  if (ccType == CCType.RC_TAILOR) {
//   int validationPhase = 1;
//   try {
//     LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_CUSTOMER, tid, keys[1], LockType.EX, ccType);
//   } catch (SQLException ex) {
//     releaseTailorLock(validationPhase, keys, ccType);
//     throw ex;
//   }
//  }

  }

  private void updateWarehouse(Worker worker, Connection conn, int w_id, float paymentAmount) throws SQLException {
    if (worker.useTxnSailsServer()) {
      try {
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "Payment", 0, BigDecimal.valueOf(paymentAmount), w_id));
        worker.parseExecutionResults();
      } catch (InterruptedException e) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
      try (PreparedStatement payUpdateWhse = this.getPreparedStatement(conn, payUpdateWhseSQL)) {
        payUpdateWhse.setBigDecimal(1, BigDecimal.valueOf(paymentAmount));
        payUpdateWhse.setInt(2, w_id);
        try (ResultSet rs = payUpdateWhse.executeQuery()) {
          if (!rs.next()) {
            throw new RuntimeException("W_ID=" + w_id + " not found!");
          }
        }
      }
    }
  }

  private void updateDistrict(Worker worker, Connection conn, int w_id, int districtID, float paymentAmount)
          throws SQLException {
    if (worker.useTxnSailsServer()) {
      try {
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "Payment", 1, BigDecimal.valueOf(paymentAmount), w_id, districtID));
        worker.parseExecutionResults();
      } catch (InterruptedException e) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
      try (PreparedStatement payUpdateDist = this.getPreparedStatement(conn, payUpdateDistSQL)) {
        payUpdateDist.setBigDecimal(1, BigDecimal.valueOf(paymentAmount));
        payUpdateDist.setInt(2, w_id);
        payUpdateDist.setInt(3, districtID);

        int result = payUpdateDist.executeUpdate();

        if (result == 0) {
          throw new RuntimeException("D_ID=" + districtID + " D_W_ID=" + w_id + " not found!");
        }
      }
    }
  }

  private void updateBalance(Worker worker, Connection conn, int customerDistrictID, int customerWarehouseID,
                             int customerID, BigDecimal amount)
          throws SQLException {
    if (worker.useTxnSailsServer()) {
      try {
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "Payment", 2, amount, customerWarehouseID,
                customerDistrictID, customerID));
        worker.parseExecutionResults();
      } catch (InterruptedException ex) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
      try (PreparedStatement payUpdateCustBal =
                   this.getPreparedStatement(conn, payUpdateCustBalSQL)) {
        payUpdateCustBal.setBigDecimal(1, amount);
        payUpdateCustBal.setInt(2, customerWarehouseID);
        payUpdateCustBal.setInt(3, customerDistrictID);
        payUpdateCustBal.setInt(4, customerID);

        try (ResultSet rs = payUpdateCustBal.executeQuery()) {
          if (!rs.next()) {
            throw new RuntimeException(
                    "C_ID="
                            + customerID
                            + " C_W_ID="
                            + customerWarehouseID
                            + " C_D_ID="
                            + customerDistrictID
                            + " not found!");
          }
        }
      }
    }
  }

  private void setConflictC(Connection conn, int w_id, int d_id, int c_id) throws SQLException {
    try (PreparedStatement stmtSetConfC = this.getPreparedStatement(conn, stmtUpdateConflictCSQL)) {
      stmtSetConfC.setInt(1, w_id);
      stmtSetConfC.setInt(2, d_id);
      stmtSetConfC.setInt(3, c_id);
      try (ResultSet rs = stmtSetConfC.executeQuery()) {
        if (!rs.next()) {
          throw new RuntimeException("C_D_ID=" + d_id + " C_ID=" + c_id + " not found!");
        }
      }
    }
  }

  private void setConflictW(Connection conn, int w_id) throws SQLException {
    try (PreparedStatement stmtSetConfW = this.getPreparedStatement(conn, stmtUpdateConflictWSQL)) {
      stmtSetConfW.setInt(1, w_id);
      try (ResultSet rs = stmtSetConfW.executeQuery()) {
        if (!rs.next()) {
          throw new RuntimeException("W_ID=" + w_id + " not found!");
        }
      }
    }
  }

  public void doAfterCommit() {
  }
}
