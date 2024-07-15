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

 import org.dbiir.tristar.benchmarks.api.SQLStmt;
 import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
 import org.dbiir.tristar.common.CCType;
 import org.dbiir.tristar.common.LockType;
 import org.dbiir.tristar.transaction.concurrency.LockTable;
 
 public class Payment extends TPCCProcedure {
 
   public SQLStmt payUpdateWhseSQL =
           new SQLStmt(
                   """
                 UPDATE %s
                    SET W_YTD = W_YTD + ?, vid = vid + 1
                  WHERE W_ID = ?
                  RETURNING vid;
             """
                           .formatted(TPCCConstants.TABLENAME_WAREHOUSE));
 
   public SQLStmt payUpdateDistSQL =
           new SQLStmt(
                   """
                 UPDATE %s
                    SET D_YTD = D_YTD + ?
                  WHERE D_W_ID = ?
                    AND D_ID = ?
             """
                           .formatted(TPCCConstants.TABLENAME_DISTRICT));
 
   public SQLStmt payUpdateCustBalSQL =
           new SQLStmt(
                   """
                 UPDATE %s
                    SET C_BALANCE = C_BALANCE - ?, vid = vid + 1
                  WHERE C_W_ID = ?
                    AND C_D_ID = ?
                    AND C_ID = ?
                  RETURNING vid;
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
 
   public final SQLStmt stmtUpdateConflictWSQL =
           new SQLStmt(
                   """
                 SELECT *
                  FROM %s
                  WHERE W_ID = ?
                  FOR UPDATE
             """
                           .formatted(TPCCConstants.TABLENAME_CONFLICT_WAREHOUSE));
 
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
           TPCCWorker worker)
           throws SQLException {
 
     int districtID = TPCCUtil.randomNumber(terminalDistrictLowerID, terminalDistrictUpperID, gen);
 
     float paymentAmount = (float) (TPCCUtil.randomNumber(-500000, 500000, gen) / 100.0);
     int customerDistrictID = districtID;
     w_id = (w_id % 17) + 1;
     int customerWarehouseID = w_id;
     int customerID;
     customerID = TPCCUtil.getCustomerID(gen);
 
     if (ccType == CCType.RC_ELT) {
       setConflictC(conn, w_id, customerDistrictID, customerID);
       setConflictW(conn, w_id);
     }
 
     // U[Warehouse]
     updateWarehouse(conn, customerWarehouseID, paymentAmount, ccType, versions);
 
     keys[0] = customerWarehouseID - 1;
 
     updateDistrict(conn, customerWarehouseID, districtID, paymentAmount);
 
     // U[Customer]
     updateBalance(conn, customerDistrictID, customerWarehouseID, customerID, BigDecimal.valueOf(paymentAmount), ccType, versions);
     
     keys[1] = ((long) (customerWarehouseID - 1) * TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist
             + (long) (customerDistrictID - 1) * TPCCConfig.configCustPerDist + (long) (customerID - 1));
 
     if (ccType == CCType.RC_TAILOR) {
       // lock WAREHOUSE
       LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, tid, keys[0], LockType.EX, ccType);
     }
     if (ccType == CCType.RC_TAILOR) {
       int validationPhase = 1;
       try {
         LockTable.getInstance().tryValidationLock(TPCCConstants.TABLENAME_CUSTOMER, tid, keys[1], LockType.EX, ccType);
       } catch (SQLException ex) {
         releaseTailorLock(validationPhase, keys, ccType);
         throw ex;
       }
     }
 
   }
 
   private void updateWarehouse(Connection conn, int w_id, float paymentAmount, CCType type, long[] versions) throws SQLException {
     try (PreparedStatement payUpdateWhse = this.getPreparedStatement(conn, payUpdateWhseSQL)) {
       payUpdateWhse.setBigDecimal(1, BigDecimal.valueOf(paymentAmount));
       payUpdateWhse.setInt(2, w_id);
       try (ResultSet rs = payUpdateWhse.executeQuery()) {
         if (!rs.next()) {
           throw new RuntimeException("W_ID=" + w_id + " not found!");
         }
         if (type == CCType.RC_TAILOR) {
           versions[0] = rs.getLong("vid");
         }
       }
     }
   }
 
   private void updateDistrict(Connection conn, int w_id, int districtID, float paymentAmount)
           throws SQLException {
     try (PreparedStatement payUpdateDist = this.getPreparedStatement(conn, payUpdateDistSQL)) {
       payUpdateDist.setBigDecimal(1, BigDecimal.valueOf(paymentAmount));
       payUpdateDist.setInt(2, w_id);
       payUpdateDist.setInt(3, districtID);
 
       int result = payUpdateDist.executeUpdate();
 
       if (result == 0) {
         throw new RuntimeException("D_ID=" + districtID + " D_W_ID=" + w_id + " not found!");
       }
     }
   }
 
   private void updateBalance(
           Connection conn, int customerDistrictID, int customerWarehouseID, int customerID, BigDecimal amount, CCType type, long[] versions)
           throws SQLException {
 
     try (PreparedStatement payUpdateCustBal =
                  this.getPreparedStatement(conn, payUpdateCustBalSQL)) {
       payUpdateCustBal.setBigDecimal(1, amount);
       payUpdateCustBal.setInt(2, customerWarehouseID);
       payUpdateCustBal.setInt(3, customerDistrictID);
       payUpdateCustBal.setInt(4, customerID);
 
       try (ResultSet rs = payUpdateCustBal.executeQuery()) {
         if (!rs.next()) {
           throw new RuntimeException(
                   "C_ID="
                           + customerID
                           + " C_W_ID="
                           + customerWarehouseID
                           + " C_D_ID="
                           + customerDistrictID
                           + " not found!");
         }
         if (type == CCType.RC_TAILOR | type == CCType.RC_TAILOR_ATTR) {
           versions[1] = rs.getLong("vid");
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
 
   private void releaseTailorLock(int phase, long[] keys, CCType type) {
 
     if (phase == 1 && type == CCType.RC_TAILOR) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.EX);
     } else if (phase == 2 && type == CCType.RC_TAILOR) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.EX);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.EX);
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
 
   public void doAfterCommit(long[] keys, CCType type, boolean success, long[] versions) {
     if (!success)
       return;
     if (type == CCType.RC_TAILOR) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], LockType.EX);
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.EX);
       // update the
       LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[1], versions[1]);
       LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_WAREHOUSE, keys[0], versions[0]);
       return;
     }
     if (type == CCType.RC_TAILOR_ATTR) {
       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, keys[1], LockType.EX);
       // update the
       LockTable.getInstance().updateHotspotVersion(TPCCConstants.TABLENAME_CUSTOMER, keys[1], versions[1]);
     }
   }
 
 }
 