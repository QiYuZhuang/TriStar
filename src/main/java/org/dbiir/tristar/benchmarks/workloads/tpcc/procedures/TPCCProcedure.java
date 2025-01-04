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

 import org.dbiir.tristar.benchmarks.api.Procedure;
 import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
 import org.dbiir.tristar.benchmarks.workloads.tpcc.TPCCWorker;
 import org.dbiir.tristar.common.CCType;
 import org.dbiir.tristar.common.utils.Zip;
 
 import java.sql.Connection;
 import java.sql.SQLException;
 import java.util.Random;
 
 public abstract class TPCCProcedure extends Procedure {
 
   public abstract void run(
       Connection conn,
       Random gen,
       int terminalWarehouseID,
       int numWarehouses,
       int terminalDistrictLowerID,
       int terminalDistrictUpperID,
       CCType ccType,
       long[] keys,
       TPCCWorker w)
       throws SQLException;
 
 
 }