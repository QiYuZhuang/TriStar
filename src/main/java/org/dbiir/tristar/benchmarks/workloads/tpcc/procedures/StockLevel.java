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

 import org.dbiir.tristar.benchmarks.api.SQLStmt;
 import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
 import org.dbiir.tristar.common.CCType;
 
 public class StockLevel extends TPCCProcedure {

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
       CCType ccType,
       long tid,
       long[] versions,
       long[] keys,
       TPCCWorker w)
       throws SQLException {
 
     int i_id = TPCCUtil.getItemID(gen);
 
     if (ccType == CCType.RC_ELT) {
       setConflictS(conn, w_id, i_id);
     }
 
     getStock(conn, w_id, i_id);
     keys[0] = (long) (w_id - 1) * TPCCConfig.configItemCount + (long) (i_id - 1);
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
   }
 }
 
 