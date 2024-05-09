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

package org.dbiir.tristar.benchmarks.api;

import org.dbiir.tristar.benchmarks.WorkloadConfiguration;
import org.dbiir.tristar.benchmarks.catalog.AbstractCatalog;
import org.dbiir.tristar.benchmarks.types.DatabaseType;
import org.dbiir.tristar.benchmarks.util.ClassUtil;
import org.dbiir.tristar.benchmarks.util.SQLUtil;
import org.dbiir.tristar.benchmarks.util.ScriptRunner;
import org.apache.commons.lang3.StringUtils;
import org.dbiir.tristar.common.CCType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/** Base class for all benchmark implementations */
public abstract class BenchmarkModule {
  private static final Logger LOG = LoggerFactory.getLogger(BenchmarkModule.class);

  /** The workload configuration for this benchmark invocation */
  protected final WorkloadConfiguration workConf;

  /** Class loader variable for this benchmark */
  protected ClassLoader classLoader;

  /** These are the variations of the Procedure's Statement SQL */
  protected final StatementDialects dialects;

  /** Supplemental Procedures */
  private final Set<Class<? extends Procedure>> supplementalProcedures = new HashSet<>();

  /** A single Random object that should be re-used by all a benchmark's components */
  private static final ThreadLocal<Random> rng = new ThreadLocal<>();

  private AbstractCatalog catalog = null;
  /*  */
  protected HashMap<String, String> firstLayerCCSolution = new HashMap<>();

  /**
   * Constructor!
   *
   * @param workConf
   */
  public BenchmarkModule(WorkloadConfiguration workConf) {
    this.workConf = workConf;
    this.dialects = new StatementDialects(workConf);
    // setClassLoader();
    this.classLoader = ClassLoader.getSystemClassLoader();
  }

  /**
   * Instantiates the classLoader variable, needs to be overwritten if benchmark uses a custom
   * implementation.
   */
  protected void setClassLoader() {
    this.classLoader = ClassLoader.getSystemClassLoader();
  }

  // --------------------------------------------------------------------------
  // DATABASE CONNECTION
  // --------------------------------------------------------------------------

  public final Connection makeConnection() throws SQLException {

    if (StringUtils.isEmpty(workConf.getUsername())) {
      return DriverManager.getConnection(workConf.getUrl());
    } else {
      return DriverManager.getConnection(
          workConf.getUrl(), workConf.getUsername(), workConf.getPassword());
    }
  }

  private String afterLoadScriptPath = null;

  public final void setAfterLoadScriptPath(String scriptPath) {
    this.afterLoadScriptPath = scriptPath;
  }

  public CCType getCCType() {
    return workConf.getConcurrencyControlType();
  }

  // --------------------------------------------------------------------------
  // IMPLEMENTING CLASS INTERFACE
  // --------------------------------------------------------------------------

  /**
   * @return
   * @throws IOException
   */
  protected abstract List<Worker<? extends BenchmarkModule>> makeWorkersImpl() throws IOException;

  protected abstract void tailorWorkloadIsolation();

  protected abstract Package getProcedurePackageImpl();

  // --------------------------------------------------------------------------
  // PUBLIC INTERFACE
  // --------------------------------------------------------------------------

  /**
   * Return the Random generator that should be used by all this benchmark's components. We are
   * using ThreadLocal to make this support multiple threads better. This will set the seed if one
   * is specified in the workload config file
   */
  public Random rng() {
    Random ret = rng.get();
    if (ret == null) {
      if (this.workConf.getRandomSeed() != -1) {
        ret = new Random(this.workConf.getRandomSeed());
      } else {
        ret = new Random();
      }
      rng.set(ret);
    }
    return ret;
  }

  private String convertBenchmarkClassToBenchmarkName() {
    return convertBenchmarkClassToBenchmarkName(this.getClass());
  }

  protected static <T> String convertBenchmarkClassToBenchmarkName(Class<T> clazz) {
    assert (clazz != null);
    String name = clazz.getSimpleName().toLowerCase();
    // Special case for "CHBenCHmark"
    if (!name.equals("chbenchmark") && name.endsWith("benchmark")) {
      name = name.replace("benchmark", "");
    }
    return (name);
  }

  /**
   * Return the URL handle to the DDL used to load the benchmark's database schema.
   *
   * @param db_type
   */
  public String getDatabaseDDLPath(DatabaseType db_type) {

    // The order matters!
    List<String> names = new ArrayList<>();
    if (db_type != null) {
        // HACK: Use MySQL if we're given MariaDB
      names.add("ddl-" + db_type.name().toLowerCase() + ".sql");
    }
    names.add("ddl-generic.sql");

    for (String fileName : names) {
      final String benchmarkName = getBenchmarkName();
      final String path = "/benchmarks/" + benchmarkName + "/" + fileName;

      try (InputStream stream = this.getClass().getResourceAsStream(path)) {
        if (stream != null) {
          return path;
        }

      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }
    }

    return null;
  }

  public final List<Worker<? extends BenchmarkModule>> makeWorkers() throws IOException {
    return (this.makeWorkersImpl());
  }

