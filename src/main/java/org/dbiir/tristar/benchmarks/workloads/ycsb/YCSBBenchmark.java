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

package org.dbiir.tristar.benchmarks.workloads.ycsb;

import org.dbiir.tristar.benchmarks.WorkloadConfiguration;
import org.dbiir.tristar.benchmarks.api.BenchmarkModule;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.InsertRecord;
import org.dbiir.tristar.benchmarks.catalog.Table;
import org.dbiir.tristar.benchmarks.util.SQLUtil;
import org.dbiir.tristar.transaction.concurrency.FlowRate;
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow;

public final class YCSBBenchmark extends BenchmarkModule {

  private static final Logger LOG = LoggerFactory.getLogger(YCSBBenchmark.class);

  /** The length in characters of each field */
  protected final int fieldSize;

  /** The constant used in the zipfian distribution (to modify the skew) */
  protected final double skewFactor;

  protected final double wrtup;
  protected final double wrtxn;
  protected final double zipf;

  public YCSBBenchmark(WorkloadConfiguration workConf) throws SQLException {
    super(workConf);
    List<Connection> connectionList = new LinkedList<>();
    makeConnections(connectionList);
    LockTable.getInstance().initHotspot("ycsb", connectionList);
    FlowRate.getInstance().init("smallbank");

    int fieldSize = YCSBConstants.MAX_FIELD_SIZE;
    if (workConf.getXmlConfig() != null && workConf.getXmlConfig().containsKey("fieldSize")) {
      fieldSize =
          Math.min(workConf.getXmlConfig().getInt("fieldSize"), YCSBConstants.MAX_FIELD_SIZE);
    }
    this.fieldSize = fieldSize;
    if (this.fieldSize <= 0) {
      throw new RuntimeException("Invalid YCSB fieldSize '" + this.fieldSize + "'");
    }

    double skewFactor = 0.99;
    if (workConf.getXmlConfig() != null && workConf.getXmlConfig().containsKey("skewFactor")) {
      skewFactor = workConf.getXmlConfig().getDouble("skewFactor");
      if (skewFactor <= 0 || skewFactor >= 1) {
        throw new RuntimeException("Invalid YCSB skewFactor '" + skewFactor + "'");
      }
    }
    this.skewFactor = skewFactor;

    if (workConf.getXmlConfig() != null && workConf.getXmlConfig().containsKey("zipf")) {
      this.zipf = workConf.getXmlConfig().getDouble("zipf");
    } else {
      this.zipf = 0.1;
    }

    if (workConf.getXmlConfig() != null && workConf.getXmlConfig().containsKey("wrtup")) {
      this.wrtup = workConf.getXmlConfig().getDouble("wrtup");
    } else {
      this.wrtup = 0.5;
    }

    if (workConf.getXmlConfig() != null && workConf.getXmlConfig().containsKey("wrtxn")) {
      this.wrtxn = workConf.getXmlConfig().getDouble("wrtxn");
    } else {
      this.wrtxn = 0.5;
    }
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
    try {
      // LOADING FROM THE DATABASE IMPORTANT INFORMATION
      // LIST OF USERS
      Table t = this.getCatalog().getTable("USERTABLE");
      String userCount = SQLUtil.getMaxColSQL(this.workConf.getDatabaseType(), t, "ycsb_key");

      try (Connection metaConn = this.makeConnection();
          Statement stmt = metaConn.createStatement();
          ResultSet res = stmt.executeQuery(userCount)) {
        int init_record_count = (int)(workConf.getScaleFactor() * 1000);
//        while (res.next()) {
//          init_record_count = res.getInt(1);
//        }

        for (int i = 0; i < workConf.getTerminals(); ++i) {
          workers.add(new YCSBWorker(this, i, init_record_count));
        }
      }
    } catch (SQLException e) {
      LOG.error(e.getMessage(), e);
    }
    return workers;
  }

  @Override
  protected void tailorWorkloadIsolation() {

  }

  @Override
  protected Package getProcedurePackageImpl() {
    return InsertRecord.class.getPackage();
  }
}
