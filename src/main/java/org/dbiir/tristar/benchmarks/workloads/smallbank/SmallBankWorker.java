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

import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.Procedure.UserAbortException;
import org.dbiir.tristar.benchmarks.api.TransactionType;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
import org.dbiir.tristar.benchmarks.util.RandomDistribution.Zipf;
import org.dbiir.tristar.benchmarks.workloads.smallbank.procedures.*;
import org.dbiir.tristar.benchmarks.types.TransactionStatus;
import org.dbiir.tristar.benchmarks.util.RandomDistribution.DiscreteRNG;
import org.dbiir.tristar.benchmarks.util.RandomDistribution.Flat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

/**
 * SmallBank Benchmark Work Driver Fuck yo couch.
 *
 * @author pavlo
 */
public final class SmallBankWorker extends Worker<SmallBankBenchmark> {
  private static final Logger LOG = LoggerFactory.getLogger(SmallBankWorker.class);

  private final Amalgamate procAmalgamate;
  private final Balance procBalance;
  private final DepositChecking procDepositChecking;
  private final SendPayment procSendPayment;
  private final TransactSavings procTransactSavings;
  private final WriteCheck procWriteCheck;
  private final DiscreteRNG rng;
  private final long numAccounts;
  private final int custNameLength;
  private final String custNameFormat;
  private final long[] custIdsBuffer = {-1L, -1L};

  private final Random rand;
  private boolean hotspotUseFixedSize = false;
  private double hotspotPercentage = 50.0;
  private int hotspotFixedSize = 1000;
  private double zipFainTheta = -1.0;
  private final ZipfianGenerator custidInZipf;
  private final long[] versionBuffer = new long[5];
  private long tid;

  public SmallBankWorker(SmallBankBenchmark benchmarkModule, int id) {
    super(benchmarkModule, id);

    // This is a minor speed-up to avoid having to invoke the hashmap look-up
    // everytime we want to execute a txn. This is important to do on
    // a client machine with not a lot of cores
    this.procAmalgamate = this.getProcedure(Amalgamate.class);
    this.procBalance = this.getProcedure(Balance.class);
    this.procDepositChecking = this.getProcedure(DepositChecking.class);
    this.procSendPayment = this.getProcedure(SendPayment.class);
    this.procTransactSavings = this.getProcedure(TransactSavings.class);
    this.procWriteCheck = this.getProcedure(WriteCheck.class);

    this.numAccounts = benchmarkModule.numAccounts;
    this.custNameLength =
        SmallBankBenchmark.getCustomerNameLength(
            benchmarkModule.getCatalog().getTable(SmallBankConstants.TABLENAME_ACCOUNTS));
    this.custNameFormat = "%0" + this.custNameLength + "d";
    this.rng = new Flat(rng(), 0, this.numAccounts);
    this.rand = new Random();
    this.hotspotUseFixedSize = benchmarkModule.getWorkloadConfiguration().isHotspotUseFixedSize();
    this.hotspotFixedSize = benchmarkModule.getWorkloadConfiguration().getHotspotFixedSize();
    this.hotspotPercentage = benchmarkModule.getWorkloadConfiguration().getHotspotPercentage();
    this.zipFainTheta = benchmarkModule.getWorkloadConfiguration().getZipFainTheta();
    this.custidInZipf = new ZipfianGenerator(new Random(), 0, this.numAccounts, this.zipFainTheta > 0 ? zipFainTheta : 0.1);
  }

