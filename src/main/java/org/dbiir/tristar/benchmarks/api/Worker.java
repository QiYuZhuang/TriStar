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

import static org.dbiir.tristar.benchmarks.types.State.MEASURE;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.Setter;

import org.dbiir.tristar.TriStar;
import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.benchmarks.LatencyRecord;
import org.dbiir.tristar.benchmarks.Phase;
import org.dbiir.tristar.benchmarks.SubmittedProcedure;
import org.dbiir.tristar.benchmarks.WorkloadConfiguration;
import org.dbiir.tristar.benchmarks.WorkloadState;
import org.dbiir.tristar.benchmarks.api.Procedure.UserAbortException;
import org.dbiir.tristar.benchmarks.types.DatabaseType;
import org.dbiir.tristar.benchmarks.types.State;

import static org.dbiir.tristar.benchmarks.types.State.DONE;
import static org.dbiir.tristar.benchmarks.types.State.EXIT;
import static org.dbiir.tristar.benchmarks.types.State.LATENCY_COMPLETE;
import static org.dbiir.tristar.benchmarks.types.State.MEASURE;
import org.dbiir.tristar.benchmarks.types.TransactionStatus;
import org.dbiir.tristar.benchmarks.util.Histogram;
import org.dbiir.tristar.benchmarks.util.SQLUtil;
import org.dbiir.tristar.common.CCType;
import org.postgresql.translation.messages_bg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public abstract class Worker<T extends BenchmarkModule> implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(Worker.class);
  private static final Logger ABORT_LOG =
      LoggerFactory.getLogger("com.oltpbenchmark.api.ABORT_LOG");
  private static final double THINK_TIME_PROBABILITY = 0.01;

  private WorkloadState workloadState;
  private LatencyRecord latencies;
  private final Statement currStatement;

  // Interval requests used by the monitor
  private final AtomicInteger intervalRequests = new AtomicInteger(0);

  private final int id;
  private final T benchmark;
  protected Connection conn = null;
  protected Connection conn2 = null;
  protected final WorkloadConfiguration configuration;
  protected final TransactionTypes transactionTypes;
  protected final Map<TransactionType, Procedure> procedures = new HashMap<>();
  protected final Map<String, Procedure> name_procedures = new HashMap<>();
  protected final Map<Class<? extends Procedure>, Procedure> class_procedures = new HashMap<>();

  private final Histogram<TransactionType> txnUnknown = new Histogram<>();
  private final Histogram<TransactionType> txnSuccess = new Histogram<>();
  private final Histogram<TransactionType> txnAbort = new Histogram<>();
  private final Histogram<TransactionType> txnRetry = new Histogram<>();
  private final Histogram<TransactionType> txnErrors = new Histogram<>();
  private final Histogram<TransactionType> txtRetryDifferent = new Histogram<>();

  private boolean seenDone = false;
  CCType ccType = CCType.NUM_CC;
  @Setter
  @Getter
  protected boolean switchFinish = false;
  @Getter
  @Setter
  protected boolean switchPhaseReady = false; // validation transaction common, set it to true
  @Getter
  @Setter
  protected CCType realTimeCCType = CCType.NUM_CC; 
  @Setter
  @Getter
  protected boolean validationFinish = false;
  @Setter
  @Getter
  protected boolean executionFinish = false;
  @Setter
  @Getter
  protected boolean preSleep = false;
  @Setter
  @Getter
  protected boolean postSleep = false;
  @Setter
  @Getter
  protected boolean needAbort = false;
  protected ChannelFuture channelFuture;
  protected AtomicBoolean waitForRespond = new AtomicBoolean(false);
  protected String buffer = null; // buffer the response (the execution result) from the txnSails server

  public Worker(T benchmark, int id) {
    this.id = id;
    this.benchmark = benchmark;
    this.configuration = this.benchmark.getWorkloadConfiguration();
    this.workloadState = this.configuration.getWorkloadState();
    this.currStatement = null;
    this.transactionTypes = this.configuration.getTransTypes();

    if (benchmark.getCCType() == CCType.RC_TAILOR || benchmark.getCCType() == CCType.SI_TAILOR || benchmark.getCCType() == CCType.DYNAMIC) {
      EventLoopGroup eventExecutors = new NioEventLoopGroup();
      try {
        this.conn = this.benchmark.makeConnection();
        this.conn.setAutoCommit(false);
        switch (benchmark.getCCType()) {
          case RC:
          case RC_ELT:
          case RC_FOR_UPDATE:
          case RC_TAILOR:
          case RC_TAILOR_LOCK:
            this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            break;
          case SI:
          case SI_ELT:
          case SI_FOR_UPDATE:
          case SI_TAILOR:
            this.conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            break;
          case SER:
          case DYNAMIC:
          case DYNAMIC_B:
          case DYNAMIC_A:
            this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        }
        // read the lasted version
        this.conn2 = this.benchmark.makeConnection();
        this.conn2.setAutoCommit(true);
        this.conn2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

//        this.conn.setTransactionIsolation(this.configuration.getIsolationMode());
      } catch (SQLException ex) {
        throw new RuntimeException("Failed to connect to database", ex);
        System.out.println("connect to txnSails server");
        Bootstrap bootstrap =  new Bootstrap();
        bootstrap.group(eventExecutors).channel(NioSocketChannel.class).handler(new TriStar.NettyClientInitializer());
        channelFuture = bootstrap.connect(benchmark.workConf.getTxnSailsServerIp(), 9876).sync();
      } catch (InterruptedException e) {
        throw new RuntimeException("Failed to connect to txnSails server", e);
      }
    } else if (!this.configuration.getNewConnectionPerTxn()) {
        try {
          this.conn = this.benchmark.makeConnection();
          this.conn.setAutoCommit(false);
          switch (benchmark.getCCType()) {
            case RC:
            case RC_ELT:
            case RC_FOR_UPDATE:
            // case RC_TAILOR:
            case RC_TAILOR_LOCK:
              this.conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
              break;
            case SI:
            case SI_ELT:
            case SI_FOR_UPDATE:
            // case SI_TAILOR:
              this.conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
              break;
            case SER:
              this.conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
          }
          // read the lasted version
          this.conn2 = this.benchmark.makeConnection();
          this.conn2.setAutoCommit(true);
          this.conn2.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

  //        this.conn.setTransactionIsolation(this.configuration.getIsolationMode());
        } catch (SQLException ex) {
          throw new RuntimeException("Failed to connect to database", ex);
        }
    }

    // Generate all the Procedures that we're going to need
    this.procedures.putAll(this.benchmark.getProcedures());
    for (Entry<TransactionType, Procedure> e : this.procedures.entrySet()) {
      Procedure proc = e.getValue();
      this.name_procedures.put(e.getKey().getName(), proc);
      this.class_procedures.put(proc.getClass(), proc);
    }
  }

  public boolean oldTransaction() {
    return !switchFinish;
  }

  protected void switchIsolation(Connection conn) throws SQLException {
    switch (TAdapter.getInstance().getNextCCType()) {
      case RC:
      case RC_ELT:
      case RC_FOR_UPDATE:
      case RC_TAILOR:
      case RC_TAILOR_LOCK:
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        break;
      case SI:
      case SI_ELT:
      case SI_FOR_UPDATE:
      case SI_TAILOR:
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        break;
      case SER:
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }
  }

  protected void switchIsolation(Connection conn, CCType ccType) throws SQLException {
    switch (ccType) {
      case RC:
      case RC_ELT:
      case RC_FOR_UPDATE:
      case RC_TAILOR:
      case RC_TAILOR_LOCK:
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        break;
      case SI:
      case SI_ELT:
      case SI_FOR_UPDATE:
      case SI_TAILOR:
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        break;
      case SER:
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }
  }

  private void setIsolation(Connection conn) throws SQLException {
    switch (benchmark.getCCType()) {
      case RC:
      case RC_ELT:
      case RC_FOR_UPDATE:
      case RC_TAILOR:
      case RC_TAILOR_LOCK:
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        break;
      case SI:
      case SI_ELT:
      case SI_FOR_UPDATE:
      case SI_TAILOR:
        conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
        break;
      case SER:
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }
  }

  /** Get the BenchmarkModule managing this Worker */
  public final T getBenchmark() {
    return (this.benchmark);
  }

  /** Get the unique thread id for this worker */
  public final int getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return String.format("%s<%03d>", this.getClass().getSimpleName(), this.getId());
  }

  public final WorkloadConfiguration getWorkloadConfiguration() {
    return (this.benchmark.getWorkloadConfiguration());
  }

  public final Random rng() {
    return (this.benchmark.rng());
  }

  public final int getRequests() {
    return latencies.size();
  }

  public final int getAndResetIntervalRequests() {
    return intervalRequests.getAndSet(0);
  }

  public final Iterable<LatencyRecord.Sample> getLatencyRecords() {
    return latencies;
  }

  public final Procedure getProcedure(TransactionType type) {
    return (this.procedures.get(type));
  }

  @Deprecated
  public final Procedure getProcedure(String name) {
    return (this.name_procedures.get(name));
  }

  @SuppressWarnings("unchecked")
  public final <P extends Procedure> P getProcedure(Class<P> procClass) {
    return (P) (this.class_procedures.get(procClass));
  }

  public final Histogram<TransactionType> getTransactionSuccessHistogram() {
    return (this.txnSuccess);
  }

  public final Histogram<TransactionType> getTransactionUnknownHistogram() {
    return (this.txnUnknown);
  }

  public final Histogram<TransactionType> getTransactionRetryHistogram() {
    return (this.txnRetry);
  }

  public final Histogram<TransactionType> getTransactionAbortHistogram() {
    return (this.txnAbort);
  }

  public final Histogram<TransactionType> getTransactionErrorHistogram() {
    return (this.txnErrors);
  }

  public final Histogram<TransactionType> getTransactionRetryDifferentHistogram() {
    return (this.txtRetryDifferent);
  }

  /** Stop executing the current statement. */
  public synchronized void cancelStatement() {
    try {
      if (this.currStatement != null) {
        this.currStatement.cancel();
      }
    } catch (SQLException e) {
      LOG.error("Failed to cancel statement: {}", e.getMessage());
    }
  }

  @Override
  public final void run() {
    Thread t = Thread.currentThread();
    t.setName(this.toString());

    // In case of reuse reset the measurements
    latencies = new LatencyRecord(workloadState.getTestStartNs());

    // Invoke initialize callback
    try {
      this.initialize();
    } catch (Throwable ex) {
      throw new RuntimeException("Unexpected error when initializing " + this, ex);
    }

    // wait for start
    workloadState.blockForStart();

    while (true) {

      // PART 1: Init and check if done

      State preState = workloadState.getGlobalState();

      SubmittedProcedure pieceOfWork = workloadState.fetchWork();

      prePhase = workloadState.getCurrentPhase();
      if (prePhase == null) {
        continue;
      }

      preState = workloadState.getGlobalState();

      switch (preState) {
        case DONE, EXIT, LATENCY_COMPLETE -> {
          // Once a latency run is complete, we wait until the next
          // phase or until DONE.
          LOG.warn("preState is {}? will continue...", preState);
          continue;
        }
        default -> {}
          // Do nothing
      }

      // PART 3: Execute work

      TransactionType transactionType =
          getTransactionType(pieceOfWork, prePhase, preState, workloadState);

      if (!transactionType.equals(TransactionType.INVALID)) {

        // TODO: Measuring latency when not rate limited is ... a little
        // weird because if you add more simultaneous clients, you will
        // increase latency (queue delay) but we do this anyway since it is
        // useful sometimes
        validationFinish = false;
        executionFinish = false;

        // Wait before transaction if specified
        boolean preDelay = rng().nextDouble() < THINK_TIME_PROBABILITY;
        long preExecutionWaitInMillis = getPreExecutionWaitInMillis(transactionType);

        if (preExecutionWaitInMillis > 0 && preDelay) {
          try {
            LOG.debug(
                "{} will sleep for {} ms before executing",
                transactionType.getName(),
                preExecutionWaitInMillis);
            preSleep = true;
            Thread.sleep(preExecutionWaitInMillis);
            preSleep = false;
            if (needAbort) {
              needAbort = false;
              System.out.println("abort for switch");
              if (rng().nextDouble() < THINK_TIME_PROBABILITY) {
                Thread.sleep(preExecutionWaitInMillis);
              }
            }
          } catch (InterruptedException e) {
            LOG.error("Pre-execution sleep interrupted", e);
          }
        }

        // PART 2: Wait for work

        // Sleep if there's nothing to do.
        workloadState.stayAwake();

        Phase prePhase = workloadState.getCurrentPhase();
        if (prePhase == null) {
          continue;
        }

        // Grab some work and update the state, in case it changed while we
        // waited.

        SubmittedProcedure pieceOfWork = workloadState.fetchWork();

        prePhase = workloadState.getCurrentPhase();
        if (prePhase == null) {
          continue;
        }

        preState = workloadState.getGlobalState();

        switch (preState) {
          case DONE, EXIT, LATENCY_COMPLETE -> {
            // Once a latency run is complete, we wait until the next
            // phase or until DONE.
            LOG.warn("preState is {}? will continue...", preState);
            continue;
          }
          default -> {}
            // Do nothing
        }

        // PART 3: Execute work

        TransactionType transactionType =
            getTransactionType(pieceOfWork, prePhase, preState, workloadState);
        validationFinish = false;
        executionFinish = false;

        long postExecutionWaitInMillis = getPostExecutionWaitInMillis(transactionType);
        preDelay = rng().nextDouble() < THINK_TIME_PROBABILITY;

        if (postExecutionWaitInMillis > 0 && preDelay) {
          try {
            LOG.debug(
                "{} will sleep for {} ms after executing",
                transactionType.getName(),
                postExecutionWaitInMillis);
            postSleep = true;
            Thread.sleep(postExecutionWaitInMillis);
            postSleep = false;
            if (needAbort) {
              needAbort = false;
              System.out.println("abort for switch");
            }
          } catch (InterruptedException e) {
            LOG.error("Post-execution sleep interrupted", e);
          }
        }

        long start = System.nanoTime();

        doWork(configuration.getDatabaseType(), transactionType);

        long end = System.nanoTime();

        // PART 4: Record results

        State postState = workloadState.getGlobalState();

        switch (postState) {
          case MEASURE:
            // Non-serial measurement. Only measure if the state both
            // before and after was MEASURE, and the phase hasn't
            // changed, otherwise we're recording results for a query
            // that either started during the warmup phase or ended
            // after the timer went off.
            Phase postPhase = workloadState.getCurrentPhase();

            if (postPhase == null) {
              // Need a null check on postPhase since current phase being null is used in
              // WorkloadState
              // and ThreadBench as the indication that the benchmark is over. However, there's a
              // race
              // condition with postState not being changed from MEASURE to DONE yet, so we entered
              // the
              // switch. In this scenario, just break from the switch.
              break;
            }
            if (preState == MEASURE && postPhase.getId() == prePhase.getId()) {
              latencies.addLatency(transactionType.getId(), start, end, this.id, prePhase.getId());
              intervalRequests.incrementAndGet();
            }
            if (prePhase.isLatencyRun()) {
              workloadState.startColdQuery();
            }
            break;
          case COLD_QUERY:
            // No recording for cold runs, but next time we will since
            // it'll be a hot run.
            if (preState == State.COLD_QUERY) {
              workloadState.startHotQuery();
            }
            break;
          default:
            // Do nothing
        }

        // wait after transaction if specified
        long postExecutionWaitInMillis = getPostExecutionWaitInMillis(transactionType);

        if (postExecutionWaitInMillis > 0) {
          try {
            LOG.debug(
                "{} will sleep for {} ms after executing",
                transactionType.getName(),
                postExecutionWaitInMillis);

            Thread.sleep(postExecutionWaitInMillis);
          } catch (InterruptedException e) {
            LOG.error("Post-execution sleep interrupted", e);
          }
        }
      }

      workloadState.finishedWork();
    }

    LOG.debug("worker calling teardown");

    tearDown();
  
  }

  private TransactionType getTransactionType(
      SubmittedProcedure pieceOfWork, Phase phase, State state, WorkloadState workloadState) {
    TransactionType type = TransactionType.INVALID;

    try {
      type = transactionTypes.getType(pieceOfWork.getType());
    } catch (IndexOutOfBoundsException e) {
      if (phase.isThroughputRun()) {
        LOG.error("Thread tried executing disabled phase!");
        throw e;
      }
      if (phase.getId() == workloadState.getCurrentPhase().getId()) {
        switch (state) {
          case WARMUP -> {
            // Don't quit yet: we haven't even begun!
            LOG.info("[Serial] Resetting serial for phase.");
            phase.resetSerial();
          }
          case COLD_QUERY, MEASURE -> {
            // The serial phase is over. Finish the run early.
            LOG.info("[Serial] Updating workload state to {}.", State.LATENCY_COMPLETE);
            workloadState.signalLatencyComplete();
          }
          default -> throw e;
        }
      }
    }

    return type;
  }

  /**
   * Called in a loop in the thread to exercise the system under test. Each implementing worker
   * should return the TransactionType handle that was executed.
   *
   * @param databaseType TODO
   * @param transactionType TODO
   */
  protected final void doWork(DatabaseType databaseType, TransactionType transactionType) {

    try {
      int retryCount = 0;
      int maxRetryCount = configuration.getMaxRetries();

      while (this.workloadState.getGlobalState() != State.DONE) {

        TransactionStatus status = TransactionStatus.UNKNOWN;

        if (this.conn == null) {
          try {
            if (!this.configuration.getNewConnectionPerTxn()) {
              if (retryCount > 0) {
                Duration delay = Duration.ofSeconds(Math.min(retryCount, 5));
                LOG.info("Backing off {} seconds before reconnecting.", delay.toSeconds());
                try {
                  Thread.sleep(delay.toMillis());
                } catch (InterruptedException ex) {
                  // pass
                }
              } else {
                LOG.info("(Re)connecting to database.");
              }
            }
            this.conn = this.benchmark.makeConnection();
            this.conn.setAutoCommit(false);
            switchIsolation(this.conn);
          } catch (SQLException ex) {
            if (LOG.isDebugEnabled()) {
              LOG.debug(String.format("%s failed to open a connection...", this));
            }
            retryCount++;
            continue;
          }
        }

//        this.conn.setTransactionIsolation(transactionType.getTransactionIsolation());
//        System.out.println("set transaction benchmark: " + transactionType.getTransactionIsolation());
        long start = System.currentTimeMillis();

        try {
          if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s %s attempting...", this, transactionType));
          }

          if (TAdapter.getInstance().isInSwitchPhase() && TAdapter.getInstance().isAllWorkersReadyForSwitch() && !switchFinish) {
            switchIsolation(conn, TAdapter.getInstance().getNextCCType());
            switchFinish = true;
            conn.setAutoCommit(false);
            System.out.println(Thread.currentThread().getName() + "switch finish: " + TAdapter.getInstance().getNextCCType());
          }

          boolean needRecord = false;
          while (TAdapter.getInstance().blockForSwitch()) {
            if (!switchFinish) {
              switchIsolation(conn, TAdapter.getInstance().getNextCCType());
              switchFinish = true;
            }
            try {
              Thread.sleep(1);
            } catch (InterruptedException ex) {
              // pass
            }
            needRecord = true;
          }

          while (TAdapter.getInstance().abortForSwitch()) {
            if (!switchFinish) {
              switchIsolation(conn, TAdapter.getInstance().getNextCCType());
              switchFinish = true;
            }
            try {
              Thread.sleep(1);
            } catch (InterruptedException ex) {
              // pass
            }
            needRecord = true;
          }
          if (needRecord) {
            System.out.println(Thread.currentThread().getName() + "-<586> block time: " + (System.currentTimeMillis() - start) + " ms");
          }
        
          if (LOG.isDebugEnabled()) {
            LOG.debug(
                String.format(
                    "%s %s completed with status [%s]...", this, transactionType, status.name()));
          }

          if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s %s committing...", this, transactionType));
          }
          if (benchmark.getCCType() == CCType.RC_TAILOR || benchmark.getCCType() == CCType.SI_TAILOR || benchmark.getCCType() == CCType.DYNAMIC) {
            try{
              channelFuture.channel().writeAndFlush("commit").sync();
            } catch (InterruptedException ex) {
              // pass
            }
          } else {
            conn.commit();
          }
          break;
        } catch (Procedure.UserAbortException ex) {
          try {
            if (benchmark.getCCType() == CCType.RC_TAILOR || benchmark.getCCType() == CCType.SI_TAILOR || benchmark.getCCType() == CCType.DYNAMIC) {
              try{
                channelFuture.channel().writeAndFlush("rollback").sync();
              } catch (InterruptedException e) {
                // pass
              }
            } else {
              conn.rollback();
            }
          } catch (SQLException ex2) {
            LOG.error("SQLException caught while rolling back transaction.", ex2);
            // force a reconnection
            conn = null;
          }

          ABORT_LOG.debug(String.format("%s Aborted", transactionType), ex);

          status = TransactionStatus.USER_ABORTED;

          break;

        } catch (SQLException ex) {
          // check if we should attempt to ignore connection errors and reconnect
          // System.out.println(Thread.currentThread().getName() + " " + ex);
          boolean isConnectionErrorException = SQLUtil.isConnectionErrorException(ex);

          if (indicatesReadOnly(ex)) {
            if (SQLUtil.isConnectionOK(conn)) {
              conn.setReadOnly(true);
            }
          }

          // if the connection is closed, we can't rollback
          if (!isConnectionErrorException && SQLUtil.isConnectionOK(conn)) {
            // if the error is that we're attempting a write transaction to a read-only secondary,
            // then we can't rollback anyways, so don't bother trying
            if (conn.isReadOnly()) {
              // in that case, we should close the connection and possibly try again
              LOG.debug(
                  String.format(
                      "Won't attempt a rollback since the SQL connection looks read-only during [%s]... current retry attempt [%d], max retry attempts [%d], sql state [%s], error code [%d].",
                      transactionType,
                      retryCount,
                      maxRetryCount,
                      ex.getSQLState(),
                      ex.getErrorCode()),
                  ex);
              try {
                conn.close();
              } catch (SQLException ex2) {
                LOG.error("SQLException caught while closing connection.", ex2);
              }
              // force a reconnection
              conn = null;
            }
            // otherwise, we should attempt a rollback
            else {
              LOG.debug(
                  String.format(
                      "Attempting a rollback since a problem was detected during [%s]... current retry attempt [%d], max retry attempts [%d], sql state [%s], error code [%d].",
                      transactionType,
                      retryCount,
                      maxRetryCount,
                      ex.getSQLState(),
                      ex.getErrorCode()),
                  ex);
              try {
                conn.rollback();
              } catch (SQLException ex2) {
                LOG.error("SQLException caught while attempting to rollback transaction.", ex2);
                // force a reconnection
                conn = null;
              }
            }
          }
          // connection is closed, try a reconnect
          else {
            if (this.configuration.getReconnectOnConnectionFailure()) {
              LOG.debug(
                  String.format(
                      "Won't attempt a rollback since a problem with the SQL connection was detected during [%s]... current retry attempt [%d], max retry attempts [%d], sql state [%s], error code [%d].",
                      transactionType,
                      retryCount,
                      maxRetryCount,
                      ex.getSQLState(),
                      ex.getErrorCode()),
                  ex);
            } else {
              // old behavior, will likley result in an exception thrown
              // and an aborted benchmark due to the connection problem
              LOG.debug(
                  String.format(
                      "Attempting a rollback since a problem was detected during [%s] (despite connection error detection - see reconnectOnConnectionFailure setting)... current retry attempt [%d], max retry attempts [%d], sql state [%s], error code [%d].",
                      transactionType,
                      retryCount,
                      maxRetryCount,
                      ex.getSQLState(),
                      ex.getErrorCode()),
                  ex);
              try {
                conn.rollback();
              } catch (SQLException ex2) {
                LOG.error("SQLException caught while attempting to rollback transaction.", ex2);
                // force a reconnection
                conn = null;
              }
            }
          }

          // check the connection (after possible reconnection) again
          if ((isConnectionErrorException || !SQLUtil.isConnectionOK(conn))
              && this.configuration.getReconnectOnConnectionFailure()) {
            LOG.debug(
                String.format(
                    "Retryable SQL connection exception occurred during [%s]... current retry attempt [%d], max retry attempts [%d], sql state [%s], error code [%d].",
                    transactionType,
                    retryCount,
                    maxRetryCount,
                    ex.getSQLState(),
                    ex.getErrorCode()),
                ex);

            // force a reconnection
            try {
              if (conn != null) {
                conn.close();
              }
            } catch (Exception e) {
              LOG.warn("Failed to close faulty connection (somewhat expected).", e);
            } finally {
              conn = null;
            }

            status = TransactionStatus.RETRY_DIFFERENT;

            retryCount++;
          } else if (isRetryable(ex)) {
            LOG.debug(
                String.format(
                    "Retryable SQLException occurred during [%s]... current retry attempt [%d], max retry attempts [%d], sql state [%s], error code [%d].",
                    transactionType,
                    retryCount,
                    maxRetryCount,
                    ex.getSQLState(),
                    ex.getErrorCode()),
                ex);

            status = TransactionStatus.RETRY;

            retryCount++;
          } else {
            LOG.warn(
                String.format(
                    "SQLException occurred during [%s] and will not be retried... sql state [%s], error code [%d].",
                    transactionType, ex.getSQLState(), ex.getErrorCode()),
                ex);

            status = TransactionStatus.ERROR;

            break;
          }
        } finally {
          long executeLatency = System.currentTimeMillis() - start;
          if (status == TransactionStatus.SUCCESS) {
            executeAfterWork(transactionType, true, executeLatency);
            // System.out.println("SUCCESS-758");
          } else {
            executeAfterWork(transactionType, false, executeLatency);
            // System.out.println("ERROR-761");
          }

          if (this.configuration.getNewConnectionPerTxn() && this.conn != null) {
            try {
              this.conn.close();
              this.conn = null;
            } catch (SQLException e) {
              LOG.error("Connection couldn't be closed.", e);
            }
          }
          
          switch (status) {
            case UNKNOWN : this.txnUnknown.put(transactionType);
            case SUCCESS : this.txnSuccess.put(transactionType);
            case USER_ABORTED : this.txnAbort.put(transactionType);
            case RETRY : this.txnRetry.put(transactionType);
            case RETRY_DIFFERENT : this.txtRetryDifferent.put(transactionType);
            case ERROR : this.txnErrors.put(transactionType);
          }
        }
      }
    } catch (SQLException ex) {
      String msg =
          String.format(
              "Unexpected SQLException in '%s' when executing '%s' on [%s]",
              this, transactionType, databaseType.name());

      throw new RuntimeException(msg, ex);
    }
  }

  /**
   * Checks to see if the exception indicates that the current connection is read-only.
   *
   * @param ex
   * @return
   */
  private boolean indicatesReadOnly(SQLException ex) {
    String sqlState = ex.getSQLState();
    int errorCode = ex.getErrorCode();

    LOG.debug("sql state [{}] and error code [{}]", sqlState, errorCode);

    if (sqlState == null) {
      return false;
    }

    // ------------------
    // SqlServer: "SELECT TOP 10 * FROM sys.messages"
    // ------------------
    if (errorCode == 3906 && sqlState.equals("S0002")) {
      return true;
    }

    // ------------------
    // MYSQL:
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-error-sqlstates.html
    // ------------------
    // TODO

    // ------------------
    // POSTGRES: https://www.postgresql.org/docs/current/errcodes-appendix.html
    // ------------------
    // TODO

    return false;
  }

  private boolean isRetryable(SQLException ex) {

    String sqlState = ex.getSQLState();
    int errorCode = ex.getErrorCode();

    LOG.debug("sql state [{}] and error code [{}]", sqlState, errorCode);

    if (sqlState == null) {
      return false;
    }

    if (ex instanceof SQLRecoverableException) {
      return true;
    }

    // ------------------
    // SqlServer: "SELECT TOP 10 * FROM sys.messages"
    // ------------------
    if (errorCode == 12222 && sqlState.equals("S0051")) {
      // Lock request time out period exceeded.
      return true;
    } else if (errorCode == 0 && sqlState.equals("HY008")) {
      // The query has timed out.
      return true;
    }

    // ------------------
    // MYSQL:
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-error-sqlstates.html
    // ------------------
    if (errorCode == 1213 && sqlState.equals("40001")) {
      // MySQL ER_LOCK_DEADLOCK
      return true;
    } else if (errorCode == 1205 && sqlState.equals("40001")) {
      // MySQL ER_LOCK_WAIT_TIMEOUT
      return true;
    }

    // ------------------
    // POSTGRES: https://www.postgresql.org/docs/current/errcodes-appendix.html
    // ------------------
    if (errorCode == 0 && sqlState.equals("40001")) {
      // Postgres serialization_failure
      return true;
    } else if (errorCode == 0 && sqlState.equals("55P03")) {
      // Postgres lockwait timeout
      return true;
    } else if (errorCode == 0 && sqlState.equals("500")) {
      return true;
    }
    return false;
  }

  private void rollbackOnConnection() throws SQLException {
    if (benchmark.getCCType() == CCType.RC_TAILOR || benchmark.getCCType() == CCType.SI_TAILOR || benchmark.getCCType() == CCType.DYNAMIC) {
      try{
        channelFuture.channel().writeAndFlush("rollback").sync();
      } catch (InterruptedException e) {
        // pass
      } finally {

      }
    } else {
      conn.rollback();
    }
  }

  private void commitOnConnection() throws SQLException {
    if (benchmark.getCCType() == CCType.RC_TAILOR || benchmark.getCCType() == CCType.SI_TAILOR || benchmark.getCCType() == CCType.DYNAMIC) {
      try{
        channelFuture.channel().writeAndFlush("commit").sync();
      } catch (InterruptedException e) {
        // pass
      }
    } else {
      conn.commit();
    }
  }

  private static void sendMsgToTxnSailsServer(Channel ctx, String msg) throws InterruptedException {
    lockWaitForResponse();
    ByteBuf resp = ctx.alloc().buffer(msg.length());
    resp.writeBytes(msg.getBytes(StandardCharsets.UTF_8));
    ctx.writeAndFlush(resp).sync();
  }

  private static void lockWaitForResponse() {
      while (!Thread.interrupted() && !waitForRespond.compareAndSet(false, true)) {
          try {
              Thread.sleep(1);
          } catch (InterruptedException e) {
          }
      }
  }

  private static void unlockWaitForResponse() {
      waitForRespond.set(false);
  }

  /**
   * Optional callback that can be used to initialize the Worker right before the benchmark
   * execution begins
   */
  protected void initialize() {
    // The default is to do nothing
  }

  /**
   * Invoke a single transaction for the given TransactionType
   */
  protected abstract TransactionStatus executeWork(Connection conn, TransactionType txnType)
      throws UserAbortException, SQLException;

  protected abstract void executeAfterWork(TransactionType txnType, boolean success, long latency)
          throws UserAbortException, SQLException;

  /** Called at the end of the test to do any clean up that may be required. */
  public void tearDown() {
    if (!this.configuration.getNewConnectionPerTxn() && this.conn != null) {
      try {
        conn.close();
      } catch (SQLException e) {
        LOG.error("Connection couldn't be closed.", e);
      }
    }
  }

  public void initializeState() {
    this.workloadState = this.configuration.getWorkloadState();
  }

  protected long getPreExecutionWaitInMillis(TransactionType type) {
    return type.getPreExecutionWait();
  }

  protected long getPostExecutionWaitInMillis(TransactionType type) {
    return type.getPostExecutionWait();
  }
}