  public final void refreshCatalog() throws SQLException {
    if (this.catalog != null) {
      try {
        this.catalog.close();
      } catch (SQLException throwables) {
        LOG.error(throwables.getMessage(), throwables);
      }
    }
    this.catalog = SQLUtil.getCatalog(this, this.getWorkloadConfiguration().getDatabaseType());
  }

  public final void runScript(String scriptPath) throws SQLException, IOException {
    try (Connection conn = this.makeConnection()) {
      DatabaseType dbType = this.workConf.getDatabaseType();
      ScriptRunner runner = new ScriptRunner(conn, true, true);
      LOG.debug("Executing script [{}] for database type [{}]", scriptPath, dbType);
      runner.runScript(scriptPath);
    }
  }

  /**
   * Create the Benchmark Database This is the main method used to create all the database objects
   * (e.g., table, indexes, etc) needed for this benchmark
   */
  public final void createDatabase() throws SQLException, IOException {
    try (Connection conn = this.makeConnection()) {
      this.createDatabase(this.workConf.getDatabaseType(), conn);
    }
  }

  /**
   * Create the Benchmark Database This is the main method used to create all the database objects
   * (e.g., table, indexes, etc) needed for this benchmark
   */
  public final void createDatabase(DatabaseType dbType, Connection conn)
          throws SQLException, IOException {

    ScriptRunner runner = new ScriptRunner(conn, true, true);

    if (workConf.getDDLPath() != null) {
      String ddlPath = workConf.getDDLPath();
      LOG.warn("Overriding default DDL script path");
      LOG.debug("Executing script [{}] for database type [{}]", ddlPath, dbType);
      runner.runExternalScript(ddlPath);
    } else {
      String ddlPath = this.getDatabaseDDLPath(dbType);
      LOG.debug("Executing script [{}] for database type [{}]", ddlPath, dbType);
      runner.runScript(ddlPath);
    }
  }

  // --------------------------------------------------------------------------
  // UTILITY METHODS
  // --------------------------------------------------------------------------

  /** Return the unique identifier for this benchmark */
  public final String getBenchmarkName() {
    String workConfName = this.workConf.getBenchmarkName();
    return workConfName != null ? workConfName : convertBenchmarkClassToBenchmarkName();
  }

  /** Return the database's catalog */
  public final AbstractCatalog getCatalog() {

    if (catalog == null) {
      throw new RuntimeException("getCatalog() has been called before refreshCatalog()");
    }

    return this.catalog;
  }

  /** Return the StatementDialects loaded for this benchmark */
  public final StatementDialects getStatementDialects() {
    return (this.dialects);
  }

  @Override
  public final String toString() {
    return getBenchmarkName();
  }

  /**
   * Initialize a TransactionType handle for the get procedure name and id This should only be
   * invoked a start-up time
   *
   * @param procName
   * @param id
   * @return
   */
  @SuppressWarnings("unchecked")
  public final TransactionType initTransactionType(
      String procName, int id, long preExecutionWait, long postExecutionWait, String transactionIsolation) {
    if (id == TransactionType.INVALID_ID) {
      throw new RuntimeException(
          String.format(
              "Procedure %s.%s cannot use the reserved id '%d' for %s",
              getBenchmarkName(),
              procName,
              id,
              TransactionType.INVALID.getClass().getSimpleName()));
    }

    Package pkg = this.getProcedurePackageImpl();

    String fullName = pkg.getName() + "." + procName;
    Class<? extends Procedure> procClass =
        (Class<? extends Procedure>) ClassUtil.getClass(this.classLoader, fullName);

    return new TransactionType(procClass, id, false, preExecutionWait, postExecutionWait, transactionIsolation);
  }

  public final WorkloadConfiguration getWorkloadConfiguration() {
    return (this.workConf);
  }

  /**
   * Return a mapping from TransactionTypes to Procedure invocations
   *
   * @return
   */
  public Map<TransactionType, Procedure> getProcedures() {
    Map<TransactionType, Procedure> proc_xref = new HashMap<>();
    TransactionTypes txns = this.workConf.getTransTypes();

    if (txns != null) {
      for (Class<? extends Procedure> procClass : this.supplementalProcedures) {
        TransactionType txn = txns.getType(procClass);
        if (txn == null) {
          txn = new TransactionType(procClass, procClass.hashCode(), true, 0, 0, "TRANSACTION_SERIALIZABLE");
          txns.add(txn);
        }
      }

      for (TransactionType txn : txns) {
        Procedure proc =
            ClassUtil.newInstance(txn.getProcedureClass(), new Object[0], new Class<?>[0]);
        proc.initialize(this.workConf.getDatabaseType());
        proc_xref.put(txn, proc);
        proc.loadSQLDialect(this.dialects);
      }
    }
    if (proc_xref.isEmpty()) {
      LOG.warn("No procedures defined for {}", this);
    }
    return (proc_xref);
  }

  /**
   * @param procClass
   */
  public final void registerSupplementalProcedure(Class<? extends Procedure> procClass) {
    this.supplementalProcedures.add(procClass);
  }
}