  protected void generateCustIds(boolean needsTwoAccts) {
    for (int i = 0; i < this.custIdsBuffer.length; i++) {
      this.custIdsBuffer[i] = genIds();

      // They can never be the same!
      if (i > 0 && this.custIdsBuffer[i - 1] == this.custIdsBuffer[i]) {
        i--;
        continue;
      }

      // If we only need one acctId, break out here.
      if (i == 0 && !needsTwoAccts) {
        break;
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug(String.format("Accounts: %s", Arrays.toString(this.custIdsBuffer)));
    }
//    if (needsTwoAccts && this.custIdsBuffer[0] < this.custIdsBuffer[1]) {
//      long temp = custIdsBuffer[0];
//      custIdsBuffer[0] = custIdsBuffer[1];
//      custIdsBuffer[1] = temp;
//    }
  }

  private long genIds() {
    if (hotspotUseFixedSize && rand.nextDouble() < hotspotPercentage) {
      return rng.nextLong() % hotspotFixedSize;
    } else if (zipFainTheta > 0) {
      return this.custidInZipf.nextLong();
    } else {
      return rng.nextLong();
    }
  }

  private void genSavingsCustId() {
    if (hotspotUseFixedSize) {
      if (custIdsBuffer[1] < hotspotFixedSize)
        custIdsBuffer[1] += hotspotFixedSize;
    } else if (zipFainTheta > 0) {
      custIdsBuffer[1] = rng.nextLong();
    }
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType txnType)
      throws UserAbortException, SQLException {
    Class<? extends Procedure> procClass = txnType.getProcedureClass();
    this.tid = (System.nanoTime() << 10) | (Thread.currentThread().getId() & 0x3ff);
    // Amalgamate
    if (procClass.equals(Amalgamate.class)) {
      this.generateCustIds(true);
      this.procAmalgamate.run(conn, this.custIdsBuffer[0], this.custIdsBuffer[1], getBenchmark().getCCType(), versionBuffer, tid);

      // Balance
    } else if (procClass.equals(Balance.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procBalance.run(conn, custName, getBenchmark().getCCType(), versionBuffer, tid);

      // DepositChecking
    } else if (procClass.equals(DepositChecking.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procDepositChecking.run(
          conn, custName, SmallBankConstants.PARAM_DEPOSIT_CHECKING_AMOUNT, getBenchmark().getCCType(), versionBuffer, tid);

      // SendPayment
    } else if (procClass.equals(SendPayment.class)) {
      return TransactionStatus.SUCCESS;
//      this.generateCustIds(true);
//      this.procSendPayment.run(
//          conn,
//          this.custIdsBuffer[0],
//          this.custIdsBuffer[1],
//          SmallBankConstants.PARAM_SEND_PAYMENT_AMOUNT);

      // TransactSavings
    } else if (procClass.equals(TransactSavings.class)) {
      this.generateCustIds(false);
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procTransactSavings.run(
          conn, custName, SmallBankConstants.PARAM_TRANSACT_SAVINGS_AMOUNT, getBenchmark().getCCType(), versionBuffer, tid);

      // WriteCheck
    } else if (procClass.equals(WriteCheck.class)) {
      this.generateCustIds(true);
      this.genSavingsCustId();
      String custName = String.format(this.custNameFormat, this.custIdsBuffer[0]);
      this.procWriteCheck.run(conn, custName, custIdsBuffer[1], SmallBankConstants.PARAM_WRITE_CHECK_AMOUNT, getBenchmark().getCCType(), conn2, versionBuffer, tid);
    }

    return TransactionStatus.SUCCESS;
  }

  @Override
  protected void executeAfterWork(TransactionType txnType, boolean success)
          throws UserAbortException, SQLException {
    Class<? extends Procedure> procClass = txnType.getProcedureClass();

    // Amalgamate
    if (procClass.equals(Amalgamate.class)) {
      this.procAmalgamate.doAfterCommit(this.custIdsBuffer[0], this.custIdsBuffer[1], getBenchmark().getCCType(), success, versionBuffer, tid);

      // Balance
    } else if (procClass.equals(Balance.class)) {
      this.procBalance.doAfterCommit(this.custIdsBuffer[0], getBenchmark().getCCType(), success, versionBuffer, tid);

      // DepositChecking
    } else if (procClass.equals(DepositChecking.class)) {
      this.procDepositChecking.doAfterCommit(this.custIdsBuffer[0], getBenchmark().getCCType(), success, versionBuffer, tid);

      // SendPayment
    } else if (procClass.equals(SendPayment.class)) {
      // TransactSavings
    } else if (procClass.equals(TransactSavings.class)) {
      this.procTransactSavings.doAfterCommit(this.custIdsBuffer[0], getBenchmark().getCCType(), success, versionBuffer, tid);

      // WriteCheck
    } else if (procClass.equals(WriteCheck.class)) {
      this.procWriteCheck.doAfterCommit(this.custIdsBuffer[0], this.custIdsBuffer[1], getBenchmark().getCCType(), success, versionBuffer, tid);
    }
  }
}
