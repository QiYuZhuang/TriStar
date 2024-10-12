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

 import org.dbiir.tristar.adapter.TAdapter;
 import org.dbiir.tristar.adapter.TransactionCollector;
 import org.dbiir.tristar.benchmarks.api.SQLStmt;
 import org.dbiir.tristar.benchmarks.catalog.RWRecord;
 import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
 import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
 import org.dbiir.tristar.common.CCType;
 import org.dbiir.tristar.common.LockType;
 import org.dbiir.tristar.transaction.concurrency.LockTable;
 
 public class Delivery extends TPCCProcedure {
 
   public SQLStmt delivUpdateOrderStatusSQL =
       new SQLStmt(
           """
         UPDATE %s
            SET O_STATUS = ?, vid = vid + 1
          WHERE O_ID = ?
            AND O_D_ID = ?
            AND O_W_ID = ?
            RETURNING vid
     """
               .formatted(TPCCConstants.TABLENAME_OPENORDER));
 
   public SQLStmt delivUpdateDeliveryInfoSQL =
           new SQLStmt(
                   """
                 UPDATE %s
                    SET ol_delivery_info = ?, vid = vid + 1
                  WHERE OL_O_ID = ?
                    AND OL_D_ID = ?
                    AND OL_W_ID = ?
                    RETURNING vid
             """
                           .formatted(TPCCConstants.TABLENAME_ORDERLINE));
 
   public SQLStmt delivUpdateCustBalSQL =
       new SQLStmt(
           """
         UPDATE %s
            SET C_BALANCE = C_BALANCE - ?, vid = vid + 1
          WHERE C_W_ID = ?
            AND C_D_ID = ?
            AND C_ID = ?
            RETURNING vid
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
       long tid,
       long[] versions,
       long[] keys,
       TPCCWorker w)
       throws SQLException {
 
 
 
     int d_id1 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
 
     int customerId;
     customerId = TPCCUtil.getCustomerID(gen);

     int no_o_id1 = customerId;
     
     if (ccType == CCType.RC_ELT) {
       setConflictC(conn, w_id, d_id1, customerId);
     }
 
     // Orders
     updateOrderStatus(conn, w_id, d_id1, no_o_id1, versions, ccType);
     
     // Orderline
     updateDeliveryInfo(conn, w_id, d_id1, no_o_id1, versions, ccType);
 
     float orderLineTotal = (float) (TPCCUtil.randomNumber(100, 500000, gen) / 100.0);
 
     // Customer
     updateBalance(conn, w_id, d_id1, customerId, orderLineTotal, versions, ccType);
 
     if (ccType == CCType.RC_TAILOR) {
       int validationPhase;

       long key1 = ((long) (w_id - 1) * (long) (TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist)
               + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (no_o_id1 - 1));
       long key2 = ((long) (w_id - 1) * (long) (TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist)
               + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (customerId - 1));

       keys[0] = key1;
       keys[1] = key2;

       LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_OPENORDER, tid, key1, LockType.EX, ccType);
       validationPhase = 1;

       try {
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_ORDERLINE, tid, key1, LockType.EX, ccType);
         validationPhase = 2;
       } catch (SQLException ex) {
         releaseTailorLock(validationPhase, key1);
         throw ex;
       }

       try {
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_CUSTOMER, tid, key2, LockType.EX, ccType);
       } catch (SQLException ex) {
         releaseTailorLock(validationPhase, key1);
         throw ex;
       }
     }
   }
 
   private void updateOrderStatus(Connection conn, int w_id, int d_id, int no_o_id, long[] versions, CCType type)
           throws SQLException {
 
     try (PreparedStatement delivUpdateCarrierId =
                  this.getPreparedStatement(conn, delivUpdateOrderStatusSQL)) {
       delivUpdateCarrierId.setString(1, "delivered");
       delivUpdateCarrierId.setInt(2, no_o_id);
       delivUpdateCarrierId.setInt(3, d_id);
       delivUpdateCarrierId.setInt(4, w_id);
 
       try(ResultSet res = delivUpdateCarrierId.executeQuery()) {
         if (!res.next()) {
           String msg =
                   String.format(
                           "Failed to update ORDER record [W_ID=%d, D_ID=%d, O_ID=%d]", w_id, d_id, no_o_id);
           throw new RuntimeException(msg);
         }
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR)
           versions[0] = res.getLong(1);
       }
     }
   }
 
   private void updateDeliveryInfo(Connection conn, int w_id, int d_id, int no_o_id, long[] versions, CCType type)
           throws SQLException {
 
     try (PreparedStatement delivUpdateDeliveryDate =
                  this.getPreparedStatement(conn, delivUpdateDeliveryInfoSQL)) {
       delivUpdateDeliveryDate.setString(1, "delivered");
       delivUpdateDeliveryDate.setInt(2, no_o_id);
       delivUpdateDeliveryDate.setInt(3, d_id);
       delivUpdateDeliveryDate.setInt(4, w_id);
 
       try(ResultSet res = delivUpdateDeliveryDate.executeQuery()) {
         if (!res.next()) {
           String msg =
                   String.format(
                           "Failed to update ORDER_LINE records [W_ID=%d, D_ID=%d, O_ID=%d]",
                           w_id, d_id, no_o_id);
           throw new RuntimeException(msg);
         }
         if (type == CCType.RC_TAILOR)
           versions[1] = res.getLong(1);
       }
     }
   }
 
 
   private void updateBalance(
       Connection conn, int w_id, int d_id, int c_id, float orderLineTotal, long[] versions, CCType type) throws SQLException {
 
     try (PreparedStatement delivUpdateCustBalDelivCnt =
         this.getPreparedStatement(conn, delivUpdateCustBalSQL)) {
       delivUpdateCustBalDelivCnt.setBigDecimal(1, BigDecimal.valueOf(orderLineTotal));
       delivUpdateCustBalDelivCnt.setInt(2, w_id);
       delivUpdateCustBalDelivCnt.setInt(3, d_id);
       delivUpdateCustBalDelivCnt.setInt(4, c_id);
 
       try(ResultSet res = delivUpdateCustBalDelivCnt.executeQuery()) {
         if (!res.next()) {
           String msg =
                   String.format(
                           "Failed to update CUSTOMER record [W_ID=%d, D_ID=%d, C_ID=%d]", w_id, d_id, c_id);
           throw new RuntimeException(msg);
         }
         if (type == CCType.RC_TAILOR)
           versions[2] = res.getLong(1);
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
 
   private void releaseTailorLock(int phase, long key1) {
 
     if (phase == 1) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, key1, LockType.EX);
     } else if (phase == 2) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, key1, LockType.EX);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, key1, LockType.EX);
     }
   }
 
   public void doAfterCommit(long key1, long key2, CCType type, boolean success, long[] versions, long latency) {
     if (TransactionCollector.getInstance().isSample()) {
       TransactionCollector.getInstance().addTransactionSample(TAdapter.getInstance().getTypesByName("Delivery").getId(),
               new RWRecord[]{new RWRecord(1, TPCCConstants.TABLENAME_TO_INDEX.get(TPCCConstants.TABLENAME_OPENORDER), (int) key1)},
               new RWRecord[]{new RWRecord(2, TPCCConstants.TABLENAME_TO_INDEX.get(TPCCConstants.TABLENAME_ORDERLINE), (int) key1)},
               new RWRecord[]{new RWRecord(3, TPCCConstants.TABLENAME_TO_INDEX.get(TPCCConstants.TABLENAME_CUSTOMER), (int) key2)},
               success?1:0, latency);
     }

     if (!success)
       return;
     if (type == CCType.RC_TAILOR) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, key2, LockType.EX);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, key1, LockType.EX);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, key1, LockType.EX);

       // update the
       LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_OPENORDER, key1, versions[0]);
       LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, key1, versions[1]);
       LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, key2, versions[2]);
     }
   }
 
 }
 