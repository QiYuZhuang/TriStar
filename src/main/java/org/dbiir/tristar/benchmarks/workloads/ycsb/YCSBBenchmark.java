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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.benchmarks.WorkloadConfiguration;
import org.dbiir.tristar.benchmarks.api.BenchmarkModule;
import org.dbiir.tristar.benchmarks.api.TransactionType;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.catalog.Table;
import org.dbiir.tristar.benchmarks.util.SQLUtil;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.InsertRecord;
import org.dbiir.tristar.config.Partition;
import org.dbiir.tristar.transaction.concurrency.LockTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class YCSBBenchmark extends BenchmarkModule {

  private static final Logger LOG = LoggerFactory.getLogger(YCSBBenchmark.class);

  /** The length in characters of each field */
  protected final int fieldSize;

  /** The constant used in the zipfian distribution (to modify the skew) */
  protected final double skewFactor;

  protected final double wrtup;
  protected final double wrtxn;
  protected final double zipf;
  protected List<Partition> partitions = null;
  protected float distributed;

  public YCSBBenchmark(WorkloadConfiguration workConf) throws SQLException {
    super(workConf);

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

    // fine-grained partition info
    if (workConf.getXmlConfig() != null) {
      int numPartitions = workConf.getXmlConfig().configurationsAt("partitions/partition").size();
      this.partitions = new ArrayList<>(numPartitions);
      for (int i = 1; i <= numPartitions; i++) {
        String key = "partitions/partition[" + i + "]";
        int partitionId = workConf.getXmlConfig().getInt(key + "/id");
        int partitionWeight = workConf.getXmlConfig().getInt(key + "/weight");
        double partitionZipf = workConf.getXmlConfig().getDouble(key + "/zipf");
        double partitionWrtup = workConf.getXmlConfig().getDouble(key + "/wrtup");
        double partitionWrtxn = workConf.getXmlConfig().getDouble(key + "/wrtxn");
        this.partitions.add(new Partition(partitionId, partitionWeight, partitionZipf, partitionWrtup, partitionWrtxn));
      }
    }

    if (workConf.getXmlConfig() != null && workConf.getXmlConfig().containsKey("distributed")) {
      this.distributed = workConf.getXmlConfig().getFloat("distributed");
    } else {
      this.distributed = 0.0f;
    }
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
    int init_record_count = (int)(workConf.getScaleFactor() * 1000);
    for (int i = 0; i < workConf.getTerminals(); ++i) {
      workers.add(new YCSBWorker(this, i, init_record_count));
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
