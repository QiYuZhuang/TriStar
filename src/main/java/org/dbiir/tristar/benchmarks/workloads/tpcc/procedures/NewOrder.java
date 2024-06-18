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
import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Random;

public class NewOrder extends TPCCProcedure {

  private static final Logger LOG = LoggerFactory.getLogger(NewOrder.class);

  public final SQLStmt stmtGetCustSQL =
      new SQLStmt(
          """
        SELECT C_INFO
          FROM %s
         WHERE C_W_ID = ?
           AND C_D_ID = ?
           AND C_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_CUSTOMER));

  public final SQLStmt stmtGetWhseSQL =
      new SQLStmt(
          """
        SELECT W_INFO
          FROM %s
         WHERE W_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_WAREHOUSE));

  public final SQLStmt stmtGetDistSQL =
      new SQLStmt(
          """
        SELECT D_NEXT_O_ID, D_TAX
          FROM %s
         WHERE D_W_ID = ? AND D_ID = ? FOR UPDATE
    """
              .formatted(TPCCConstants.TABLENAME_DISTRICT));

  public final SQLStmt stmtInsertNewOrderSQL =
      new SQLStmt(
          """
        INSERT INTO %s
         (NO_O_ID, NO_D_ID, NO_W_ID)
         VALUES ( ?, ?, ?)
    """
              .formatted(TPCCConstants.TABLENAME_NEWORDER));

  public final SQLStmt stmtUpdateDistSQL =
      new SQLStmt(
          """
        UPDATE %s
           SET D_NEXT_O_ID = D_NEXT_O_ID + 1
         WHERE D_W_ID = ?
           AND D_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_DISTRICT));

  public final SQLStmt stmtUpdateAndGetDistSQL =
          new SQLStmt(
                  """
                UPDATE %s
                   SET D_NEXT_O_ID = D_NEXT_O_ID + 1
                 WHERE D_W_ID = ?
                   AND D_ID = ?;
                SELECT D_NEXT_O_ID, D_INFO
                  FROM %s
                  WHERE D_W_ID = ?
                   AND D_ID = ?;
            """
                          .formatted(TPCCConstants.TABLENAME_DISTRICT, TPCCConstants.TABLENAME_DISTRICT));


  public final SQLStmt stmtInsertOOrderSQL =
      new SQLStmt(
          """
        INSERT INTO %s
         (O_W_ID, O_D_ID, O_ID, O_C_ID, O_STATUS)
         VALUES (?, ?, ?, ?, ?)
    """
              .formatted(TPCCConstants.TABLENAME_OPENORDER));

  public final SQLStmt stmtGetItemSQL =
      new SQLStmt(
          """
        SELECT I_PRICE, I_NAME , I_DATA
          FROM %s
         WHERE I_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_ITEM));

  public final SQLStmt stmtGetStockSQL =
      new SQLStmt(
          """
        SELECT S_QUANTITY
          FROM %s
         WHERE S_I_ID = ?
           AND S_W_ID = ? FOR UPDATE
    """
              .formatted(TPCCConstants.TABLENAME_STOCK));

  public final SQLStmt stmtUpdateStockSQL =
      new SQLStmt(
          """
        UPDATE %s
           SET S_QUANTITY -= ?
         WHERE S_I_ID = ?
           AND S_W_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_STOCK));

  public final SQLStmt stmtInsertOrderLineSQL =
      new SQLStmt(
          """
        INSERT INTO %s
         (OL_W_ID, OL_D_ID, OL_O_ID, OL_NUMBER_, OL_I_ID, OL_DELIVERY_INFO, OL_QUANTITY)
         VALUES (?,?,?,?,?,?,?)
    """
              .formatted(TPCCConstants.TABLENAME_ORDERLINE));

  public void run(
      Connection conn,
      Random gen,
      int terminalWarehouseID,
      int numWarehouses,
      int terminalDistrictLowerID,
      int terminalDistrictUpperID,
      TPCCWorker w)
      throws SQLException {

    int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
    int customerID = TPCCUtil.getCustomerID(gen);

    int numItems = TPCCUtil.randomNumber(5, 15, gen);
    int numItems2 = TPCCUtil.randomNumber(5, 15, gen);
    int[] itemIDs = new int[numItems];
    int[] itemIDs2 = new int[numItems2];
    int[] supplierWarehouseIDs = new int[numItems];
    int[] orderQuantities = new int[numItems];
    int[] supplierWarehouseIDs2 = new int[numItems2];
    int[] orderQuantities2 = new int[numItems2];

//

    for (int i = 0; i < numItems; i++) {
      itemIDs[i] = TPCCUtil.getItemID(gen);
      supplierWarehouseIDs[i] = terminalWarehouseID;
//      if (TPCCUtil.randomNumber(1, 100, gen) > 1) {
//        supplierWarehouseIDs[i] = terminalWarehouseID;
//      } else {
//        do {
//          supplierWarehouseIDs[i] = TPCCUtil.randomNumber(1, numWarehouses, gen);
//        } while (supplierWarehouseIDs[i] == terminalWarehouseID && numWarehouses > 1);
//        allLocal = 0;
//      }
      orderQuantities[i] = TPCCUtil.randomNumber(1, 10, gen);
    }

    // we need to cause 1% of the new orders to be rolled back.
    if (TPCCUtil.randomNumber(1, 100, gen) == 1) {
      itemIDs[numItems - 1] = TPCCConfig.INVALID_ITEM_ID;
    }

    for (int i = 0; i < numItems2; i++) {
      itemIDs2[i] = TPCCUtil.getItemID(gen);
      supplierWarehouseIDs2[i] = terminalWarehouseID;
//      if (TPCCUtil.randomNumber(1, 100, gen) > 1) {
//        supplierWarehouseIDs[i] = terminalWarehouseID;
//      } else {
//        do {
//          supplierWarehouseIDs[i] = TPCCUtil.randomNumber(1, numWarehouses, gen);
//        } while (supplierWarehouseIDs[i] == terminalWarehouseID && numWarehouses > 1);
//        allLocal = 0;
//      }
      orderQuantities2[i] = TPCCUtil.randomNumber(1, 10, gen);
    }

    // we need to cause 1% of the new orders to be rolled back.
    if (TPCCUtil.randomNumber(1, 100, gen) == 1) {
      itemIDs2[numItems2 - 1] = TPCCConfig.INVALID_ITEM_ID;
    }

    newOrderTransaction(
        terminalWarehouseID,
        districtID,
        customerID,
        numItems,
        numItems2,
        itemIDs,
        supplierWarehouseIDs,
        orderQuantities,
        itemIDs2,
        supplierWarehouseIDs2,
        orderQuantities2,
        conn);
  }

  private void newOrderTransaction(
      int w_id,
      int d_id,
      int c_id,
      int o_ol_cnt,
      int o_ol_cnt2,
      int[] itemIDs,
      int[] supplierWarehouseIDs,
      int[] orderQuantities,
      int[] itemIDs2,
      int[] supplierWarehouseIDs2,
      int[] orderQuantities2,
      Connection conn)
      throws SQLException {

    getWarehouse(conn, w_id);

    int d_next_o_id = updateandgetDistrict(conn, w_id, d_id);

    getCustomer(conn, w_id, d_id, c_id);

    insertOpenOrder(conn, w_id, d_id, c_id, d_next_o_id);

    // insertNewOrder(conn, w_id, d_id, d_next_o_id);

    try (PreparedStatement stmtUpdateStock = this.getPreparedStatement(conn, stmtUpdateStockSQL);
        PreparedStatement stmtInsertOrderLine =
            this.getPreparedStatement(conn, stmtInsertOrderLineSQL)) {

      for (int ol_number = 1; ol_number <= o_ol_cnt; ol_number++) {
        int ol_supply_w_id = supplierWarehouseIDs[ol_number - 1];
        int ol_i_id = itemIDs[ol_number - 1];
        int ol_quantity = orderQuantities[ol_number - 1];

        // this may occasionally error and that's ok!
        //  float i_price = getItemPrice(conn, ol_i_id);

        // float ol_amount = ol_quantity * i_price;

        //Stock s = getStock(conn, ol_supply_w_id, ol_i_id, ol_quantity);

        stmtUpdateStock.setInt(1, ol_quantity);
//        stmtUpdateStock.setInt(2, ol_quantity);
//        stmtUpdateStock.setInt(3, s_remote_cnt_increment);
        stmtUpdateStock.setInt(2, ol_i_id);
        stmtUpdateStock.setInt(3, ol_supply_w_id);
        stmtUpdateStock.addBatch();
        //String ol_dist_info = getDistInfo(d_id, s);

        stmtInsertOrderLine.setInt(3, d_next_o_id);
        stmtInsertOrderLine.setInt(2, d_id);
        stmtInsertOrderLine.setInt(1, w_id);
        stmtInsertOrderLine.setInt(4, ol_number);
        stmtInsertOrderLine.setInt(5, ol_i_id);
        stmtInsertOrderLine.setInt(6, ol_supply_w_id);
        stmtInsertOrderLine.setInt(7, ol_quantity);
        // stmtInsertOrderLine.setDouble(8, ol_amount);
        //stmtInsertOrderLine.setString(9, ol_dist_info);
        stmtInsertOrderLine.addBatch();

//        int s_remote_cnt_increment;
//
//        if (ol_supply_w_id == w_id) {
//          s_remote_cnt_increment = 0;
//        } else {
//          s_remote_cnt_increment = 1;
//        }

      }

      stmtInsertOrderLine.executeBatch();
      stmtInsertOrderLine.clearBatch();

      stmtUpdateStock.executeBatch();
      stmtUpdateStock.clearBatch();
    }

    try (PreparedStatement stmtUpdateStock2 = this.getPreparedStatement(conn, stmtUpdateStockSQL);
         PreparedStatement stmtInsertOrderLine2 =
                 this.getPreparedStatement(conn, stmtInsertOrderLineSQL)) {

      for (int ol_number = 1; ol_number <= o_ol_cnt2; ol_number++) {
        int ol_supply_w_id = supplierWarehouseIDs2[ol_number - 1];
        int ol_i_id = itemIDs2[ol_number - 1];
        int ol_quantity = orderQuantities2[ol_number - 1];

        // this may occasionally error and that's ok!
        //  float i_price = getItemPrice(conn, ol_i_id);

        // float ol_amount = ol_quantity * i_price;

        // Stock s = getStock(conn, ol_supply_w_id, ol_i_id, ol_quantity);

        //String ol_dist_info = getDistInfo(d_id, s);

        stmtUpdateStock2.setInt(1, ol_quantity);
//        stmtUpdateStock.setInt(2, ol_quantity);
//        stmtUpdateStock.setInt(3, s_remote_cnt_increment);
        stmtUpdateStock2.setInt(2, ol_i_id);
        stmtUpdateStock2.setInt(3, ol_supply_w_id);
        stmtUpdateStock2.addBatch();

        stmtInsertOrderLine2.setInt(3, d_next_o_id);
        stmtInsertOrderLine2.setInt(2, d_id);
        stmtInsertOrderLine2.setInt(1, w_id);
        stmtInsertOrderLine2.setInt(4, ol_number);
        stmtInsertOrderLine2.setInt(5, ol_i_id);
        stmtInsertOrderLine2.setInt(6, ol_supply_w_id);
        stmtInsertOrderLine2.setInt(7, ol_quantity);
        // stmtInsertOrderLine.setDouble(8, ol_amount);
        //stmtInsertOrderLine.setString(9, ol_dist_info);
        stmtInsertOrderLine2.addBatch();

//        int s_remote_cnt_increment;
//
//        if (ol_supply_w_id == w_id) {
//          s_remote_cnt_increment = 0;
//        } else {
//          s_remote_cnt_increment = 1;
//        }

      }

      stmtInsertOrderLine2.executeBatch();
      stmtInsertOrderLine2.clearBatch();

      stmtUpdateStock2.executeBatch();
      stmtUpdateStock2.clearBatch();
    }
  }

//  private String getDistInfo(int d_id, Stock s) {
//    return switch (d_id) {
//      case 1 -> s.s_dist_01;
//      case 2 -> s.s_dist_02;
//      case 3 -> s.s_dist_03;
//      case 4 -> s.s_dist_04;
//      case 5 -> s.s_dist_05;
//      case 6 -> s.s_dist_06;
//      case 7 -> s.s_dist_07;
//      case 8 -> s.s_dist_08;
//      case 9 -> s.s_dist_09;
//      case 10 -> s.s_dist_10;
//      default -> null;
//    };
//  }

  private Stock getStock(Connection conn, int ol_supply_w_id, int ol_i_id, int ol_quantity)
      throws SQLException {
    try (PreparedStatement stmtGetStock = this.getPreparedStatement(conn, stmtGetStockSQL)) {
      stmtGetStock.setInt(1, ol_i_id);
      stmtGetStock.setInt(2, ol_supply_w_id);
      try (ResultSet rs = stmtGetStock.executeQuery()) {
        if (!rs.next()) {
          throw new RuntimeException("S_I_ID=" + ol_i_id + " not found!");
        }
        Stock s = new Stock();
        s.s_quantity = rs.getInt("S_QUANTITY");

        if (s.s_quantity - ol_quantity >= 10) {
          s.s_quantity -= ol_quantity;
        } else {
          s.s_quantity += -ol_quantity + 91;
        }

        return s;
      }
    }
  }

  private float getItemPrice(Connection conn, int ol_i_id) throws SQLException {
    try (PreparedStatement stmtGetItem = this.getPreparedStatement(conn, stmtGetItemSQL)) {
      stmtGetItem.setInt(1, ol_i_id);
      try (ResultSet rs = stmtGetItem.executeQuery()) {
        if (!rs.next()) {
          // This is (hopefully) an expected error: this is an expected new order rollback
          throw new UserAbortException(
              "EXPECTED new order rollback: I_ID=" + ol_i_id + " not found!");
        }

        return rs.getFloat("I_PRICE");
      }
    }
  }

  private void insertNewOrder(Connection conn, int w_id, int d_id, int o_id) throws SQLException {
    try (PreparedStatement stmtInsertNewOrder =
        this.getPreparedStatement(conn, stmtInsertNewOrderSQL)) {
      stmtInsertNewOrder.setInt(1, o_id);
      stmtInsertNewOrder.setInt(2, d_id);
      stmtInsertNewOrder.setInt(3, w_id);
      int result = stmtInsertNewOrder.executeUpdate();

      if (result == 0) {
        LOG.warn("new order not inserted");
      }
    }
  }

  private void insertOpenOrder(
      Connection conn, int w_id, int d_id, int c_id, int o_id)
      throws SQLException {
    try (PreparedStatement stmtInsertOOrder =
        this.getPreparedStatement(conn, stmtInsertOOrderSQL)) {
      stmtInsertOOrder.setInt(1, w_id);
      stmtInsertOOrder.setInt(2, d_id);
      stmtInsertOOrder.setInt(3, o_id);
      stmtInsertOOrder.setInt(4, c_id);
      stmtInsertOOrder.setString(5, "created");


      int result = stmtInsertOOrder.executeUpdate();

      if (result == 0) {
        LOG.warn("open order not inserted");
      }
    }
  }

  private int updateandgetDistrict(Connection conn, int w_id, int d_id) throws SQLException {
    try (PreparedStatement stmtUpdateAndGetDist = this.getPreparedStatement(conn, stmtUpdateAndGetDistSQL)) {
      stmtUpdateAndGetDist.setInt(1, w_id);
      stmtUpdateAndGetDist.setInt(2, d_id);
      stmtUpdateAndGetDist.setInt(3, w_id);
      stmtUpdateAndGetDist.setInt(4, d_id);

      boolean resultsAvailable = stmtUpdateAndGetDist.execute();
      int res = -1;
      while (true) {
        if (resultsAvailable) {
          ResultSet rs = stmtUpdateAndGetDist.getResultSet();
          if (!rs.next()) {
            throw new RuntimeException(
            "Error!! Cannot update next_order_id on district for D_ID=" + d_id + " D_W_ID=" + w_id);
          }
          res = rs.getInt("D_NEXT_O_ID");
        } else if (stmtUpdateAndGetDist.getUpdateCount() < 0){
          break;
        }

        resultsAvailable = stmtUpdateAndGetDist.getMoreResults();
      }

//      int result = stmtUpdateDist.execute();
//      if (result == 0) {
//        throw new RuntimeException(
//            "Error!! Cannot update next_order_id on district for D_ID=" + d_id + " D_W_ID=" + w_id);
//      }

      return res;
    }
  }

  private int getDistrict(Connection conn, int w_id, int d_id) throws SQLException {
    try (PreparedStatement stmtGetDist = this.getPreparedStatement(conn, stmtGetDistSQL)) {
      stmtGetDist.setInt(1, w_id);
      stmtGetDist.setInt(2, d_id);
      try (ResultSet rs = stmtGetDist.executeQuery()) {
        if (!rs.next()) {
          throw new RuntimeException("D_ID=" + d_id + " D_W_ID=" + w_id + " not found!");
        }
        return rs.getInt("D_NEXT_O_ID");
      }
    }
  }

  private void getWarehouse(Connection conn, int w_id) throws SQLException {
    try (PreparedStatement stmtGetWhse = this.getPreparedStatement(conn, stmtGetWhseSQL)) {
      stmtGetWhse.setInt(1, w_id);
      try (ResultSet rs = stmtGetWhse.executeQuery()) {
        if (!rs.next()) {
          throw new RuntimeException("W_ID=" + w_id + " not found!");
        }
      }
    }
  }

  private void getCustomer(Connection conn, int w_id, int d_id, int c_id) throws SQLException {
    try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, stmtGetCustSQL)) {
      stmtGetCust.setInt(1, w_id);
      stmtGetCust.setInt(2, d_id);
      stmtGetCust.setInt(3, c_id);
      try (ResultSet rs = stmtGetCust.executeQuery()) {
        if (!rs.next()) {
          throw new RuntimeException("C_D_ID=" + d_id + " C_ID=" + c_id + " not found!");
        }
      }
    }
  }
}
