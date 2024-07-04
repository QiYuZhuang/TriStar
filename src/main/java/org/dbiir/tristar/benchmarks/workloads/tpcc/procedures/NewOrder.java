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
   import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
   import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
   import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
   import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
   import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
   import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Stock;
   import org.dbiir.tristar.common.CCType;
   import org.dbiir.tristar.common.LockType;
   import org.dbiir.tristar.transaction.concurrency.LockTable;
   import org.slf4j.Logger;
   import org.slf4j.LoggerFactory;
 
   import java.sql.*;
   import java.util.Random;
   import java.util.Arrays;
 
   public class NewOrder extends TPCCProcedure {
 
     private static final Logger LOG = LoggerFactory.getLogger(NewOrder.class);
 
     private int release_cnt;
 
     public final SQLStmt stmtGetCustSQL =
             new SQLStmt(
                     """
                   SELECT C_INFO, vid
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
                   SELECT W_INFO, vid
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
                   SELECT D_NEXT_O_ID, D_TAX
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
                             .formatted(TPCCConstants.TABLENAME_DISTRICT, TPCCConstants.TABLENAME_DISTRICT));
 
 
     public final SQLStmt stmtInsertOOrderSQL =
             new SQLStmt(
                     """
                   INSERT INTO %s
                    (O_W_ID, O_D_ID, O_ID, O_C_ID, O_STATUS, vid)
                    VALUES (?, ?, ?, ?, ?, 0)
               """
                             .formatted(TPCCConstants.TABLENAME_OPENORDER));
 
     public final SQLStmt    stmtUpdateOOrderSQL =
             new SQLStmt(
                     """
                   UPDATE %s
                    SET o_status = 'created'
                    WHERE O_W_ID = ? AND O_D_ID = ? AND O_ID = ?
               """
                             .formatted(TPCCConstants.TABLENAME_OPENORDER));
 
     public final SQLStmt stmtUpdateStockSQL =
             new SQLStmt(
                     """
                   UPDATE %s
                      SET S_QUANTITY = S_QUANTITY - ?, vid = vid + 1
                    WHERE S_I_ID = ?
                      AND S_W_ID = ?
                    RETURNING vid
               """
                             .formatted(TPCCConstants.TABLENAME_STOCK));
 
     public final SQLStmt stmtInsertOrderLineSQL =
             new SQLStmt(
                     """
                   INSERT INTO %s
                    (OL_W_ID, OL_D_ID, OL_O_ID, ol_number, OL_I_ID, OL_DELIVERY_INFO, OL_QUANTITY, vid)
                    VALUES (?,?,?,?,?,?,?,0)
               """
                             .formatted(TPCCConstants.TABLENAME_ORDERLINE));
 
     public final SQLStmt stmtUpdateOrderLineSQL =
             new SQLStmt(
                     """
                   UPDATE %s
                     SET OL_DELIVERY_INFO = 'created'
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
 
     public void run(
             Connection conn,
             Random gen,
             int terminalWarehouseID,
             int numWarehouses,
             int terminalDistrictLowerID,
             int terminalDistrictUpperID,
             CCType ccType,
             double zipftheta,
             ZipfianGenerator iditer,
             long tid,
             long[] versions,
             long[] keys,
             TPCCWorker w)
             throws SQLException {
 
       int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
       int customerID;
       if (zipftheta > -1.0) {
         customerID = iditer.nextInt();
       } else {
         customerID = TPCCUtil.getCustomerID(gen);
       }
 
       int numItems = TPCCUtil.randomNumber(5, 15, gen);
       int numItems2 = TPCCUtil.randomNumber(5, 15, gen);
       int[] itemIDs = new int[numItems];
       int[] itemIDs2 = new int[numItems2];
       int[] orderQuantities = new int[numItems];
       int[] orderQuantities2 = new int[numItems2];
 
 
       for (int i = 0; i < numItems; i++) {
         itemIDs[i] = TPCCUtil.getItemID(gen);
         orderQuantities[i] = TPCCUtil.randomNumber(1, 10, gen);
       }
       for (int i = 0; i < numItems2; i++) {
         itemIDs2[i] = TPCCUtil.getItemID(gen);
         orderQuantities2[i] = TPCCUtil.randomNumber(1, 10, gen);
       }
 
       newOrderTransaction(
               terminalWarehouseID,
               districtID,
               customerID,
               numItems,
               numItems2,
               itemIDs,
               orderQuantities,
               itemIDs2,
               orderQuantities2,
               ccType,
               versions,
               keys,
               tid,
               conn);
     }
 
     private void newOrderTransaction(
             int w_id,
             int d_id,
             int c_id,
             int o_ol_cnt1,
             int o_ol_cnt2,
             int[] itemIDs,
             int[] orderQuantities,
             int[] itemIDs2,
             int[] orderQuantities2,
             CCType type,
             long[] versions,
             long[] keys,
             long tid,
             Connection conn)
             throws SQLException {
 
       this.release_cnt = o_ol_cnt1 + o_ol_cnt2;
 
       if (type == CCType.RC_ELT)
         setConflictW(conn,w_id);
       if (type == CCType.RC_ELT | type == CCType.RC_ELT_ATTR) {
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
 
         try (PreparedStatement stmtUpdateConfS = this.getPreparedStatement(conn, stmtUpdateConflictSSQL)) {
           for (int ol_number = 1; ol_number <= o_ol_cnt2; ol_number++) {
             int ol_i_id = itemIDs2[ol_number - 1];
 
             stmtUpdateConfS.setInt(1, w_id);
             stmtUpdateConfS.setInt(2, ol_i_id);
             stmtUpdateConfS.addBatch();
           }
           stmtUpdateConfS.executeBatch();
         }
       }
 
       // R[Warehouse]
       if (type == CCType.SI | type ==CCType.SER | type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR | type == CCType.RC_FOR_UPDATE_ATTR) {
         try (PreparedStatement stmtGetWhse = this.getPreparedStatement(conn, stmtGetWhseSQL)) {
           stmtGetWhse.setInt(1, w_id);
           try (ResultSet rs = stmtGetWhse.executeQuery()) {
             if (!rs.next()) {
                String msg = String.format("No %s for warehouse #%d", TPCCConstants.TABLENAME_WAREHOUSE, w_id);
               throw new UserAbortException(msg);
             }
             if (type == CCType.RC_TAILOR) {
               versions[0] = rs.getLong(2);
             }
           }
         }
       } else if (type == CCType.RC_FOR_UPDATE) {
         updateWarehouse(conn, w_id);
       }
       keys[0] = w_id - 1;
       if (type == CCType.RC_TAILOR) {
         // validate R[warehouse]
         int idx = 0;
         // int validationPhase = 0;
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, tid, keys[0], LockType.SH, type);
         long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_WAREHOUSE, keys[0]);
         if (v >= 0) {
           if (v != versions[0]) {
             String msg = String.format("v1:%d v2:%d Validation failed for warehouse #%d, warehouse, NewOrder", v, versions[0], keys[0] + 1);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
             throw new SQLException(msg, "500");
           }
         } else {
           try (PreparedStatement getWareStmt = this.getPreparedStatement(conn, stmtGetWhseSQL, keys[0] + 1)) {
             try (ResultSet rs = getWareStmt.executeQuery()) {
               if (!rs.next()) {
                 String msg = String.format("No %s for warehouse #%d", TPCCConstants.TABLENAME_WAREHOUSE, keys[0] + 1);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
                 throw new UserAbortException(msg);
               }
               v = rs.getLong(2);
               LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], v);
               if (v != versions[0]) {
                 String msg = String.format("Validation failed for warehouse #%d, warehouse, NewOrder", keys[0] + 1);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
                 throw new SQLException(msg, "500");
               }
             }
           }
         }
       }
 
       // U[District]
       int d_next_o_id = 3001;
       try {
        d_next_o_id = updateandgetDistrict(conn, w_id, d_id);
       } catch (SQLException ex) {
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
         throw ex;
       }
 
       // R[Customer]
       if (type == CCType.SI | type == CCType.SER | type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR | type == CCType.RC_FOR_UPDATE_ATTR) {
         try{
           getCustomer(conn, w_id, d_id, c_id, type, versions);
         } catch(SQLException ex) {
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
            throw ex;
         }
 
       } else if (type == CCType.RC_FOR_UPDATE) {
         updateCustomer(conn, w_id, d_id, c_id);
       }
       long key1 = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
               + (long) (d_id - 1) * TPCCConfig.configCustPerDist + (long) (c_id - 1));
       keys[1] = key1;
 
       if (type == CCType.RC_TAILOR) {
         // validate R[Customer]
         int idx = 1;
         try{
           LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_CUSTOMER, tid, keys[idx], LockType.SH, type);
         } catch (SQLException ex) {
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
           throw ex;
         }
         long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[idx]);
         int[] customerkey = getCustomerIdsFromIndex((int) keys[idx]);
         if (v >= 0) {
           if (v != versions[1]) {
             String msg = String.format("vi:%d v2:%d Validation failed for customer #%d#%d#%d, customer, NewOrder", v, versions[1], customerkey[0], customerkey[1], customerkey[2]);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[idx-1], LockType.SH);
             throw new SQLException(msg, "500");
           }
         } else {
           try (PreparedStatement getCustStmt = this.getPreparedStatement(conn, stmtGetCustSQL, customerkey[0], customerkey[1], customerkey[2])) {
             try (ResultSet rs = getCustStmt.executeQuery()) {
               if (!rs.next()) {
                 String msg = String.format("Nothing for customer #%d#%d#%d, customer, NewOrder", customerkey[0], customerkey[1], customerkey[2]);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[idx-1], LockType.SH);
                 throw new UserAbortException(msg);
               }
               v = rs.getLong(2);
               LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], v);
               if (v != versions[1]) {
                 String msg = String.format("Validation failed for customer #%d#%d#%d, customer, NewOrder", customerkey[0], customerkey[1], customerkey[2]);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[idx-1], LockType.SH);
                 throw new SQLException(msg, "500");
               }
             }
           }
         }
       }
 
       // U[Orders]
       d_next_o_id = (d_next_o_id - 1) % 3000 + 3001;
       try {
         updateOpenOrder(conn, w_id, d_id, (c_id + 3000));
       } catch (SQLException ex) {
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.SH);
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
            throw ex;
       }
 
       int index = 2;
       try (PreparedStatement stmtUpdateStock = this.getPreparedStatement(conn, stmtUpdateStockSQL);
            PreparedStatement stmtInsertOrderLine =
                    this.getPreparedStatement(conn, stmtUpdateOrderLineSQL)) {
 
         for (int ol_number = 1; ol_number <= o_ol_cnt1; ol_number++) {
           int ol_i_id = itemIDs[ol_number - 1];
           int ol_quantity = orderQuantities[ol_number - 1];
 
 
           stmtUpdateStock.setInt(1, ol_quantity);
           stmtUpdateStock.setInt(2, ol_i_id);
           stmtUpdateStock.setInt(3, w_id);
           stmtUpdateStock.addBatch();
           keys[index++] = ((long) (w_id - 1) * TPCCConfig.configItemCount + (long) (ol_i_id - 1));
 
           stmtInsertOrderLine.setInt(3, d_next_o_id);
           stmtInsertOrderLine.setInt(2, d_id);
           stmtInsertOrderLine.setInt(1, w_id);
           stmtInsertOrderLine.setInt(4, ol_number);
           stmtInsertOrderLine.addBatch();
 
         }
 
         stmtUpdateStock.executeBatch();
         stmtUpdateStock.clearBatch();
 
         stmtInsertOrderLine.executeBatch();
         stmtInsertOrderLine.clearBatch();
       } catch (SQLException ex) {
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.SH);
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
            throw ex;
       }
        /*
        try (PreparedStatement stmtUpdateStock2 = this.getPreparedStatement(conn, stmtUpdateStockSQL);
             PreparedStatement stmtInsertOrderLine2 =
                     this.getPreparedStatement(conn, stmtUpdateOrderLineSQL)) {
 
          for (int ol_number = 1; ol_number <= o_ol_cnt2; ol_number++) {
            int ol_i_id = itemIDs2[ol_number - 1];
            int ol_quantity = orderQuantities2[ol_number - 1];
 
 
            stmtUpdateStock2.setInt(1, ol_quantity);
            stmtUpdateStock2.setInt(2, ol_i_id);
            stmtUpdateStock2.setInt(3, w_id);
            // keys[index++] = ((long) (w_id - 1) * TPCCConfig.configItemCount + (long) (ol_i_id - 1));
            stmtUpdateStock2.addBatch();
 
            stmtInsertOrderLine2.setInt(3, d_next_o_id);
            stmtInsertOrderLine2.setInt(2, d_id);
            stmtInsertOrderLine2.setInt(1, w_id);
            stmtInsertOrderLine2.setInt(4, ol_number + o_ol_cnt1);
            stmtInsertOrderLine2.addBatch();
 
          }
          stmtUpdateStock2.executeBatch();
          stmtUpdateStock2.clearBatch();
 
          stmtInsertOrderLine2.executeBatch();
          stmtInsertOrderLine2.clearBatch();
 
        }
        */
         /*
         for (int ol_number = 1; ol_number <= o_ol_cnt1; ol_number++) {
           try (PreparedStatement stmtUpdateStock = this.getPreparedStatement(conn, stmtUpdateStockSQL)
           ) {
             int ol_i_id = itemIDs[ol_number - 1];
             int ol_quantity = orderQuantities[ol_number - 1];
 
             // U1[Stock]
             stmtUpdateStock.setInt(1, ol_quantity);
             stmtUpdateStock.setInt(2, ol_i_id);
             stmtUpdateStock.setInt(3, w_id);
             keys[index++] = ((long) (w_id - 1) * TPCCConfig.configItemCount + (long) (ol_i_id - 1));
             try (ResultSet rs = stmtUpdateStock.executeQuery()) {
               if (!rs.next()) {
                 String msg = String.format("No w_id:%s i:id %s for NewOrder UpdateStock", w_id, ol_i_id);
                 throw new UserAbortException(msg);
               }
             }
           }
 
           try (PreparedStatement stmtInsertOrderLine =
                        this.getPreparedStatement(conn, stmtUpdateOrderLineSQL)) {
             // U1[OrderLine]
             stmtInsertOrderLine.setInt(3, d_next_o_id);
             stmtInsertOrderLine.setInt(2, d_id);
             stmtInsertOrderLine.setInt(1, w_id);
             stmtInsertOrderLine.setInt(4, ol_number);
             int res = stmtInsertOrderLine.executeUpdate();
             if (res == 0) {
               String msg = String.format("No w_id:%d d_id%d o_id%d ol_number%d for NewOrder InsertOrderLine", w_id, d_id, d_next_o_id, ol_number);
               throw new UserAbortException(msg);
             }
           }
         }
 
          try (PreparedStatement stmtUpdateStock2 = this.getPreparedStatement(conn, stmtUpdateStockSQL);
               PreparedStatement stmtInsertOrderLine2 =
                       this.getPreparedStatement(conn, stmtUpdateOrderLineSQL)) {
 
            for (int ol_number = 1; ol_number <= o_ol_cnt2; ol_number++) {
              int ol_i_id = itemIDs2[ol_number - 1];
              int ol_quantity = orderQuantities2[ol_number - 1];
 
              // U2[Stock]
              stmtUpdateStock2.setInt(1, ol_quantity);
              stmtUpdateStock2.setInt(2, ol_i_id);
              stmtUpdateStock2.setInt(3, w_id);
              keys[index++] = ((long) (w_id - 1) * TPCCConfig.configItemCount + (long) (ol_i_id - 1));
              try (ResultSet rs = stmtUpdateStock2.executeQuery()) {
                if (!rs.next()) {
                  String msg = String.format("No w_id:%s i:id %s for NewOrder UpdateStock", w_id, ol_i_id);
                  throw new UserAbortException(msg);
                }
              }
              // U2[OrderLine]
              stmtInsertOrderLine2.setInt(3, d_next_o_id);
              stmtInsertOrderLine2.setInt(2, d_id);
              stmtInsertOrderLine2.setInt(1, w_id);
              stmtInsertOrderLine2.setInt(4, o_ol_cnt1+ol_number);
              int res = stmtInsertOrderLine2.executeUpdate();
              if (res == 0) {
                String msg = String.format("No w_id:%d d_id%d o_id%d ol_number%d for NewOrder InsertOrderLine", w_id, d_id, d_next_o_id, ol_number + o_ol_cnt1);
                throw new UserAbortException(msg);
              }
            }
          }
          */
         /*
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
           // lock STOCK and ORDERLINE first
           int idx = 2;
           int validationPhase = 1;
 
           long[] lock_list = new long[keys.length]; // 创建一个新的数组对象
           System.arraycopy(keys, 0, lock_list, 0, keys.length); // 复制元素
 
           Arrays.sort(lock_list, idx, o_ol_cnt1 + o_ol_cnt2 + idx - 1);
           for (int i = 0; i < o_ol_cnt1; i++) {
             try {
               LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_STOCK, tid, lock_list[idx], LockType.EX, type);
               validationPhase++;
               idx++;
             } catch (SQLException ex) {
               releaseTailorLock(validationPhase, lock_list);
               throw ex;
             }
           }
           for (int i = 0; i < o_ol_cnt2; i++) {
             try {
               LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_STOCK, tid, lock_list[idx], LockType.EX, type);
               validationPhase++;
               idx++;
             } catch (SQLException ex) {
               releaseTailorLock(validationPhase, lock_list);
               throw ex;
             }
           }
         }
         */
     }
 
 
 
 
 
     private void updateOpenOrder(
             Connection conn, int w_id, int d_id, int c_id)
             throws SQLException {
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
 
     private int updateandgetDistrict(Connection conn, int w_id, int d_id) throws SQLException {
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
 
     private void getWarehouse(Connection conn, int w_id, CCType type, long[] versions) throws SQLException {
       try (PreparedStatement stmtGetWhse = this.getPreparedStatement(conn, stmtGetWhseSQL)) {
         stmtGetWhse.setInt(1, w_id);
         try (ResultSet rs = stmtGetWhse.executeQuery()) {
           if (!rs.next()) {
             throw new RuntimeException("W_ID=" + w_id + " not found!");
           }
           if (type == CCType.RC_TAILOR) {
             versions[0] = rs.getLong(2);
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
 
     private void getCustomer(Connection conn, int w_id, int d_id, int c_id, CCType type, long[] versions) throws SQLException {
       try (PreparedStatement stmtGetCust = this.getPreparedStatement(conn, stmtGetCustSQL)) {
         stmtGetCust.setInt(1, w_id);
         stmtGetCust.setInt(2, d_id);
         stmtGetCust.setInt(3, c_id);
         try (ResultSet rs = stmtGetCust.executeQuery()) {
           if (!rs.next()) {
             throw new RuntimeException("C_D_ID=" + d_id + " C_ID=" + c_id + " not found!");
           }
           if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR){
             versions[1] = rs.getLong(2);
           }
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
 
 
     public static int[] getCustomerIdsFromIndex(int index) {
       int w_id = (index / 30000) + 1;
       int d_id = ((index % 30000) / 3000) + 1;
       int c_id = (index % 3000) + 1;
       return new int[]{w_id, d_id, c_id};
     }
 
     private void releaseTailorLock(int phase, long[] keys) {
 
       if (phase == 1) {
         // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
         // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.SH);
       } else {
         int idx = 2;
         // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
         // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.SH);
         for (int i = 0; i < phase - 1; i++) {
           // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[idx++], LockType.EX);
         }
       }
     }
 
     public void doAfterCommit(long[] keys, CCType type, boolean success, long[] versions) {
       if (!success)
         return;
       if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
 
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.SH);
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.SH);
           /*
           int idx = 2;
           for (int i = 0; i < this.release_cnt; i++) {
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[idx++], LockType.EX);
           }
           // update the
           idx = 2;
           for (int i = 0; i < this.release_cnt; i++) {
             LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_STOCK, keys[idx], versions[idx++]);
           }
           */
       }
     }
 
   }
 