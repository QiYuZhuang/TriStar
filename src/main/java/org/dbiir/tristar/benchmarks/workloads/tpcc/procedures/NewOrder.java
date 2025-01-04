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

  import java.sql.*;
  import java.util.Random;
  import java.util.HashMap;
  import java.util.LinkedList;
  import java.util.List;

  import org.dbiir.tristar.benchmarks.api.Worker;
  import org.dbiir.tristar.benchmarks.util.StringUtil;
  import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;

  import java.sql.Connection;
  import java.sql.PreparedStatement;
  import java.sql.ResultSet;
  import java.sql.SQLException;
  import java.util.Arrays;
  import java.util.Random;

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

    public final SQLStmt stmtUpdateCustSQL =
            new SQLStmt(
                    """
                  UPDATE %s
                  SET C_INFO = C_INFO
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

    public final SQLStmt stmtUpdateWhseSQL =
            new SQLStmt(
                    """
                  UPDATE %s
                  SET W_INFO = W_INFO
                   WHERE W_ID = ?
              """
                            .formatted(TPCCConstants.TABLENAME_WAREHOUSE));

    public final SQLStmt stmtGetDistSQL =
            new SQLStmt(
                    """
                  SELECT D_NEXT_O_ID
                    FROM %s
                   WHERE D_W_ID = ? AND D_ID = ? FOR UPDATE
              """
                            .formatted(TPCCConstants.TABLENAME_DISTRICT));


    public final SQLStmt stmtUpdateAndGetDistSQL =
            new SQLStmt(
                    """
                  UPDATE %s
                     SET D_NEXT_O_ID = D_NEXT_O_ID + 1
                   WHERE D_W_ID = ?
                     AND D_ID = ?
                   RETURNING D_NEXT_O_ID, D_INFO;
              """
                            .formatted(TPCCConstants.TABLENAME_DISTRICT));


    public final SQLStmt stmtInsertOOrderSQL =
            new SQLStmt(
                    """
                  INSERT INTO %s
                   (O_W_ID, O_D_ID, O_ID, O_C_ID, O_STATUS, vid)
                   VALUES (?, ?, ?, ?, ?, 0)
              """
                            .formatted(TPCCConstants.TABLENAME_OPENORDER));

    public final SQLStmt  stmtUpdateOOrderSQL =
            new SQLStmt(
                    """
                  UPDATE %s
                   SET o_status = 'renewed'
                   WHERE O_W_ID = ? AND O_D_ID = ? AND O_ID = ?
              """
                            .formatted(TPCCConstants.TABLENAME_OPENORDER));

    public final SQLStmt stmtUpdateStockSQL =
            new SQLStmt(
                    """
                  UPDATE %s
                     SET S_QUANTITY = S_QUANTITY - ?
                   WHERE S_I_ID = ?
                     AND S_W_ID = ?
              """
                            .formatted(TPCCConstants.TABLENAME_STOCK));

    public final SQLStmt stmtUpdateOrderLineSQL =
            new SQLStmt(
                    """
                  UPDATE %s
                    SET OL_DELIVERY_INFO = 'renewed'
                   WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ? AND OL_NUMBER = ?
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

    public final SQLStmt stmtUpdateConflictWSQL =
            new SQLStmt(
                    """
                  SELECT *
                   FROM %s
                   WHERE W_ID = ?
                   FOR UPDATE
              """
                            .formatted(TPCCConstants.TABLENAME_CONFLICT_WAREHOUSE));

    public final SQLStmt stmtUpdateConflictSSQL =
            new SQLStmt(
                    """
                  SELECT *
                   FROM %s
                   WHERE ol_w_id = ?
                     AND ol_i_id = ?
                   FOR UPDATE
              """
                            .formatted(TPCCConstants.TABLENAME_CONFLICT_STOCK));



    static HashMap<Integer, Integer> clientServerIndexMap = new HashMap<>();
    static {
      clientServerIndexMap.put(0, -1);
      clientServerIndexMap.put(1, -1);
      clientServerIndexMap.put(2, -1);
      clientServerIndexMap.put(3, -1);
      clientServerIndexMap.put(4, -1);
      clientServerIndexMap.put(5, -1);
    }
    @Override
    public void updateClientServerIndexMap(int clientSideIndex, int serverSideIndex) {
      clientServerIndexMap.put(clientSideIndex, serverSideIndex);
    }
    @Override
    public List<TemplateSQLMeta> getTemplateSQLMetas() {
      List<TemplateSQLMeta> templateSQLMetas = new LinkedList<>();
      templateSQLMetas.add(new TemplateSQLMeta("NewOrder", 0, TPCCConstants.TABLENAME_WAREHOUSE,
              0, "SELECT W_INFO FROM " + TPCCConstants.TABLENAME_WAREHOUSE + " WHERE W_ID = ?"));
      templateSQLMetas.add(new TemplateSQLMeta("NewOrder", 1, TPCCConstants.TABLENAME_ORDERLINE,
              1, "UPDATE " + TPCCConstants.TABLENAME_DISTRICT + " SET D_NEXT_O_ID = D_NEXT_O_ID + 1 WHERE D_W_ID = ? AND D_ID = ?"));
      templateSQLMetas.add(new TemplateSQLMeta("NewOrder", 0, TPCCConstants.TABLENAME_CUSTOMER,
              2, "SELECT C_INFO FROM " + TPCCConstants.TABLENAME_CUSTOMER +" WHERE C_W_ID = ? AND C_D_ID = ? AND C_ID = ?"));
      templateSQLMetas.add(new TemplateSQLMeta("NewOrder", 1, TPCCConstants.TABLENAME_OPENORDER,
              3, "UPDATE " + TPCCConstants.TABLENAME_OPENORDER + " SET O_STATUS = 'renewed' WHERE O_W_ID = ? AND O_D_ID = ? AND O_ID = ?"));
      templateSQLMetas.add(new TemplateSQLMeta("NewOrder", 1, TPCCConstants.TABLENAME_STOCK,
              4, "UPDATE " + TPCCConstants.TABLENAME_STOCK + " SET S_QUANTITY = S_QUANTITY - ? WHERE S_I_ID = ? AND S_W_ID = ?"));
      templateSQLMetas.add(new TemplateSQLMeta("NewOrder", 1, TPCCConstants.TABLENAME_ORDERLINE,
              5, "UPDATE " + TPCCConstants.TABLENAME_ORDERLINE + " SET OL_DELIVERY_INFO = 'renewed' WHERE OL_W_ID = ? AND OL_D_ID = ? AND OL_O_ID = ? AND OL_NUMBER = ?"));
      return templateSQLMetas;
    }

    public void run(
            Connection conn,
            Random gen,
            int terminalWarehouseID,
            int numWarehouses,
            int terminalDistrictLowerID,
            int terminalDistrictUpperID,
            CCType ccType,
            long[] keys,
            TPCCWorker w)
            throws SQLException {

      int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
      int customerID;
      customerID = TPCCUtil.getCustomerID(gen);

      int numItems = TPCCUtil.randomNumber(5, 15, gen);
      int[] itemIDs = new int[numItems];
      int[] orderQuantities = new int[numItems];

      for (int i = 0; i < numItems; i++) {
        itemIDs[i] = TPCCUtil.getItemID(gen);
        orderQuantities[i] = TPCCUtil.randomNumber(1, 10, gen);
      }

      newOrderTransaction(terminalWarehouseID, districtID, customerID, numItems, itemIDs, orderQuantities, ccType,
              keys, conn, w);
    }

    private void newOrderTransaction(
            int w_id,
            int d_id,
            int c_id,
            int o_ol_cnt1,
            int[] itemIDs,
            int[] orderQuantities,
            CCType type,
            long[] keys,
            Connection conn,
            Worker worker)
            throws SQLException {

      w_id = w_id % 18 + 15;

      if (type == CCType.RC_ELT)
        setConflictW(conn,w_id);
      if (type == CCType.RC_ELT) {
        setConflictC(conn, w_id, d_id, c_id);

        try (PreparedStatement stmtUpdateConfS = this.getPreparedStatement(conn, stmtUpdateConflictSSQL)) {
          for (int ol_number = 1; ol_number <= o_ol_cnt1; ol_number++) {
            int ol_i_id = itemIDs[ol_number - 1];

            stmtUpdateConfS.setInt(1, w_id);
            stmtUpdateConfS.setInt(2, ol_i_id);
            stmtUpdateConfS.addBatch();
          }
          stmtUpdateConfS.executeBatch();
        }
      }

      // R[Warehouse]
      if (worker.useTxnSailsServer()) {
        try {
          worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "NewOrder", 0, w_id));
          worker.parseExecutionResults();
        } catch (InterruptedException e) {
          System.out.println("InterruptedException on sending or receiving message");
        }
      } else if (type == CCType.RC_FOR_UPDATE) {
        updateWarehouse(conn, w_id);
      } else {
        try (PreparedStatement stmtGetWhse = this.getPreparedStatement(conn, stmtGetWhseSQL)) {
          stmtGetWhse.setInt(1, w_id);
          try (ResultSet rs = stmtGetWhse.executeQuery()) {
            if (!rs.next()) {
              String msg = String.format("No %s for warehouse #%d", TPCCConstants.TABLENAME_WAREHOUSE, w_id);
              throw new UserAbortException(msg);
            }
          }
        }
      }

      keys[0] = w_id - 1;

      // U[District]
      int d_next_o_id = 3001;
      d_next_o_id = updateAndGetDistrict(worker, conn, w_id, d_id);

      // R[Customer]
      if (worker.useTxnSailsServer()) {
        try {
          worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "NewOrder", 2, w_id, d_id, c_id));
          worker.parseExecutionResults();
        } catch (InterruptedException e) {
          System.out.println("InterruptedException on sending or receiving message");
        }
      } else if (type == CCType.RC_FOR_UPDATE) {
        updateCustomer(conn, w_id, d_id, c_id);
      } else {
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

      long key1 = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
              + (long) (d_id - 1) * TPCCConfig.configCustPerDist + (long) (c_id - 1));
      keys[1] = key1;

      keys[2] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
              + (long) (d_id - 1) * TPCCConfig.configCustPerDist);

      // U[Orders]
      d_next_o_id = (d_next_o_id - 1) % 3000 + 3001;
      updateOpenOrder(worker, conn, w_id, d_id, (c_id + 3000));

      // U[STOCK], U[ORDERLINE]
      Arrays.sort(itemIDs);
      for (int ol_number = 1; ol_number <= o_ol_cnt1; ol_number++) {
        int ol_i_id = itemIDs[ol_number - 1];
        int ol_quantity = orderQuantities[ol_number - 1];
        if (worker.useTxnSailsServer()) {
          try {
            worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "NewOrder", 4, ol_quantity, ol_i_id, w_id));
            worker.parseExecutionResults();
            worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "NewOrder", 5, w_id, d_id, d_next_o_id, ol_number));
            worker.parseExecutionResults();
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
        } else {
          PreparedStatement stmtUpdateStock = this.getPreparedStatement(conn, stmtUpdateStockSQL);
          PreparedStatement stmtUpdateOrderLine = this.getPreparedStatement(conn, stmtUpdateOrderLineSQL);
          stmtUpdateStock.setInt(1, ol_quantity);
          stmtUpdateStock.setInt(2, ol_i_id);
          stmtUpdateStock.setInt(3, w_id);
          int status = stmtUpdateStock.executeUpdate();

          stmtUpdateOrderLine.setInt(1, w_id);
          stmtUpdateOrderLine.setInt(2, d_id);
          stmtUpdateOrderLine.setInt(3, d_next_o_id);
          stmtUpdateOrderLine.setInt(4, ol_number);
          status = stmtUpdateOrderLine.executeUpdate();
        }
      }
    }

    private void updateOpenOrder(Worker worker, Connection conn, int w_id, int d_id, int c_id)
            throws SQLException {
      if (worker.useTxnSailsServer()) {
        try {
          worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "NewOrder", 3, w_id, d_id, c_id));
          worker.parseExecutionResults();
        } catch (InterruptedException e) {
          System.out.println("InterruptedException on sending or receiving message");
        }
      } else {
        try (PreparedStatement stmtInsertOOrder =
                     this.getPreparedStatement(conn, stmtUpdateOOrderSQL)) {
          stmtInsertOOrder.setInt(1, w_id);
          stmtInsertOOrder.setInt(2, d_id);
          stmtInsertOOrder.setInt(3, c_id);

          int result = stmtInsertOOrder.executeUpdate();

          if (result == 0) {
            LOG.warn("open order not inserted, %d %d %d".formatted(w_id, d_id, c_id));
          }
        }
      }
    }

    private int updateAndGetDistrict(Worker worker, Connection conn, int w_id, int d_id) throws SQLException {
      if (worker.useTxnSailsServer()) {
        int next_oid = 0;
        try {
          worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "NewOrder", 1, w_id, d_id));
          List<List<String>> results = worker.parseExecutionResults();
          next_oid = Integer.parseInt(results.get(0).get(0));
        } catch (InterruptedException e) {
          System.out.println("InterruptedException on sending or receiving message");
        }
        return next_oid;
      } else {
        try (PreparedStatement stmtUpdateAndGetDist = this.getPreparedStatement(conn, stmtUpdateAndGetDistSQL)) {
          stmtUpdateAndGetDist.setInt(1, w_id);
          stmtUpdateAndGetDist.setInt(2, d_id);

          try(ResultSet res = stmtUpdateAndGetDist.executeQuery()) {
            if (!res.next()) {
              throw new RuntimeException(
                      "Error!! Cannot update next_order_id on district for D_ID=" + d_id + " D_W_ID=" + w_id);
            }
            return res.getInt(1);
          }
        }
      }
    }

    private void updateWarehouse(Connection conn, int w_id) throws SQLException {
      try (PreparedStatement stmtGetWhse = this.getPreparedStatement(conn, stmtUpdateWhseSQL)) {
        stmtGetWhse.setInt(1, w_id);
        int rs = stmtGetWhse.executeUpdate();
        if (rs == 0) {
          throw new RuntimeException("W_ID=" + w_id + " not found!");
        }
      }
    }

    private void updateCustomer(Connection conn, int w_id, int d_id, int c_id) throws SQLException {
      try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, stmtUpdateCustSQL)) {
        stmtGetCust.setInt(1, w_id);
        stmtGetCust.setInt(2, d_id);
        stmtGetCust.setInt(3, c_id);
        int rs = stmtGetCust.executeUpdate();
        if (rs == 0) {
          throw new RuntimeException("C_D_ID=" + d_id + " C_ID=" + c_id + " not found!");
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


    public int[] getCustomerIdsFromIndex(int index) {
      int w_id = (index / 30000) + 1;
      int d_id = ((index % 30000) / 3000) + 1;
      int c_id = (index % 3000) + 1;
      return new int[]{w_id, d_id, c_id};
    }


    public void doAfterCommit() {
      // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
      // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.SH);
    }
  }
