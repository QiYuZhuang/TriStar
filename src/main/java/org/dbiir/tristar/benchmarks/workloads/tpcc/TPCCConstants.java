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

 import java.util.HashMap;
 
 public abstract class TPCCConstants {
   public static final String TABLENAME_DISTRICT = "district";
   public static final String TABLENAME_WAREHOUSE = "warehouse";
   public static final String TABLENAME_STOCK = "stock";
   public static final String TABLENAME_CUSTOMER = "customer";
   public static final String TABLENAME_OPENORDER = "orders";
   public static final String TABLENAME_ORDERLINE = "order_line";
   public static final String TABLENAME_CONFLICT_STOCK = "conflict_s";
   public static final String TABLENAME_CONFLICT_CUSTOMER = "conflict_c";
   public static final String TABLENAME_CONFLICT_WAREHOUSE = "conflict_w";
   public static final HashMap<String , Integer> TABLENAME_TO_INDEX = new HashMap<>(7);
 
 
   static {
     TABLENAME_TO_INDEX.put(TABLENAME_DISTRICT, 0);
     TABLENAME_TO_INDEX.put(TABLENAME_WAREHOUSE, 1);
     TABLENAME_TO_INDEX.put(TABLENAME_STOCK, 2);
     TABLENAME_TO_INDEX.put(TABLENAME_CUSTOMER, 3);
     TABLENAME_TO_INDEX.put(TABLENAME_OPENORDER, 4);
     TABLENAME_TO_INDEX.put(TABLENAME_ORDERLINE, 5);
   }
 
 
 }