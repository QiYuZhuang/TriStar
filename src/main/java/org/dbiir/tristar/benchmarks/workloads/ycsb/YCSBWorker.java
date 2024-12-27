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


import org.dbiir.tristar.adapter.Adapter;
import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.TransactionType;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.distributions.CounterGenerator;
import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
import org.dbiir.tristar.benchmarks.types.TransactionStatus;
import org.dbiir.tristar.benchmarks.util.TextGenerator;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

/**
 * YCSBWorker Implementation I forget who really wrote this but I fixed it up in 2016...
 *
 * @author pavlo
 */
class YCSBWorker extends Worker<YCSBBenchmark> {

  private final ZipfianGenerator readRecord;
  private static CounterGenerator insertRecord;

  private final char[] data;
  private final String[] params = new String[YCSBConstants.NUM_FIELDS];
  private final String[][] fixParams = new String[10][YCSBConstants.NUM_FIELDS];
  private final String[] results = new String[YCSBConstants.NUM_FIELDS];

  private final UpdateRecord procUpdateRecord;
  private final ScanRecord procScanRecord;
  private final ReadRecord procReadRecord;
  private final ReadModifyWriteRecord procReadModifyWriteRecord;
  private final InsertRecord procInsertRecord;
  private final DeleteRecord procDeleteRecord;
  private final ReadWriteRecord procReadWriteRecord;
  private final int totalRequest = 10;
  private final double ratio1;
  private final double ratio2;
  private final long[] versionBuffer;
  private int[] keynames = new int[totalRequest];
  private long tid;


  public YCSBWorker(YCSBBenchmark benchmarkModule, int id, int init_record_count) {
    super(benchmarkModule, id);
    this.data = new char[benchmarkModule.fieldSize];
    this.ratio1 = benchmarkModule.wrtxn;
    this.ratio2 = benchmarkModule.wrtup;
    this.readRecord = new ZipfianGenerator(rng(), init_record_count, benchmarkModule.zipf); // pool for read keys
    versionBuffer = new long[totalRequest];
    for (int i = 0; i < totalRequest; i++) {
      for (int j = 0; j < this.params.length; j++) {
        this.fixParams[i][j] = new String(TextGenerator.randomFastChars(rng(), this.data));
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll(";", "!");
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll("\"", ".");
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll("'", "~");
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll("#", "~");
      }
    }

    synchronized (YCSBWorker.class) {
      // We must know where to start inserting
      if (insertRecord == null) {
        insertRecord = new CounterGenerator(init_record_count);
      }
    }

    // This is a minor speed-up to avoid having to invoke the hashmap look-up
    // everytime we want to execute a txn. This is important to do on
    // a client machine with not a lot of cores
    this.procUpdateRecord = this.getProcedure(UpdateRecord.class);
    this.procScanRecord = this.getProcedure(ScanRecord.class);
    this.procReadRecord = this.getProcedure(ReadRecord.class);
    this.procReadModifyWriteRecord = this.getProcedure(ReadModifyWriteRecord.class);
    this.procInsertRecord = this.getProcedure(InsertRecord.class);
    this.procDeleteRecord = this.getProcedure(DeleteRecord.class);
    this.procReadWriteRecord = this.getProcedure(ReadWriteRecord.class);
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTrans)
      throws Procedure.UserAbortException, SQLException {
    this.tid = (System.nanoTime() << 10) | (Thread.currentThread().getId() & 0x3ff);
    Class<? extends Procedure> procClass = nextTrans.getProcedureClass();

    if (procClass.equals(DeleteRecord.class)) {
      deleteRecord(conn);
    } else if (procClass.equals(InsertRecord.class)) {
      insertRecord(conn);
    } else if (procClass.equals(ReadModifyWriteRecord.class)) {
      readModifyWriteRecord(conn);
    } else if (procClass.equals(ReadRecord.class)) {
      readRecord(conn);
    } else if (procClass.equals(ScanRecord.class)) {
      scanRecord(conn);
    } else if (procClass.equals(UpdateRecord.class)) {
      updateRecord(conn);
    } else if (procClass.equals(ReadWriteRecord.class)) {
      readWriteRead(conn);
    }
    return (TransactionStatus.SUCCESS);
  }

  @Override
  protected void executeAfterWork(TransactionType txnType, boolean success) throws Procedure.UserAbortException, SQLException {
    if (TAdapter.getInstance().isInSwitchPhase() && !TAdapter.getInstance().isAllWorkersReadyForSwitch()) {
      switchPhaseReady = true;
    }
    Class<? extends Procedure> procClass = txnType.getProcedureClass();
    if (procClass.equals(ReadWriteRecord.class)) {
      // release validation locks if needs
      this.procReadWriteRecord.doAfterCommit(keynames, TAdapter.getInstance().getCCType(), success, versionBuffer, tid);
    }

    while(TAdapter.getInstance().isInSwitchPhase() && !TAdapter.getInstance().isAllWorkersReadyForSwitch()) {
      if (!this.switchPhaseReady) {
        this.switchPhaseReady = true;
      }

      try {
        Thread.sleep(1L);
      } catch (InterruptedException var5) {
      }
    }
  }

  private void updateRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.buildParameters();
    this.procUpdateRecord.run(conn, keyname, this.params);
  }

  private void scanRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    int count = 10;
    this.procScanRecord.run(conn, keyname, count, new ArrayList<>());
  }

  private void readRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.procReadRecord.run(conn, keyname, this.results);
  }

  private void readModifyWriteRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.buildParameters();
    this.procReadModifyWriteRecord.run(conn, keyname, this.params, this.results);
  }

  private void insertRecord(Connection conn) throws SQLException {
    int keyname = insertRecord.nextInt();
    this.buildParameters();
    this.procInsertRecord.run(conn, keyname, this.params);
  }

  private void deleteRecord(Connection conn) throws SQLException {
    int keyname = readRecord.nextInt();
    this.procDeleteRecord.run(conn, keyname);
  }

  private boolean isDuplicatedKey(int key, int slot, int[] keyNames) {
    for (int i = 0; i < slot; i++) {
      if (key == keyNames[i])
        return true;
    }
    return false;
  }

  private void readWriteRead(Connection conn) throws SQLException {
    for (int i = 0; i < totalRequest; i++) {
      int keyname = 0;

      do {
        keyname = readRecord.nextInt();
      } while (isDuplicatedKey(keyname, i, keynames));

      keynames[i] = keyname;
    }

    this.procReadWriteRecord.run(this, conn, this.conn2, keynames, fixParams, ratio1, ratio2, tid, versionBuffer, TAdapter.getInstance().getCCType());
  }

  private void buildParameters() {
    for (int i = 0; i < this.params.length; i++) {
      this.params[i] = new String(TextGenerator.randomFastChars(rng(), this.data));
      this.params[i] = this.params[i].replaceAll(";", "!");
      this.params[i] = this.params[i].replaceAll("\"", ".");
      this.params[i] = this.params[i].replaceAll("'", "~");
      this.params[i] = this.params[i].replaceAll("#", "~");
    }
  }
}
