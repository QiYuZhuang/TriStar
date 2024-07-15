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

 package org.dbiir.tristar.benchmarks.workloads.tpcc;

 import org.dbiir.tristar.benchmarks.api.Procedure;
 import org.dbiir.tristar.benchmarks.api.Procedure.UserAbortException;
 import org.dbiir.tristar.benchmarks.api.TransactionType;
 import org.dbiir.tristar.benchmarks.api.Worker;
 import org.dbiir.tristar.benchmarks.distributions.Generator;
 import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.procedures.*;
 import org.dbiir.tristar.benchmarks.types.TransactionStatus;
 import org.dbiir.tristar.common.CCType;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 
 import java.awt.*;
 import java.sql.Connection;
 import java.sql.SQLException;
 import java.util.Random;
 
 public final class TPCCWorker extends Worker<TPCCBenchmark> {
 
   private static final Logger LOG = LoggerFactory.getLogger(TPCCWorker.class);
 
   private final int terminalWarehouseID;
 
   /** Forms a range [lower, upper] (inclusive). */
   private final int terminalDistrictLowerID;
   private final int terminalDistrictUpperID;
   private final Random gen = new Random();
 
   private final int numWarehouses;
   private long tid;
   private final long[] versionBuffer = new long[20];
   private final long[] keyBuffer = new long[80];
 
   public TPCCWorker(
           TPCCBenchmark benchmarkModule,
           int id,
           int terminalWarehouseID,
           int terminalDistrictLowerID,
           int terminalDistrictUpperID,
           int numWarehouses) {
     super(benchmarkModule, id);
 
     this.terminalWarehouseID = terminalWarehouseID;
     this.terminalDistrictLowerID = terminalDistrictLowerID;
     this.terminalDistrictUpperID = terminalDistrictUpperID;
     this.numWarehouses = numWarehouses;
   }
 
   /** Executes a single TPCC transaction of type transactionType. */
   @Override
   protected TransactionStatus executeWork(Connection conn, TransactionType nextTransaction)
       throws UserAbortException, SQLException {
     long mask = 0x7FFFFFFFFFFFFFFFL;
     this.tid = ((System.nanoTime() << 10) | (Thread.currentThread().getId() & 0x3ff)) & mask ; ;
     //System.out.println(Thread.currentThread().getName() + " #" + tid + " transactionType: " + nextTransaction);
     try {
       TPCCProcedure proc = (TPCCProcedure) this.getProcedure(nextTransaction.getProcedureClass());
       proc.run(
           conn,
           gen,
           terminalWarehouseID,
           numWarehouses,
           terminalDistrictLowerID,
           terminalDistrictUpperID,
           getBenchmark().getCCType(),
           tid,
           versionBuffer,
           keyBuffer,
           this);
     } catch (ClassCastException ex) {
       // fail gracefully
       LOG.error("We have been invoked with an INVALID transactionType?!", ex);
       throw new RuntimeException("Bad transaction type = " + nextTransaction);
     }
     return (TransactionStatus.SUCCESS);
   }
 
   @Override
   protected long getPreExecutionWaitInMillis(TransactionType type) {
     // TPC-C 5.2.5.2: For keying times for each type of transaction.
     return type.getPreExecutionWait();
   }
 
   @Override
   protected long getPostExecutionWaitInMillis(TransactionType type) {
     // TPC-C 5.2.5.4: For think times for each type of transaction.
     long mean = type.getPostExecutionWait();
 
     float c = this.getBenchmark().rng().nextFloat();
     long thinkTime = (long) (-1 * Math.log(c) * mean);
     if (thinkTime > 10 * mean) {
       thinkTime = 10 * mean;
     }
 
     return thinkTime;
   }
 
   @Override
   protected void executeAfterWork(TransactionType txnType, boolean success)
           throws UserAbortException, SQLException {
     Class<? extends Procedure> procClass = txnType.getProcedureClass();
 
     // Delivery
     if (procClass.equals(Delivery.class)) {
       this.getProcedure(Delivery.class).doAfterCommit(keyBuffer[0], keyBuffer[1], getBenchmark().getCCType(), success, versionBuffer);
     }
 
     // NewOrder
     if (procClass.equals(NewOrder.class)) {
       this.getProcedure(NewOrder.class).doAfterCommit(keyBuffer, getBenchmark().getCCType(),success, versionBuffer);
     }
 
     // Payment
     if (procClass.equals(Payment.class)) {
       this.getProcedure(Payment.class).doAfterCommit(keyBuffer, getBenchmark().getCCType(),success, versionBuffer);
     }
 
     // OrderStatus
     if (procClass.equals(OrderStatus.class)) {
       this.getProcedure(OrderStatus.class).doAfterCommit(keyBuffer, getBenchmark().getCCType(),success, versionBuffer);
     }
 
     // Stock
     if (procClass.equals(StockLevel.class)) {
       this.getProcedure(StockLevel.class).doAfterCommit(keyBuffer, getBenchmark().getCCType(),success, versionBuffer);
     }
   }
 }
 