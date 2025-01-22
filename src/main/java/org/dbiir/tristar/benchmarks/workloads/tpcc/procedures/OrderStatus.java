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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import java.util.Random;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.util.StringUtil;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.adapter.TransactionCollector;
import org.dbiir.tristar.benchmarks.api.SQLStmt;
import org.dbiir.tristar.benchmarks.catalog.RWRecord;
import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.common.LockType;
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                 WHERE OL_W_ID = ?
                   AND OL_D_ID = ?
                   AND OL_O_ID = ?
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
                   WHERE OL_W_ID = ?
                     AND OL_D_ID = ?
                     AND OL_O_ID = ?
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
    templateSQLMetas.add(new TemplateSQLMeta("OrderStatus", 0, TPCCConstants.TABLENAME_CUSTOMER,
            0, "SELECT C_INFO, C_BALANCE " + TPCCConstants.TABLENAME_CUSTOMER + " WHERE C_W_ID = ? AND C_D_ID = ? AND C_ID = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("OrderStatus", 0, TPCCConstants.TABLENAME_OPENORDER,
            1, "SELECT O_STATUS FROM " + TPCCConstants.TABLENAME_OPENORDER + " WHERE O_W_ID = ? AND O_D_ID = ? AND O_ID = ?"));
    templateSQLMetas.add(new TemplateSQLMeta("OrderStatus", 0, TPCCConstants.TABLENAME_ORDERLINE,
            2, "SELECT OL_DELIVERY_INFO, OL_QUANTITY " + TPCCConstants.TABLENAME_ORDERLINE +" WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ?"));
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
          TPCCWorker w)
          throws SQLException {

    int d_id1 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);

    int c_id;
    c_id = TPCCUtil.getCustomerID(gen);

    int o_id1 = c_id;

    if (ccType == CCType.RC_FOR_UPDATE) {
      updateCustomerById(conn, w_id, d_id1, c_id);

      updateOrderDetails(conn, w_id, d_id1, o_id1);

      updateOrderLines(conn, w_id, d_id1, o_id1);

      return;
    }

    if (ccType == CCType.RC_ELT) {
      setConflictC(conn, w_id, d_id1, c_id);
    }

    // R[Customer]
    getCustomerById(w, w_id, d_id1, c_id, conn);
//    keys[0] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
//            + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (long) (c_id - 1));
    // Validate R[Customer]

    // R[Orders]
    getOrderStatus(w, w_id, d_id1, o_id1, conn);


    keys[1] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
            + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (long) (o_id1 - 1));

    // Validate R[Orders]

    // R[OrderLines]
    getOrderLines(w, w_id, d_id1, o_id1, conn);
    keys[2] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
            + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (long) (o_id1 - 1));

    // Validate R[OrderLines]
  }

  private void updateOrderDetails(Connection conn, int w_id, int d_id, int o_id)
          throws SQLException {
    try (PreparedStatement ordStatGetNewestOrd =
                 this.getPreparedStatement(conn, ordStatUpdateNewestOrdSQL)) {

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
      ordStatGetOrderLines.setInt(1, w_id);
      ordStatGetOrderLines.setInt(2, d_id);
      ordStatGetOrderLines.setInt(3, o_id);

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

  public void updateCustomerById(Connection conn, int c_w_id, int c_d_id, int c_id)
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

  public void getCustomerById(Worker worker, int c_w_id, int c_d_id, int c_id, Connection conn)
          throws SQLException {
    if (worker.useTxnSailsServer()) {
      try {
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "OrderStatus", 0, c_w_id, c_d_id, c_id));
        worker.parseExecutionResults();
      } catch (InterruptedException e) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
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
  }

  public void getOrderStatus(Worker worker, int o_w_id, int o_d_id, int o_id, Connection conn)
          throws SQLException {
    if (worker.useTxnSailsServer()) {
      try {
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "OrderStatus", 1, o_w_id, o_d_id, o_id));
        worker.parseExecutionResults();
      } catch (InterruptedException e) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
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
  }

  public void getOrderLines(Worker worker, int ol_w_id, int ol_d_id, int ol_o_id, Connection conn)
          throws SQLException {
    if (worker.useTxnSailsServer()) {
      try {
        worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "OrderStatus", 2, ol_w_id, ol_d_id, ol_o_id));
        worker.parseExecutionResults();
      } catch (InterruptedException e) {
        System.out.println("InterruptedException on sending or receiving message");
      }
    } else {
      try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, ordStatGetOrderLinesSQL)) {

        stmtGetCust.setInt(1, ol_w_id);
        stmtGetCust.setInt(2, ol_d_id);
        stmtGetCust.setInt(3, ol_o_id);

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

  public void doAfterCommit() {
//      LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[0], LockType.SH);
//      LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[1], LockType.SH);
//      LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[2], LockType.SH);
  }

}
