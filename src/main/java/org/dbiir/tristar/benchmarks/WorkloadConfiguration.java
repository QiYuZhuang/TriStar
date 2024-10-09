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

package org.dbiir.tristar.benchmarks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.benchmarks.api.TransactionTypes;
import org.dbiir.tristar.benchmarks.types.DatabaseType;
import org.dbiir.tristar.common.CCType;

import lombok.Getter;
import lombok.Setter;

public class WorkloadConfiguration {

  @Getter
  private final List<Phase> phases = new ArrayList<>();
  @Setter
  @Getter
  private DatabaseType databaseType;
  @Setter
  @Getter
  private String benchmarkName;
  @Setter
  @Getter
  private String url;
  @Setter
  @Getter
  private String username;
  @Setter
  @Getter
  private String password;
  @Getter
  @Setter
  private String driverClass;
  @Getter
  @Setter
  private int batchSize;
  @Getter
  @Setter
  private int maxRetries;
  @Getter
  @Setter
  private int randomSeed = -1;
  @Setter
  @Getter
  private double scaleFactor = 1.0;
  @Getter
  @Setter
  private double selectivity = -1.0;
  @Setter
  @Getter
  private int terminals;
  @Setter
  @Getter
  private XMLConfiguration xmlConfig = null;
  @Getter
  private WorkloadState workloadState;
  @Getter
  @Setter
  private TransactionTypes transTypes = null;
  @Getter
  private int isolationMode = Connection.TRANSACTION_SERIALIZABLE;
  @Getter
  private CCType concurrencyControlType = CCType.SER;
  /* smallbank variables */
  @Setter
  @Getter
  private boolean hotspotUseFixedSize = false;
  @Getter
  @Setter
  private double hotspotPercentage = -1.0;
  @Getter
  @Setter
  private int hotspotFixedSize = -1;
  @Getter
  @Setter
  private double zipFainTheta = -1.0;
  // TPCC workload params
  @Getter
  @Setter
  private boolean customerSkew = false;
  @Getter
  @Setter
  private boolean warehouseSkew = false;

  /**
   * -- GETTER --
   *  Return the directory in which we can find the data files (for example, CSV files) for loading
   *  the database.
   * -- SETTER --
   *  Set the directory in which we can find the data files (for example, CSV files) for loading the
   *  database.

   */
  @Setter
  @Getter
  private String dataDir = null;
  private String ddlPath = null;

  /**
   * If true, establish a new connection for each transaction, otherwise use one persistent
   * connection per client session. This is useful to measure the connection overhead.
   * -- SETTER --
   *  Used by the configuration loader at startup. Changing it any other time is probably
   *  dangeroues. @see newConnectionPerTxn member docs for behavior.
   *
   * @param newConnectionPerTxn

   */
  @Setter
  private boolean newConnectionPerTxn = false;

  /**
   * If true, attempt to catch connection closed exceptions and reconnect. This allows the benchmark
   * to recover like a typical application would in the case of a replicated cluster
   * primary-secondary failover.
   * -- SETTER --
   *  Used by the configuration loader at startup. Changing it any other time is probably
   *  dangeroues. @see reconnectOnConnectionFailure member docs for behavior.
   *
   * @param reconnectOnConnectionFailure

   */
  @Setter
  private boolean reconnectOnConnectionFailure = false;

  public void setConcurrencyControlType(String type) {
    switch (type) {
      case "SERIALIZABLE":
        this.concurrencyControlType = CCType.SER;
        break;
      case "SI_ELT":
        this.concurrencyControlType = CCType.SI_ELT;
        break;
      case "RC_ELT":
        this.concurrencyControlType = CCType.RC_ELT;
        break;
      case "SI_FOR_UPDATE":
        this.concurrencyControlType = CCType.SI_FOR_UPDATE;
        break;
      case "RC_FOR_UPDATE":
        this.concurrencyControlType = CCType.RC_FOR_UPDATE;
        break;
      case "SI_TAILOR":
        this.concurrencyControlType = CCType.SI_TAILOR;
        break;
      case "RC_TAILOR":
        this.concurrencyControlType = CCType.RC_TAILOR;
        break;
      case "RC_TAILOR_LOCK":
        this.concurrencyControlType = CCType.RC_TAILOR_LOCK;
        break;
      case "DYNAMIC":
        this.concurrencyControlType = CCType.DYNAMIC;
        TAdapter.getInstance().setUsed(true);
        break;
      case "DYNAMIC_B":
        this.concurrencyControlType = CCType.DYNAMIC_B;
        TAdapter.getInstance().setUsed(true);
        break;
      case "DYNAMIC_A":
        this.concurrencyControlType = CCType.DYNAMIC_A;
        TAdapter.getInstance().setUsed(true);
        break;
      case "RC":
        this.concurrencyControlType = CCType.RC;
        break;
      case "SI":
        this.concurrencyControlType = CCType.SI;
        break;
      default:
        this.concurrencyControlType = CCType.NUM_CC;
    }
  }

