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

import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Customer;
import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.District;
import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Warehouse;
import org.dbiir.tristar.common.CCType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class Payment extends TPCCProcedure {

  private static final Logger LOG = LoggerFactory.getLogger(Payment.class);

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
           SET C_BALANCE = ?
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

  public void run(
      Connection conn,
      Random gen,
      int w_id,
      int numWarehouses,
      int terminalDistrictLowerID,
      int terminalDistrictUpperID,
      CCType ccType,
      TPCCWorker worker)
      throws SQLException {

    int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);

    float paymentAmount = (float) (TPCCUtil.randomNumber(100, 500000, gen) / 100.0);
    int x = TPCCUtil.randomNumber(1, 100, gen);

    int customerDistrictID = getCustomerDistrictId(gen, districtID, x);
    int customerWarehouseID = getCustomerWarehouseID(gen, w_id, numWarehouses, x);
    int customerID = TPCCUtil.getCustomerID(gen);

    if (ccType == CCType.RC_ELT) {
      setConflictC(conn, customerWarehouseID, customerDistrictID, customerID);
    }


    updateWarehouse(conn, w_id, paymentAmount);


    updateDistrict(conn, w_id, districtID, paymentAmount);


    updateBalance(conn, customerDistrictID, customerWarehouseID, customerID, paymentAmount);

  }

  private int getCustomerWarehouseID(Random gen, int w_id, int numWarehouses, int x) {
    int customerWarehouseID;
    if (x <= 85) {
      customerWarehouseID = w_id;
    } else {
      do {
        customerWarehouseID = TPCCUtil.randomNumber(1, numWarehouses, gen);
      } while (customerWarehouseID == w_id && numWarehouses > 1);
    }
    return customerWarehouseID;
  }

  private int getCustomerDistrictId(Random gen, int districtID, int x) {
    if (x <= 85) {
      return districtID;
    } else {
      return TPCCUtil.randomNumber(1, TPCCConfig.configDistPerWhse, gen);
    }
  }

  private void updateWarehouse(Connection conn, int w_id, float paymentAmount) throws SQLException {
    try (PreparedStatement payUpdateWhse = this.getPreparedStatement(conn, payUpdateWhseSQL)) {
      payUpdateWhse.setBigDecimal(1, BigDecimal.valueOf(paymentAmount));
      payUpdateWhse.setInt(2, w_id);
      // MySQL reports deadlocks due to lock upgrades:
      // t1: read w_id = x; t2: update w_id = x; t1 update w_id = x
      int result = payUpdateWhse.executeUpdate();
      if (result == 0) {
        throw new RuntimeException("W_ID=" + w_id + " not found!");
      }
    }
  }

  private void updateDistrict(Connection conn, int w_id, int districtID, float paymentAmount)
      throws SQLException {
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

  private void updateBalance(
      Connection conn, int customerDistrictID, int customerWarehouseID, int customerID, double amount)
      throws SQLException {

    try (PreparedStatement payUpdateCustBal =
        this.getPreparedStatement(conn, payUpdateCustBalSQL)) {
      payUpdateCustBal.setDouble(1, amount);
      payUpdateCustBal.setInt(2, customerWarehouseID);
      payUpdateCustBal.setInt(3, customerDistrictID);
      payUpdateCustBal.setInt(4, customerID);

      int result = payUpdateCustBal.executeUpdate();

      if (result == 0) {
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

}
