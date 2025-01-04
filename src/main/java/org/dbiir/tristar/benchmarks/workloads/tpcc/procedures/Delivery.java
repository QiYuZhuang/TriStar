
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
 import org.dbiir.tristar.benchmarks.api.Worker;
 import org.dbiir.tristar.benchmarks.catalog.RWRecord;
 import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
 import org.dbiir.tristar.benchmarks.util.StringUtil;
 import org.dbiir.tristar.benchmarks.workloads.smallbank.SmallBankConstants;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConfig;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCConstants;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCUtil;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
 import org.dbiir.tristar.common.CCType;
 import org.dbiir.tristar.common.LockType;
 import org.dbiir.tristar.transaction.concurrency.LockTable;

import java.sql.*;
import java.util.Random;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.dbiir.tristar.transaction.isolation.TemplateSQLMeta;
 
 public class Delivery extends TPCCProcedure {
 
   public SQLStmt delivUpdateOrderStatusSQL =
       new SQLStmt(
           """
         UPDATE %s
            SET O_STATUS = ?
          WHERE O_ID = ?
            AND O_D_ID = ?
            AND O_W_ID = ?
     """
               .formatted(TPCCConstants.TABLENAME_OPENORDER));
 
   public SQLStmt delivUpdateDeliveryInfoSQL =
           new SQLStmt(
                   """
                 UPDATE %s
                    SET ol_delivery_info = ?
                  WHERE OL_O_ID = ?
                    AND OL_D_ID = ?
                    AND OL_W_ID = ?
             """
                           .formatted(TPCCConstants.TABLENAME_ORDERLINE));
 
   public SQLStmt delivUpdateCustBalSQL =
       new SQLStmt(
           """
         UPDATE %s
            SET C_BALANCE = C_BALANCE - ?
          WHERE C_W_ID = ?
            AND C_D_ID = ?
            AND C_ID = ?
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
 
    static HashMap<Integer, Integer> clientServerIndexMap = new HashMap<>();
    static {
        clientServerIndexMap.put(0, -1);
        clientServerIndexMap.put(1, -1);
        clientServerIndexMap.put(2, -1);
    }
    @Override
    public void updateClientServerIndexMap(int clientSideIndex, int serverSideIndex) {
        clientServerIndexMap.put(clientSideIndex, serverSideIndex);
    }
    @Override
    public List<TemplateSQLMeta> getTemplateSQLMetas() {
        List<TemplateSQLMeta> templateSQLMetas = new LinkedList<>();
        templateSQLMetas.add(new TemplateSQLMeta("Delivery", 1, TPCCConstants.TABLENAME_OPENORDER,
                0, "UPDATE " + TPCCConstants.TABLENAME_OPENORDER + " SET O_STATUS  = ? WHERE OL_O_ID = ? AND OL_D_ID = ? AND OL_W_ID = ?"));
        templateSQLMetas.add(new TemplateSQLMeta("Delivery", 1, TPCCConstants.TABLENAME_ORDERLINE,
                1, "UPDATE " + TPCCConstants.TABLENAME_ORDERLINE + " SET OL_DELIVERY_INFO = ? WHERE OL_O_ID = ? AND OL_D_ID = ? AND OL_W_ID = ?"));
        templateSQLMetas.add(new TemplateSQLMeta("Delivery", 1, TPCCConstants.TABLENAME_CUSTOMER,
                2, "UPDATE " + TPCCConstants.TABLENAME_CUSTOMER +" SET C_BALANCE = C_BALANCE - ? WHERE C_W_ID = ? AND C_D_ID = ? AND C_ID = ?"));
        return templateSQLMetas;
    }
                         

   public void run(
       Connection conn,
       Random gen,
       int w_id,
       int numWarehouses,
       int terminalDistrictLowerID,
       int terminalDistrictUpperID,
       CCType ccType,
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
     updateOrderStatus(w, conn, w_id, d_id1, no_o_id1);
     
     // Orderline
     updateDeliveryInfo(w, conn, w_id, d_id1, no_o_id1);
 
     float orderLineTotal = (float) (TPCCUtil.randomNumber(100, 500000, gen) / 100.0);
 
     // Customer
     updateBalance(w, conn, w_id, d_id1, customerId, orderLineTotal);

//     long key1 = ((long) (w_id - 1) * (long) (TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist)
//             + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (no_o_id1 - 1));
//     long key2 = ((long) (w_id - 1) * (long) (TPCCConfig.configDistPerWhse * TPCCConfig.configCustPerDist)
//             + (long) (d_id1 - 1) * TPCCConfig.configCustPerDist + (customerId - 1));
   }
 
   private void updateOrderStatus(Worker worker, Connection conn, int w_id, int d_id, int no_o_id)
           throws SQLException {
      if (worker.useTxnSailsServer()) {
        try {
          worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "Delivery", 0, no_o_id, d_id, w_id));
          worker.parseExecutionResults();
        } catch (InterruptedException e) {
          System.out.println("InterruptedException on sending or receiving message");
        }
      } else {
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
          }
        }
      }
   }
 
   private void updateDeliveryInfo(Worker worker, Connection conn, int w_id, int d_id, int no_o_id)
           throws SQLException {
      if (worker.useTxnSailsServer()) {
        try {
          worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "Delivery", 1, no_o_id, d_id, w_id));
          worker.parseExecutionResults();
        } catch (InterruptedException e) {
          System.out.println("InterruptedException on sending or receiving message");
        }
      } else {
        try (PreparedStatement delivUpdateDeliveryDate = this.getPreparedStatement(conn, delivUpdateDeliveryInfoSQL)) {
          delivUpdateDeliveryDate.setString(1, "delivered");
          delivUpdateDeliveryDate.setInt(2, no_o_id);
          delivUpdateDeliveryDate.setInt(3, d_id);
          delivUpdateDeliveryDate.setInt(4, w_id);
          int status = delivUpdateDeliveryDate.executeUpdate();
        }
      }
   }
 
 
   private void updateBalance(Worker worker, Connection conn, int w_id, int d_id, int c_id, float orderLineTotal) throws SQLException {
     if (worker.useTxnSailsServer()) {
       try {
         worker.sendMsgToTxnSailsServer(StringUtil.joinValuesWithHash("execute", "Delivery", 2,
                 BigDecimal.valueOf(orderLineTotal), w_id, d_id, c_id));
         worker.parseExecutionResults();
       } catch (InterruptedException e) {
         System.out.println("InterruptedException on sending or receiving message");
       }
     } else {
       try (PreparedStatement delivUpdateCustBalDelivCnt =
                    this.getPreparedStatement(conn, delivUpdateCustBalSQL)) {
         delivUpdateCustBalDelivCnt.setBigDecimal(1, BigDecimal.valueOf(orderLineTotal));
         delivUpdateCustBalDelivCnt.setInt(2, w_id);
         delivUpdateCustBalDelivCnt.setInt(3, d_id);
         delivUpdateCustBalDelivCnt.setInt(4, c_id);
        int status = delivUpdateCustBalDelivCnt.executeUpdate();
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
 
   public void doAfterCommit() {
//       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_CUSTOMER, key2, LockType.EX);
//       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_ORDERLINE, key1, LockType.EX);
//       LockTable.getInstance().releaseValidationLock(TPCCConstants.TABLENAME_OPENORDER, key1, LockType.EX);
   }
 }
 