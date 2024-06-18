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
         ORDER BY O_ID DESC LIMIT 1
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

  public SQLStmt customerByNameSQL =
      new SQLStmt(
          """
        SELECT C_FIRST, C_MIDDLE, C_ID, C_STREET_1, C_STREET_2, C_CITY,
               C_STATE, C_ZIP, C_PHONE, C_CREDIT, C_CREDIT_LIM, C_DISCOUNT,
               C_BALANCE, C_YTD_PAYMENT, C_PAYMENT_CNT, C_SINCE
          FROM  %s
         WHERE C_W_ID = ?
           AND C_D_ID = ?
           AND C_LAST = ?
         ORDER BY C_FIRST
    """
              .formatted(TPCCConstants.TABLENAME_CUSTOMER));

  public void run(
      Connection conn,
      Random gen,
      int w_id,
      int numWarehouses,
      int terminalDistrictLowerID,
      int terminalDistrictUpperID,
      TPCCWorker w)
      throws SQLException {

    int d_id1 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
    int d_id2 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
//    int y = TPCCUtil.randomNumber(1, 100, gen);

//    boolean c_by_name;
//    String c_last = null;
    int c_id = -1;

//    if (y <= 60) {
//      c_by_name = true;
//      c_last = TPCCUtil.getNonUniformRandomLastNameForRun(gen);
//    } else {
//      c_by_name = false;
//      c_id = TPCCUtil.getCustomerID(gen);
//    }
    c_id = TPCCUtil.getCustomerID(gen);
    int o_id1 = TPCCUtil.randomNumber(1, 3000, gen);
    int o_id2 = TPCCUtil.randomNumber(1, 3000, gen);


//    if (c_by_name) {
//      c = getCustomerByName(w_id, d_id, c_last, conn);
//    } else {
//      c = getCustomerById(w_id, d_id, c_id, conn);
//    }
    updateCustomerById(w_id, d_id1, c_id, conn);

    updateOrderDetails(conn, w_id, d_id1, o_id1);

    // retrieve the order lines for the most recent order
    updateOrderLines(conn, w_id, d_id1, o_id1);
    updateOrderLines(conn, w_id, d_id2, o_id2);
    /*
    if (LOG.isTraceEnabled()) {
      StringBuilder sb = new StringBuilder();
      sb.append("\n");
      sb.append("+-------------------------- ORDER-STATUS -------------------------+\n");
      sb.append(" Date: ");
      sb.append(TPCCUtil.getCurrentTime());
      sb.append("\n\n Warehouse: ");
      sb.append(w_id);
      sb.append("\n District:  ");
      sb.append(d_id);
      sb.append("\n\n Customer:  ");
      sb.append(c.c_id);
      sb.append("\n   Balance: ");
      sb.append(c.c_balance);
      sb.append("\n\n");
      if (o.o_id == -1) {
        sb.append(" Customer has no orders placed.\n");
      } else {
        sb.append(" Order-Number: ");
        sb.append(o.o_id);
//        sb.append("\n    Entry-Date: ");
//        sb.append(o.o_entry_d);
//        sb.append("\n    Carrier-Number: ");
//        sb.append(o.o_carrier_id);
        sb.append("\n\n");
        if (orderLines.size() != 0) {
          sb.append(" [Supply_W - Item_ID - Qty - Delivery-Info]\n");
          for (String orderLine : orderLines) {
            sb.append(" ");
            sb.append(orderLine);
            sb.append("\n");
          }
        } else {
          LOG.trace(" This Order has no Order-Lines.\n");
        }
      }
      sb.append("+-----------------------------------------------------------------+\n\n");
      LOG.trace(sb.toString());
    }

     */
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

  // attention this code is repeated in other transacitons... ok for now to
  // allow for separate statements.
  public Customer getCustomerByName(int c_w_id, int c_d_id, String c_last, Connection conn)
      throws SQLException {
    ArrayList<Customer> customers = new ArrayList<>();

    try (PreparedStatement customerByName = this.getPreparedStatement(conn, customerByNameSQL)) {

      customerByName.setInt(1, c_w_id);
      customerByName.setInt(2, c_d_id);
      customerByName.setString(3, c_last);

      try (ResultSet rs = customerByName.executeQuery()) {
        while (rs.next()) {
          Customer c = TPCCUtil.newCustomerFromResults(rs);
          c.c_id = rs.getInt("C_ID");
          // c.c_last = c_last;
          customers.add(c);
        }
      }
    }

    if (customers.size() == 0) {
      String msg =
          String.format(
              "Failed to get CUSTOMER [C_W_ID=%d, C_D_ID=%d, C_LAST=%s]", c_w_id, c_d_id, c_last);

      throw new RuntimeException(msg);
    }

    // TPC-C 2.5.2.2: Position n / 2 rounded up to the next integer, but
    // that counts starting from 1.
    int index = customers.size() / 2;
    if (customers.size() % 2 == 0) {
      index -= 1;
    }
    return customers.get(index);
  }
}
