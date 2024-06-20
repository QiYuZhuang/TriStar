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
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Customer;
import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Oorder;
import org.dbiir.tristar.common.CCType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderStatus extends TPCCProcedure {

  private static final Logger LOG = LoggerFactory.getLogger(OrderStatus.class);

  public SQLStmt ordStatUpdateNewestOrdSQL =
      new SQLStmt(
          """
        UPDATE %s
        SET o_status = o_status
         WHERE O_W_ID = ?
           AND O_D_ID = ?
           AND o_id = ?
    """
              .formatted(TPCCConstants.TABLENAME_OPENORDER));

  public SQLStmt ordStatUpdateOrderLinesSQL =
      new SQLStmt(
          """
        update %s
        set ol_delivery_info = ol_delivery_info
         WHERE OL_O_ID = ?
           AND OL_D_ID = ?
           AND OL_W_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_ORDERLINE));

  public SQLStmt payUpdateCustSQL =
      new SQLStmt(
          """
        UPDATE %s
         SET c_balance = c_balance
         WHERE C_W_ID = ?
           AND C_D_ID = ?
           AND C_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_CUSTOMER));

  public SQLStmt ordStatGetCustSQL =
          new SQLStmt(
                  """
                SELECT  c_info, c_balance
                  FROM %s
                 WHERE C_W_ID = ?
                   AND C_D_ID = ?
                   AND C_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_CUSTOMER));

  public SQLStmt ordStatGetNewestOrdSQL =
          new SQLStmt(
                  """
                SELECT o_status
                  FROM %s
                  WHERE O_W_ID = ?
                   AND O_D_ID = ?
                   AND O_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_OPENORDER));

  public SQLStmt ordStatGetOrderLinesSQL =
          new SQLStmt(
                  """
                SELECT ol_delivery_info, ol_quantity
                   FROM %s
                   WHERE OL_O_ID = ?
                     AND OL_D_ID = ?
                     AND OL_W_ID = ?
            """
                          .formatted(TPCCConstants.TABLENAME_ORDERLINE));

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
    int c_id = -1;
    c_id = TPCCUtil.getCustomerID(gen);
    int o_id1 = TPCCUtil.randomNumber(1, 3000, gen);
    int o_id2 = TPCCUtil.randomNumber(1, 3000, gen);

    if (ccType == CCType.RC_FOR_UPDATE) {
      updateCustomerById(w_id, d_id1, c_id, conn);

      updateOrderDetails(conn, w_id, d_id1, o_id1);

      updateOrderLines(conn, w_id, d_id1, o_id1);
      updateOrderLines(conn, w_id, d_id2, o_id2);
    }

    if (ccType == CCType.RC_ELT) {
      setConflictC(conn, w_id, d_id1, c_id);
    }

    getCustomerById(w_id, d_id1, c_id, conn);
    getOrderStatus(w_id, d_id1, o_id1, conn);;
    getOrderLines(w_id, d_id1, o_id1, conn);
    getOrderLines( w_id, d_id2, o_id2, conn);
  }

  private void updateOrderDetails(Connection conn, int w_id, int d_id, int o_id)
      throws SQLException {
    try (PreparedStatement ordStatGetNewestOrd =
        this.getPreparedStatement(conn, ordStatUpdateNewestOrdSQL)) {

      // find the newest order for the customer
      // retrieve the carrier & order date for the most recent order.

      ordStatGetNewestOrd.setInt(1, w_id);
      ordStatGetNewestOrd.setInt(2, d_id);
      ordStatGetNewestOrd.setInt(3, o_id);

      int res = ordStatGetNewestOrd.executeUpdate();

      if (res == 0) {
        String msg =
            String.format(
                "No order records for CUSTOMER [C_W_ID=%d, C_D_ID=%d, C_ID=%d]",
                w_id, d_id, o_id);

        throw new RuntimeException(msg);
      }
    }
  }

  private void updateOrderLines(Connection conn, int w_id, int d_id, int o_id)
      throws SQLException {

    try (PreparedStatement ordStatGetOrderLines =
        this.getPreparedStatement(conn, ordStatUpdateOrderLinesSQL)) {
      ordStatGetOrderLines.setInt(1, o_id);
      ordStatGetOrderLines.setInt(2, d_id);
      ordStatGetOrderLines.setInt(3, w_id);

      int res = ordStatGetOrderLines.executeUpdate();
      if (res == 0) {
        String msg =
                String.format(
                        "Order record had no order line items [C_W_ID=%d, C_D_ID=%d, O_ID=%d]",
                        w_id, d_id, o_id);
        LOG.trace(msg);
      }

    }
  }

  // attention duplicated code across trans... ok for now to maintain separate
  // prepared statements
  public void updateCustomerById(int c_w_id, int c_d_id, int c_id, Connection conn)
      throws SQLException {

    try (PreparedStatement payGetCust = this.getPreparedStatement(conn, payUpdateCustSQL)) {

      payGetCust.setInt(1, c_w_id);
      payGetCust.setInt(2, c_d_id);
      payGetCust.setInt(3, c_id);

      int res = payGetCust.executeUpdate();

      if (res == 0) {
        String msg =
            String.format(
                "Failed to get CUSTOMER [C_W_ID=%d, C_D_ID=%d, C_ID=%d]", c_w_id, c_d_id, c_id);

        throw new RuntimeException(msg);
      }
    }
  }

  public void getCustomerById(int c_w_id, int c_d_id, int c_id, Connection conn)
          throws SQLException {

    try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, ordStatGetCustSQL)) {

      stmtGetCust.setInt(1, c_w_id);
      stmtGetCust.setInt(2, c_d_id);
      stmtGetCust.setInt(3, c_id);

      try (ResultSet rs = stmtGetCust.executeQuery()) {
        if (!rs.next()) {
          String msg =
                  String.format(
                          "Failed to get CUSTOMER [C_W_ID=%d, C_D_ID=%d, C_ID=%d]", c_w_id, c_d_id, c_id);

          throw new RuntimeException(msg);
        }
      }
    }
  }

  public void getOrderStatus(int o_w_id, int o_d_id, int o_id, Connection conn)
          throws SQLException {

    try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, ordStatGetNewestOrdSQL)) {

      stmtGetCust.setInt(1, o_w_id);
      stmtGetCust.setInt(2, o_d_id);
      stmtGetCust.setInt(3, o_id);

      try (ResultSet rs = stmtGetCust.executeQuery()) {
        if (!rs.next()) {
          String msg =
                  String.format(
                          "Failed to get ORDERS [O_W_ID=%d, O_D_ID=%d, O_ID=%d]", o_w_id, o_d_id, o_id);

          throw new RuntimeException(msg);
        }
      }
    }
  }

  public void getOrderLines(int ol_w_id, int ol_d_id, int ol_o_id, Connection conn)
          throws SQLException {

    try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, ordStatGetOrderLinesSQL)) {

      stmtGetCust.setInt(1, ol_o_id);
      stmtGetCust.setInt(2, ol_d_id);
      stmtGetCust.setInt(3, ol_w_id);

      try (ResultSet rs = stmtGetCust.executeQuery()) {
        if (!rs.next()) {
          String msg =
                  String.format(
                          "Failed to get ORDERS [OL_W_ID=%d, OL_D_ID=%d, OL_O_ID=%d]", ol_w_id, ol_d_id, ol_o_id);

          throw new RuntimeException(msg);
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

}
