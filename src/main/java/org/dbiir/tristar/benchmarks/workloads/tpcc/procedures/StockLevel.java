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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class StockLevel extends TPCCProcedure {

  private static final Logger LOG = LoggerFactory.getLogger(StockLevel.class);

  public SQLStmt stockGetDistOrderIdSQL =
      new SQLStmt(
          """
        SELECT D_NEXT_O_ID
          FROM  %s
         WHERE D_W_ID = ?
           AND D_ID = ?
    """
              .formatted(TPCCConstants.TABLENAME_DISTRICT));

  public SQLStmt stockGetStockSQL =
      new SQLStmt(
          """
        SELECT S_QUANTITY
         FROM  %s
         WHERE s_w_id = ?
         AND s_i_id = ?
    """
              .formatted(TPCCConstants.TABLENAME_STOCK));

  public void run(
      Connection conn,
      Random gen,
      int w_id,
      int numWarehouses,
      int terminalDistrictLowerID,
      int terminalDistrictUpperID,
      TPCCWorker w)
      throws SQLException {

    int i_id = TPCCUtil.getItemID(gen);


    int stock_quantity = getStock(conn, w_id, i_id);

    if (LOG.isTraceEnabled()) {
      String terminalMessage =
          "\n+-------------------------- STOCK-LEVEL --------------------------+"
              + "\n Warehouse: "
              + w_id
              + "\n Item:  "
              + i_id
              + "\n Stock Quantity:       "
              + stock_quantity
              + "\n+-----------------------------------------------------------------+\n\n";
      LOG.trace(terminalMessage);
    }
  }

  private int getOrderId(Connection conn, int w_id, int d_id) throws SQLException {
    try (PreparedStatement stockGetDistOrderId =
        this.getPreparedStatement(conn, stockGetDistOrderIdSQL)) {
      stockGetDistOrderId.setInt(1, w_id);
      stockGetDistOrderId.setInt(2, d_id);

      try (ResultSet rs = stockGetDistOrderId.executeQuery()) {

        if (!rs.next()) {
          throw new RuntimeException("D_W_ID=" + w_id + " D_ID=" + d_id + " not found!");
        }
        return rs.getInt("D_NEXT_O_ID");
      }
    }
  }

  private int getStock(Connection conn, int w_id, int i_id)
      throws SQLException {
    try (PreparedStatement stockGetCountStock =
        this.getPreparedStatement(conn, stockGetStockSQL)) {
      stockGetCountStock.setInt(1, w_id);
      stockGetCountStock.setInt(2, i_id);

      try (ResultSet rs = stockGetCountStock.executeQuery()) {
        if (!rs.next()) {
          String msg =
              String.format(
                  "Failed to get StockLevel result for query [W_ID=%d, I_ID=%d]",
                  w_id, i_id);

          throw new RuntimeException(msg);
        }

        return rs.getInt("S_QUANTITY");
      }
    }
  }
}