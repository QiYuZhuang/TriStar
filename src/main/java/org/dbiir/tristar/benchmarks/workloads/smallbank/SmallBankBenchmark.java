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

package org.dbiir.tristar.benchmarks.workloads.smallbank;

import org.dbiir.tristar.benchmarks.WorkloadConfiguration;
import org.dbiir.tristar.benchmarks.api.BenchmarkModule;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.workloads.smallbank.procedures.Amalgamate;
import org.dbiir.tristar.benchmarks.catalog.Column;
import org.dbiir.tristar.benchmarks.catalog.Table;
import org.dbiir.tristar.benchmarks.util.SQLUtil;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.transaction.concurrency.FlowRate;
import org.dbiir.tristar.transaction.concurrency.LockTable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class SmallBankBenchmark extends BenchmarkModule {

  protected final long numAccounts;

  public SmallBankBenchmark(WorkloadConfiguration workConf) throws SQLException {
    super(workConf);
    List<Connection> connectionList = new LinkedList<>();
    makeConnections(connectionList);
    LockTable.getInstance().initHotspot("smallbank", connectionList);
    FlowRate.getInstance().init("smallbank");
    this.numAccounts =
        (int) Math.round(SmallBankConstants.NUM_ACCOUNTS * workConf.getScaleFactor());
  }

  @Override
  protected List<Worker<? extends BenchmarkModule>> makeWorkersImpl() {
    List<Worker<? extends BenchmarkModule>> workers = new ArrayList<>();
    for (int i = 0; i < workConf.getTerminals(); ++i) {
      workers.add(new SmallBankWorker(this, i));
    }
    return workers;
  }

  @Override
  protected void tailorWorkloadIsolation() {
    CCType type = workConf.getConcurrencyControlType();
    if (type == CCType.RC_TAILOR) {
      firstLayerCCSolution.put("savings", "cust_id");
      firstLayerCCSolution.put("checking", "cust_id");
    } else if (type == CCType.SI_TAILOR) {
      firstLayerCCSolution.put("savings", "cust_id");
    }
  }

  @Override
  protected Package getProcedurePackageImpl() {
    return Amalgamate.class.getPackage();
  }

  /**
   * For the given table, return the length of the first VARCHAR attribute
   *
   * @param acctsTbl
   * @return
   */
  public static int getCustomerNameLength(Table acctsTbl) {
    int acctNameLength = -1;
    for (Column col : acctsTbl.getColumns()) {
      if (SQLUtil.isStringType(col.getType())) {
        acctNameLength = col.getSize();
        break;
      }
    }

    return (acctNameLength);
  }
}
