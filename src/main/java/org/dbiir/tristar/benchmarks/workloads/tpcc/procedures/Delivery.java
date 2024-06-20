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
import org.dbiir.tristar.common.CCType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Random;

public class Delivery extends TPCCProcedure {

  private static final Logger LOG = LoggerFactory.getLogger(Delivery.class);

  public SQLStmt delivUpdateOrderStatusSQL =
      new SQLStmt(
          """
        UPDATE %s
           SET O_STATUS = ?
         WHERE O_ID = ?
           AND O_D_ID = ?
           AND O_W_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_OPENORDER));

  public SQLStmt delivUpdateDeliveryInfoSQL =
          new SQLStmt(
                  """
                UPDATE %s
                   SET ol_delivery_info = ?
                 WHERE OL_O_ID = ?
                   AND OL_D_ID = ?
                   AND OL_W_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_ORDERLINE));

  public SQLStmt delivUpdateCustBalSQL =
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


  public void run(
      Connection conn,
      Random gen,
      int w_id,
      int numWarehouses,
      int terminalDistrictLowerID,
      int terminalDistrictUpperID,
      CCType ccType,
      TPCCWorker w)
      throws SQLException {



    int d_id1 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
    int d_id2 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);

    int no_o_id1 = TPCCUtil.randomNumber(1,3000, gen);
    int no_o_id2 = TPCCUtil.randomNumber(1,3000, gen);
    int customerId = TPCCUtil.getCustomerID(gen);

    if (ccType == CCType.RC_ELT) {
      setConflictC(conn, w_id, d_id1, customerId);
    }

    updateOrderStatus(conn, w_id, d_id1, no_o_id1);

    updateDeliveryInfo(conn, w_id, d_id1, no_o_id1);

    updateDeliveryInfo(conn, w_id, d_id2, no_o_id2);

    float orderLineTotal = (float) (TPCCUtil.randomNumber(100, 500000, gen) / 100.0);

    updateBalance(conn, w_id, d_id1, customerId, orderLineTotal);
  }

  private void updateOrderStatus(Connection conn, int w_id, int d_id, int no_o_id)
          throws SQLException {
    try (PreparedStatement delivUpdateCarrierId =
                 this.getPreparedStatement(conn, delivUpdateOrderStatusSQL)) {
      delivUpdateCarrierId.setString(1, "delivered");
      delivUpdateCarrierId.setInt(2, no_o_id);
      delivUpdateCarrierId.setInt(3, d_id);
      delivUpdateCarrierId.setInt(4, w_id);

      int result = delivUpdateCarrierId.executeUpdate();

      if (result != 1) {
        String msg =
                String.format(
                        "Failed to update ORDER record [W_ID=%d, D_ID=%d, O_ID=%d]", w_id, d_id, no_o_id);
        throw new RuntimeException(msg);
      }
    }
  }

  private void updateDeliveryInfo(Connection conn, int w_id, int d_id, int no_o_id)
          throws SQLException {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    try (PreparedStatement delivUpdateDeliveryDate =
                 this.getPreparedStatement(conn, delivUpdateDeliveryInfoSQL)) {
      delivUpdateDeliveryDate.setString(1, "delivered");
      delivUpdateDeliveryDate.setInt(2, no_o_id);
      delivUpdateDeliveryDate.setInt(3, d_id);
      delivUpdateDeliveryDate.setInt(4, w_id);

      int result = delivUpdateDeliveryDate.executeUpdate();

      if (result == 0) {
        String msg =
                String.format(
                        "Failed to update ORDER_LINE records [W_ID=%d, D_ID=%d, O_ID=%d]",
                        w_id, d_id, no_o_id);
        throw new RuntimeException(msg);
      }
    }
  }


  private void updateBalance(
      Connection conn, int w_id, int d_id, int c_id, float orderLineTotal) throws SQLException {

    try (PreparedStatement delivUpdateCustBalDelivCnt =
        this.getPreparedStatement(conn, delivUpdateCustBalSQL)) {
      delivUpdateCustBalDelivCnt.setBigDecimal(1, BigDecimal.valueOf(orderLineTotal));
      delivUpdateCustBalDelivCnt.setInt(2, w_id);
      delivUpdateCustBalDelivCnt.setInt(3, d_id);
      delivUpdateCustBalDelivCnt.setInt(4, c_id);

      int result = delivUpdateCustBalDelivCnt.executeUpdate();

      if (result == 0) {
        String msg =
            String.format(
                "Failed to update CUSTOMER record [W_ID=%d, D_ID=%d, C_ID=%d]", w_id, d_id, c_id);
        throw new RuntimeException(msg);
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
