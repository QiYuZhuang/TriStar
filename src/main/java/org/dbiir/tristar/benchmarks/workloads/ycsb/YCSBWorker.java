//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.dbiir.tristar.benchmarks.workloads.ycsb;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import org.dbiir.tristar.adapter.TAdapter;
import org.dbiir.tristar.benchmarks.api.Procedure;
import org.dbiir.tristar.benchmarks.api.TransactionType;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.benchmarks.distributions.CounterGenerator;
import org.dbiir.tristar.benchmarks.distributions.ZipfianGenerator;
import org.dbiir.tristar.benchmarks.types.TransactionStatus;
import org.dbiir.tristar.benchmarks.util.TextGenerator;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.DeleteRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.InsertRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.ReadModifyWriteRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.ReadRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.ReadWriteRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.ScanRecord;
import org.dbiir.tristar.benchmarks.workloads.ycsb.procedures.UpdateRecord;
import org.dbiir.tristar.common.CCType;

class YCSBWorker extends Worker<YCSBBenchmark> {
  private ZipfianGenerator readRecord;
  private static CounterGenerator insertRecord;
  private final char[] data;
  private final String[] params = new String[10];
  private final String[][] fixParams = new String[10][10];
  private final String[] results = new String[10];
  private final UpdateRecord procUpdateRecord;
  private final ScanRecord procScanRecord;
  private final ReadRecord procReadRecord;
  private final ReadModifyWriteRecord procReadModifyWriteRecord;
  private final InsertRecord procInsertRecord;
  private final DeleteRecord procDeleteRecord;
  private final ReadWriteRecord procReadWriteRecord;
  private final int totalRequest = 10;
  private double ratio1;
  private double ratio2;
  private final long[] versionBuffer;
  private int[] keynames = new int[10];
  private long tid;
  private final int init_record_count;
  // private final int phaseNum = 8;
  // private final double[] zipfPhases = new double[]{0.1, 0.1, 0.1, 1.3, 0.7, 1.3, 0.7, 1.1};
  // private final double[] ratio1Phases = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
  // private final double[] ratio2Phases = new double[]{0.1, 0.1, 0.1, 0.95, 0.05, 0.1, 0.1, 0.1};
  private final int phaseNum = 4;
  private final double[] zipfPhases = new double[]{0.1, 0.1, 1.3, 0.7};
  private final double[] ratio1Phases = new double[]{1.0, 1.0, 1.0, 1.0};
  private final double[] ratio2Phases = new double[]{0.1, 0.1, 0.1, 0.1};
  private final int phaseInterval = 10000;
  private int phaseCount = 0;
  private long lastPhaseTimestamp = 0L;
  private final boolean dynamic = false;

  public YCSBWorker(YCSBBenchmark benchmarkModule, int id, int init_record_count) {
    super(benchmarkModule, id);
    this.data = new char[benchmarkModule.fieldSize];
    this.ratio1 = benchmarkModule.wrtxn;
    this.ratio2 = benchmarkModule.wrtup;
    this.init_record_count = init_record_count;
    this.readRecord = new ZipfianGenerator(this.rng(), (long)init_record_count, benchmarkModule.zipf);
    this.versionBuffer = new long[10];

    for(int i = 0; i < 10; ++i) {
      for(int j = 0; j < this.params.length; ++j) {
        this.fixParams[i][j] = new String(TextGenerator.randomFastChars(this.rng(), this.data));
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll(";", "!");
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll("\"", ".");
        this.fixParams[i][j] = this.fixParams[i][j].replaceAll("'", "~");
      }
    }

    synchronized (YCSBWorker.class) {
      // We must know where to start inserting
      if (insertRecord == null) {
        insertRecord = new CounterGenerator(init_record_count);
      }
    }

    this.procUpdateRecord = (UpdateRecord)this.getProcedure(UpdateRecord.class);
    this.procScanRecord = (ScanRecord)this.getProcedure(ScanRecord.class);
    this.procReadRecord = (ReadRecord)this.getProcedure(ReadRecord.class);
    this.procReadModifyWriteRecord = (ReadModifyWriteRecord)this.getProcedure(ReadModifyWriteRecord.class);
    this.procInsertRecord = (InsertRecord)this.getProcedure(InsertRecord.class);
    this.procDeleteRecord = (DeleteRecord)this.getProcedure(DeleteRecord.class);
    this.procReadWriteRecord = (ReadWriteRecord)this.getProcedure(ReadWriteRecord.class);
  }

  @Override
  protected TransactionStatus executeWork(Connection conn, TransactionType nextTrans) throws Procedure.UserAbortException, SQLException {
    this.checkTransferPhase();
    this.realTimeCCType = CCType.NUM_CC;
    this.validationFinish = false;
    this.executionFinish = false;
    this.tid = ((System.nanoTime() << 10) & Long.MAX_VALUE) | Thread.currentThread().getId() & 1023L;
    Class<? extends Procedure> procClass = nextTrans.getProcedureClass();

    if (procClass.equals(DeleteRecord.class)) {
      this.deleteRecord(conn);
    } else if (procClass.equals(InsertRecord.class)) {
      this.insertRecord(conn);
    } else if (procClass.equals(ReadModifyWriteRecord.class)) {
      this.readModifyWriteRecord(conn);
    } else if (procClass.equals(ReadRecord.class)) {
      this.readRecord(conn);
    } else if (procClass.equals(ScanRecord.class)) {
      this.scanRecord(conn);
    } else if (procClass.equals(UpdateRecord.class)) {
      this.updateRecord(conn);
    } else if (procClass.equals(ReadWriteRecord.class)) {
      this.readWriteRead(conn);
    }

    return TransactionStatus.SUCCESS;
  }