  /**
   * @return @see newConnectionPerTxn member docs for behavior.
   */
  public boolean getNewConnectionPerTxn() {
    return newConnectionPerTxn;
  }

  /**
   * @return @see reconnectOnConnectionFailure member docs for behavior.
   */
  public boolean getReconnectOnConnectionFailure() {
    return reconnectOnConnectionFailure;
  }

  /** Initiate a new benchmark and workload state */
  public void initializeState(BenchmarkState benchmarkState) {
    this.workloadState = new WorkloadState(benchmarkState, phases, terminals);
  }

  public void addPhase(
      int id,
      int time,
      int warmup,
      double rate,
      List<Double> weights,
      boolean rateLimited,
      boolean disabled,
      boolean serial,
      boolean timed,
      int active_terminals,
      Phase.Arrival arrival) {
    phases.add(
        new Phase(
            benchmarkName,
            id,
            time,
            warmup,
            rate,
            weights,
            rateLimited,
            disabled,
            serial,
            timed,
            active_terminals,
            arrival));
  }

  /**
   * Return the number of phases specified in the config file
   *
   * @return
   */
  public int getNumberOfPhases() {
    return phases.size();
  }

  /** Return the path in which we can find the ddl script. */
  public String getDDLPath() {
    return this.ddlPath;
  }

  /** Set the path in which we can find the ddl script. */
  public void setDDLPath(String ddlPath) {
    this.ddlPath = ddlPath;
  }

  /** A utility method that init the phaseIterator and dialectMap */
  public void init() {
    try {
      Class.forName(this.driverClass);
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException("Failed to initialize JDBC driver '" + this.driverClass + "'", ex);
    }
  }

  public void setIsolationMode(String mode) {
    switch (mode) {
      case "TRANSACTION_SERIALIZABLE":
        this.isolationMode = Connection.TRANSACTION_SERIALIZABLE;
        break;
      case "TRANSACTION_READ_COMMITTED":
        this.isolationMode = Connection.TRANSACTION_READ_COMMITTED;
        break;
      case "TRANSACTION_REPEATABLE_READ":
        this.isolationMode = Connection.TRANSACTION_REPEATABLE_READ;
        break;
      case "TRANSACTION_READ_UNCOMMITTED":
        this.isolationMode = Connection.TRANSACTION_READ_UNCOMMITTED;
        break;
      case "TRANSACTION_NONE":
        this.isolationMode = Connection.TRANSACTION_NONE;
    }
  }

  public String getIsolationString() {
    if (this.isolationMode == Connection.TRANSACTION_SERIALIZABLE) {
      return "TRANSACTION_SERIALIZABLE";
    } else if (this.isolationMode == Connection.TRANSACTION_READ_COMMITTED) {
      return "TRANSACTION_READ_COMMITTED";
    } else if (this.isolationMode == Connection.TRANSACTION_REPEATABLE_READ) {
      return "TRANSACTION_REPEATABLE_READ";
    } else if (this.isolationMode == Connection.TRANSACTION_READ_UNCOMMITTED) {
      return "TRANSACTION_READ_UNCOMMITTED";
    } else if (this.isolationMode == Connection.TRANSACTION_NONE) {
      return "TRANSACTION_NONE";
    } else {
      return "TRANSACTION_SERIALIZABLE";
    }
  }

  @Override
  public String toString() {
    return "WorkloadConfiguration{"
        + "phases="
        + phases
        + ", databaseType="
        + databaseType
        + ", benchmarkName='"
        + benchmarkName
        + '\''
        + ", url='"
        + url
        + '\''
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", driverClass='"
        + driverClass
        + '\''
        + ", batchSize="
        + batchSize
        + ", maxRetries="
        + maxRetries
        + ", scaleFactor="
        + scaleFactor
        + ", selectivity="
        + selectivity
        + ", terminals="
        + terminals
        + ", workloadState="
        + workloadState
        + ", transTypes="
        + transTypes
        + ", isolationMode="
        + isolationMode
        + ", dataDir='"
        + dataDir
        + '\''
        + '}';
  }
}
