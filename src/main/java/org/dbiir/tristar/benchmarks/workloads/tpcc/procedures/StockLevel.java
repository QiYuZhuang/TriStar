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
 import org.dbiir.tristar.common.CCType;
 import org.dbiir.tristar.common.LockType;
 import org.dbiir.tristar.transaction.concurrency.LockTable;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.Random;
 
 public class StockLevel extends TPCCProcedure {
 
   private static final Logger LOG = LoggerFactory.getLogger(StockLevel.class);
 
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
 
   public SQLStmt stockGetStockSQL =
       new SQLStmt(
           """
         SELECT S_QUANTITY, vid
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
       CCType ccType,
       double zipftheta,
       ZipfianGenerator iditer,
       long tid,
       long[] versions,
       long[] keys,
       TPCCWorker w)
       throws SQLException {
 
     int i_id = TPCCUtil.getItemID(gen);
 
     if (ccType == CCType.RC_ELT) {
       setConflictS(conn, w_id, i_id);
     }
 
     getStock(conn, w_id, i_id, versions, ccType);
     keys[0] = (long) (w_id - 1) * TPCCConfig.configItemCount + (long) (i_id - 1);
 
     /*
     if (ccType == CCType.RC_TAILOR) {
       LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_STOCK, tid, keys[0], LockType.SH, ccType);
       long v = LockTable.getInstance().getHotspotVersion(TPCCConstants.TABLENAME_STOCK, keys[0]);
       // validate R[Stock]
       if (v >= 0) {
         if (v != versions[0]) {
           String msg = String.format("Validation failed for stock %d %d  StockLevel", w_id, i_id);
           LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[0], LockType.SH);
           throw new SQLException(msg, "500");
         }
       } else {
         try (PreparedStatement getWareStmt = this.getPreparedStatement(conn, stockGetStockSQL, w_id, i_id)) {
           try (ResultSet rs = getWareStmt.executeQuery()) {
             if (!rs.next()) {
               String msg = String.format("Nothing for stock %d %d  StockLevel", w_id, i_id);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[0], LockType.SH);
               throw new UserAbortException(msg);
             }
             v = rs.getLong("vid");
             LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_STOCK, keys[0], v);
             if (v != versions[0]) {
               String msg = String.format("Validation failed for stock %d %d  StockLevel", w_id, i_id);
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[0], LockType.SH);
               throw new SQLException(msg, "500");
             }
             else {
               LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[0], LockType.SH);
             }
           }
         }
       }
     }
      */
   }
 
 
   private int getStock(Connection conn, int w_id, int i_id, long[] versions, CCType type)
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
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
           versions[0] = rs.getLong("vid");
         }
         return rs.getInt("S_QUANTITY");
       }
     }
   }
   private void setConflictS(Connection conn, int w_id, int i_id) throws SQLException {
     try (PreparedStatement stmtSetConfC = this.getPreparedStatement(conn, stmtUpdateConflictSSQL)) {
       stmtSetConfC.setInt(1, w_id);
       stmtSetConfC.setInt(2, i_id);
       try (ResultSet rs = stmtSetConfC.executeQuery()) {
         if (!rs.next()) {
           throw new RuntimeException("ol_w_id=" + w_id + " ol_i_id=" + i_id + " not found!");
         }
       }
     }
   }
 
   public void doAfterCommit(long[] keys, CCType type, boolean success, long[] versions) {
     if (!success)
       return;
     // if (type == CCType.RC_TAILOR) {
       // LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_STOCK, keys[0], LockType.SH);
     // }
   }
 }
 
 