  private void checkTransferPhase() {
    if (!dynamic) {
      return;
    }
    Objects.requireNonNull(this);
    if (this.lastPhaseTimestamp == 0L) {
      this.lastPhaseTimestamp = System.currentTimeMillis();
      this.ratio1 = this.ratio1Phases[0];
      this.ratio2 = this.ratio2Phases[0];
      this.readRecord = new ZipfianGenerator(this.rng(), (long)this.init_record_count, this.zipfPhases[0]);
    } else {
      long var10000 = System.currentTimeMillis() - this.lastPhaseTimestamp;
      Objects.requireNonNull(this);
      if (var10000 > phaseInterval) {
        this.lastPhaseTimestamp = System.currentTimeMillis();
        ++this.phaseCount;
        int var1 = this.phaseCount;
        Objects.requireNonNull(this);
        if (var1 >= phaseNum) {
          this.phaseCount = 0;
        }

        this.ratio1 = this.ratio1Phases[this.phaseCount];
        this.ratio2 = this.ratio2Phases[this.phaseCount];
        this.readRecord = new ZipfianGenerator(this.rng(), (long)this.init_record_count, this.zipfPhases[this.phaseCount]);
        System.out.println("phase: " + this.phaseCount + " ratio1: " + this.ratio1 + " ratio2: " + this.ratio2 + " skew: " + this.zipfPhases[this.phaseCount]);
      }
    }

  }

  @Override
  protected void executeAfterWork(TransactionType txnType, boolean success, long latency) throws Procedure.UserAbortException, SQLException {
    if (TAdapter.getInstance().isInSwitchPhase() && !TAdapter.getInstance().isAllWorkersReadyForSwitch()) {
      System.out.println(Thread.currentThread().getName() + " is ready " + System.currentTimeMillis());
      this.switchPhaseReady = true;
    }
    Class<? extends Procedure> procClass = txnType.getProcedureClass();
    if (procClass.equals(ReadWriteRecord.class)) {
      if (this.realTimeCCType != CCType.NUM_CC) {
        this.procReadWriteRecord.doAfterCommit(this.keynames, this.realTimeCCType, success, this.validationFinish, this.versionBuffer, this.tid, oldTransaction(), latency);
      } else {
        this.procReadWriteRecord.doAfterCommit(this.keynames, TAdapter.getInstance().getCCType(), success, this.validationFinish, this.versionBuffer, this.tid, oldTransaction(), latency);
      }
    } else if (procClass.equals(ReadRecord.class)) {
      this.procReadRecord.doAfterCommit(this.keynames, success, latency);;
    } else if (procClass.equals(UpdateRecord.class)) {
      this.procUpdateRecord.doAfterCommit(this.keynames, success, latency);
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
    for (int i = 0; i < totalRequest; i++) {
      int keyname = 0;

      do {
        keyname = this.readRecord.nextInt();
      } while(this.isDuplicatedKey(keyname, i, this.keynames));

      this.keynames[i] = keyname;
    }

    this.procUpdateRecord.run(conn, this.keynames, this.fixParams);
  }

  private void scanRecord(Connection conn) throws SQLException {
    int keyname = this.readRecord.nextInt();
    int count = 10;
    this.procScanRecord.run(conn, keyname, count, new ArrayList());
  }

  private void readRecord(Connection conn) throws SQLException {
    for (int i = 0; i < totalRequest; i++) {
      int keyname = 0;

      do {
        keyname = this.readRecord.nextInt();
      } while(this.isDuplicatedKey(keyname, i, this.keynames));

      this.keynames[i] = keyname;
    }
    this.procReadRecord.run(conn, this.keynames);
  }

  private void readModifyWriteRecord(Connection conn) throws SQLException {
    int keyname = this.readRecord.nextInt();
    this.buildParameters();
    this.procReadModifyWriteRecord.run(conn, keyname, this.params, this.results);
  }

  private void insertRecord(Connection conn) throws SQLException {
    int keyname = insertRecord.nextInt();
    this.buildParameters();
    this.procInsertRecord.run(conn, keyname, this.params);
  }

  private void deleteRecord(Connection conn) throws SQLException {
    int keyname = this.readRecord.nextInt();
    this.procDeleteRecord.run(conn, keyname);
  }

  private boolean isDuplicatedKey(int key, int slot, int[] keyNames) {
    for(int i = 0; i < slot; ++i) {
      if (key == keyNames[i]) {
        return true;
      }
    }
    return false;
  }

  private void readWriteRead(Connection conn) throws SQLException {
    CCType ccType = CCType.NUM_CC;
    for (int i = 0; i < totalRequest; i++) {
      int keyname = 0;

      do {
        keyname = this.readRecord.nextInt();
      } while(this.isDuplicatedKey(keyname, i, this.keynames));

      this.keynames[i] = keyname;
    }

    this.procReadWriteRecord.run(this, conn, this.conn2, keynames, fixParams, ratio1, ratio2, tid, versionBuffer, TAdapter.getInstance().getCCType());
  }

  private void buildParameters() {
    for(int i = 0; i < this.params.length; ++i) {
      this.params[i] = new String(TextGenerator.randomFastChars(this.rng(), this.data));
      this.params[i] = this.params[i].replaceAll(";", "!");
      this.params[i] = this.params[i].replaceAll("\"", ".");
      this.params[i] = this.params[i].replaceAll("'", "~");
    }
  }
}
