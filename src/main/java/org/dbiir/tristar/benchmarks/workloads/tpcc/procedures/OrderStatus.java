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
 import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Customer;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.pojo.Oorder;
 import org.dbiir.tristar.common.CCType;
 import org.dbiir.tristar.common.LockType;
 import org.dbiir.tristar.transaction.concurrency.LockTable;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;
 import java.util.concurrent.locks.Lock;
 
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
                 SELECT  c_info, c_balance, vid
                   FROM %s
                  WHERE C_W_ID = ?
                    AND C_D_ID = ?
                    AND C_ID = ?
             """
                           .formatted(TPCCConstants.TABLENAME_CUSTOMER));
 
   public SQLStmt ordStatGetNewestOrdSQL =
           new SQLStmt(
                   """
                 SELECT o_status, vid
                   FROM %s
                   WHERE O_W_ID = ?
                    AND O_D_ID = ?
                    AND O_ID = ?
             """
                           .formatted(TPCCConstants.TABLENAME_OPENORDER));
 
   public SQLStmt ordStatGetOrderLinesSQL =
           new SQLStmt(
                   """
                 SELECT ol_delivery_info, ol_quantity, vid
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
 
   public void run(
           Connection conn,
           Random gen,
           int w_id,
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
 
     int d_id1 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
     // int d_id2 = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
     int c_id;
     if (zipftheta > -1.0) {
       c_id = iditer.nextInt();
     } else {
       c_id = TPCCUtil.getCustomerID(gen);
     }
     int o_id1 = TPCCUtil.randomNumber(1, 3000, gen);
     // int o_id2 = TPCCUtil.randomNumber(1, 3000, gen);
 
     if (ccType == CCType.RC_FOR_UPDATE) {
       updateCustomerById(w_id, d_id1, c_id, conn);
 
       updateOrderDetails(conn, w_id, d_id1, o_id1);
 
       updateOrderLines(conn, w_id, d_id1, o_id1);
       // updateOrderLines(conn, w_id, d_id2, o_id2);
     }
 
     if (ccType == CCType.RC_ELT) {
       setConflictC(conn, w_id, d_id1, c_id);
     }
 
     // R[Customer]
     getCustomerById(w_id, d_id1, c_id, versions, ccType, conn);
     keys[0] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
             + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (long) (c_id - 1));
     // Validate
     if (ccType == CCType.RC_TAILOR | ccType == CCType.RC_TAILOR_ATTR) {
       int idx = 0;
       LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_CUSTOMER, tid, keys[idx], LockType.SH, ccType);
       long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[idx]);
       if (v >= 0) {
         if (v != versions[idx]) {
           String msg = String.format("v1:%d v2:%d Validation failed for customer %d %d %d OrderStatus", v, versions[idx], w_id, d_id1, c_id);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
           throw new SQLException(msg, "500");
         }
       } else {
         try (PreparedStatement getCustStmt = this.getPreparedStatement(conn, ordStatGetCustSQL, w_id, d_id1, c_id)) {
           try (ResultSet rs = getCustStmt.executeQuery()) {
             if (!rs.next()) {
               String msg = String.format("Nothing for customer #%d#%d#%d OrderStatus",  w_id, d_id1, c_id);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
               throw new UserAbortException(msg);
             }
             v = rs.getLong("vid");
             LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], v);
             if (v != versions[idx]) {
               String msg = String.format("Validation failed for customer #%d#%d#%d OrderStatus",  w_id, d_id1, c_id);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
               throw new SQLException(msg, "500");
             }
           }
         }
       }
     }
     // R[Orders]
     try {
        getOrderStatus(w_id, d_id1, o_id1, versions, ccType, conn);
     } catch (SQLException ex) {
        LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[0], LockType.SH);
     }
     
     keys[1] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
             + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (long) (o_id1 - 1));
     
     if (ccType == CCType.RC_TAILOR | ccType == CCType.RC_TAILOR_ATTR) {
       int idx = 1;
       try{
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_OPENORDER, tid, keys[idx], LockType.SH, ccType);
       } catch (SQLException ex) {
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-1], LockType.SH);
       }
       long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_OPENORDER, keys[idx]);
       if (v >= 0) {
         if (v != versions[idx]) {
           String msg = String.format("v1:%d v2:%d Validation failed for Orders %d %d %d OrderStatus", v, versions[idx], w_id, d_id1, o_id1);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-1], LockType.SH);
           throw new SQLException(msg, "500");
         }
       } else {
         try (PreparedStatement getordStmt = this.getPreparedStatement(conn, ordStatGetNewestOrdSQL, w_id, d_id1, o_id1)) {
           try (ResultSet rs = getordStmt.executeQuery()) {
             if (!rs.next()) {
               String msg = String.format("Nothing for orders #%d#%d#%d OrderStatus",  w_id, d_id1, o_id1);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-1], LockType.SH);
               throw new UserAbortException(msg);
             }
             v = rs.getLong("vid");
             LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_OPENORDER, keys[idx], v);
             if (v != versions[idx]) {
               String msg = String.format("Validation failed for Orders %d %d %d OrderStatus", w_id, d_id1, o_id1);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-1], LockType.SH);
               throw new SQLException(msg, "500");
             }
           }
         }
       }
     }
 
     // R[OrderLines]
     try{
        getOrderLines(w_id, d_id1, o_id1, versions, ccType, 2, conn);
     } catch (SQLException ex) {
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[1], LockType.SH);
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[0], LockType.SH);
     }
     keys[2] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
             + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (long) (o_id1 - 1));
 
     if (ccType == CCType.RC_TAILOR | ccType == CCType.RC_TAILOR_ATTR) {
       int idx = 2;
       // validationPhase = 2;
       try {
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_ORDERLINE, tid, keys[idx], LockType.SH, ccType);
       } catch (SQLException ex) {
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-1], LockType.SH);
         LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-2], LockType.SH);
       }
       long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, keys[idx]);
       if (v >= 0) {
         if (v != versions[idx]) {
           String msg = String.format("v1:%d v2:%d  Validation failed for OrderLine1 %d %d %d OrderStatus", v, versions[idx], w_id, d_id1, o_id1);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-1], LockType.SH);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-2], LockType.SH);
           throw new SQLException(msg, "500");
         }
       } else {
         try (PreparedStatement getordStmt = this.getPreparedStatement(conn, ordStatGetOrderLinesSQL, w_id, d_id1, o_id1)) {
           try (ResultSet rs = getordStmt.executeQuery()) {
             if (!rs.next()) {
               String msg = String.format("Nothing for OrderLine1 #%d#%d#%d OrderStatus",  w_id, d_id1, o_id1);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-1], LockType.SH);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-2], LockType.SH);
               throw new UserAbortException(msg);
             }
             v = rs.getLong("vid");
             LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], v);
             if (v != versions[idx]) {
               String msg = String.format("Validation failed for OrderLine1 %d %d %d OrderStatus", w_id, d_id1, o_id1);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-1], LockType.SH);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-2], LockType.SH);
               throw new SQLException(msg, "500");
             }
           }
         }
       }
     }
       /*
       getOrderLines(w_id, d_id2, o_id2, versions, ccType, 3, conn);
       keys[3] = ((long) (w_id - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
               + (long) (d_id2 - 1) * TPCCConfig.configCustPerDist + (long) (o_id2 - 1))
        */
      /*
      if (ccType == CCType.RC_TAILOR) {
        // validate R[Customer]
        int idx = 0;
        LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_CUSTOMER, tid, keys[idx], LockType.SH, ccType);
        long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[idx]);
        if (v >= 0) {
          if (v != versions[idx]) {
            String msg = String.format("Validation failed for customer %d %d %d OrderStatus", w_id, d_id1, c_id);
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
            throw new SQLException(msg, "500");
          } else {
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
          }
        } else {
          try (PreparedStatement getCustStmt = this.getPreparedStatement(conn, ordStatGetCustSQL, w_id, d_id1, c_id)) {
            try (ResultSet rs = getCustStmt.executeQuery()) {
              if (!rs.next()) {
                String msg = String.format("Nothing for customer #%d#%d#%d OrderStatus",  w_id, d_id1, c_id);
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
                throw new UserAbortException(msg);
              }
              v = rs.getLong("vid");
              LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], v);
              if (v != versions[idx]) {
                String msg = String.format("Validation failed for customer #%d#%d#%d OrderStatus",  w_id, d_id1, c_id);
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
                throw new SQLException(msg, "500");
              }
              else {
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx], LockType.SH);
              }
            }
          }
        }
 
        // validate R[Orders]
        idx = 1;
        LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_OPENORDER, tid, keys[idx], LockType.SH, ccType);
        v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_OPENORDER, keys[idx]);
        if (v >= 0) {
          if (v != versions[idx]) {
            String msg = String.format("Validation failed for Orders %d %d %d OrderStatus", w_id, d_id1, o_id1);
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
            throw new SQLException(msg, "500");
          } else {
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
          }
        } else {
          try (PreparedStatement getordStmt = this.getPreparedStatement(conn, ordStatGetNewestOrdSQL, w_id, d_id1, o_id1)) {
            try (ResultSet rs = getordStmt.executeQuery()) {
              if (!rs.next()) {
                String msg = String.format("Nothing for orders #%d#%d#%d OrderStatus",  w_id, d_id1, o_id1);
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
                throw new UserAbortException(msg);
              }
              v = rs.getLong("vid");
              LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_OPENORDER, keys[idx], v);
              if (v != versions[idx]) {
                String msg = String.format("Validation failed for Orders %d %d %d OrderStatus", w_id, d_id1, o_id1);
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
                throw new SQLException(msg, "500");
              }
              else {
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx], LockType.SH);
              }
            }
          }
        }
 
        // validate R1[OrderLine]
        idx = 2;
        // validationPhase = 2;
        LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_ORDERLINE, tid, keys[idx], LockType.SH, ccType);
        v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, keys[idx]);
        if (v >= 0) {
          if (v != versions[idx]) {
            String msg = String.format("Validation failed for OrderLine1 %d %d %d OrderStatus", w_id, d_id1, o_id1);
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
            throw new SQLException(msg, "500");
          } else {
            LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
          }
        } else {
          try (PreparedStatement getordStmt = this.getPreparedStatement(conn, ordStatGetOrderLinesSQL, w_id, d_id1, o_id1)) {
            try (ResultSet rs = getordStmt.executeQuery()) {
              if (!rs.next()) {
                String msg = String.format("Nothing for OrderLine1 #%d#%d#%d OrderStatus",  w_id, d_id1, o_id1);
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
                throw new UserAbortException(msg);
              }
              v = rs.getLong("vid");
              LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], v);
              if (v != versions[idx]) {
                String msg = String.format("Validation failed for OrderLine1 %d %d %d OrderStatus", w_id, d_id1, o_id1);
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
                throw new SQLException(msg, "500");
              }
              else {
                LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
              }
            }
          }
        }
         /*
         // validate R2[OrderLine]
         idx = 3;
         validationPhase = 3;
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_ORDERLINE, tid, keys[idx], LockType.SH, ccType);
         v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, keys[idx]);
         if (v >= 0) {
           if (v != versions[idx]) {
             String msg = String.format("Validation failed for OrderLine2 %d %d %d OrderStatus", w_id, d_id2, o_id2);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-3], LockType.SH);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-2], LockType.SH);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx-1], LockType.SH);
             LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
             throw new SQLException(msg, "500");
           }
         } else {
           try (PreparedStatement getordStmt = this.getPreparedStatement(conn, ordStatGetOrderLinesSQL, w_id, d_id2, o_id2)) {
             try (ResultSet rs = getordStmt.executeQuery()) {
               if (!rs.next()) {
                 String msg = String.format("Nothing for OrderLine2 #%d#%d#%d OrderStatus",  w_id, d_id2, o_id2);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-3], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-2], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx-1], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
                 throw new UserAbortException(msg);
               }
               v = rs.getLong("vid");
               LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], v);
               if (v != versions[idx]) {
                 String msg = String.format("Validation failed for OrderLine2 %d %d %d OrderStatus", w_id, d_id2, o_id2);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[idx-3], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[idx-2], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx-1], LockType.SH);
                 LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[idx], LockType.SH);
                 throw new SQLException(msg, "500");
               }
             }
           }
         }
 
 
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
 
   public void getCustomerById(int c_w_id, int c_d_id, int c_id, long[] versions, CCType type,  Connection conn)
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
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
           versions[0] = rs.getLong("vid");
         }
       }
     }
   }
 
   public void getOrderStatus(int o_w_id, int o_d_id, int o_id, long[] versions, CCType type, Connection conn)
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
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
           versions[1] = rs.getLong("vid");
         }
       }
     }
   }
 
   public void getOrderLines(int ol_w_id, int ol_d_id, int ol_o_id, long[] versions, CCType type, int idx, Connection conn)
           throws SQLException {
 
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
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
           versions[idx] = rs.getLong("vid");
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
 
   public void doAfterCommit(long[] keys, CCType type, boolean success, long[] versions) {
     if (!success)
       return;
     if (type == CCType.RC_TAILOR) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[0], LockType.SH);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, keys[1], LockType.SH);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[2], LockType.SH);
       // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, keys[3], LockType.SH);
     }
   }
 
 }
 