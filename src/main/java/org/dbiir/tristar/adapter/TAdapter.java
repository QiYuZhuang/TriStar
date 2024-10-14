package org.dbiir.tristar.adapter;

import java.util.List;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.dbiir.tristar.benchmarks.api.BenchmarkModule;
import org.dbiir.tristar.benchmarks.api.TransactionType;
import org.dbiir.tristar.benchmarks.api.TransactionTypes;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.common.CCType;
import org.dbiir.tristar.transaction.concurrency.LockTable;

import lombok.Getter;
import lombok.Setter;

public class TAdapter {
    private static final TAdapter INSTANCE;
    @Setter
    private boolean used = false; // true if DYNAMIC
    private CCType currentCCType = CCType.SER;
    @Getter
    private CCType nextCCType;
    @Getter
    private CCType switchPhaseCCType = CCType.NUM_CC;
    @Setter
    private List<Worker<? extends BenchmarkModule>> workers;
    @Setter
    private BenchmarkModule benchmark;
    @Getter
    private boolean inSwitchPhase;
    @Getter
    private boolean allWorkersReadyForSwitch;
    private ListOrderedMap<Integer, TransactionType> typesById = new ListOrderedMap<>();
    private ListOrderedMap<String, TransactionType> typesByName = new ListOrderedMap<>();

    static {
        INSTANCE = new TAdapter();
    }

    public TAdapter() {

    }

    public TransactionType getTypesByName(String name) {
        return this.typesByName.get(name.toUpperCase());
    }

    public TransactionType getTypesById(int id) {
        return this.typesById.get(Integer.valueOf(id));
    }

    public void initTransactionTypes(List<TransactionType> transactionTypeList) {
        transactionTypeList.sort(TransactionType::compareTo);
        for (TransactionType tt : transactionTypeList) {
            String key = tt.getName().toUpperCase();
            this.typesByName.put(key, tt); 
            this.typesById.put(tt.getId(), tt);
        }
    }

    public CCType getCCType() {
        if (!used) {
            return benchmark.getCCType();
        }
        if (inSwitchPhase)
            return switchPhaseCCType;
        else
            return currentCCType;
    }

    public CCType getCCType(boolean old) {
        if (!used) {
            return benchmark.getCCType();
        }
        if (!inSwitchPhase) {
            return currentCCType;
        }
        if (nextCCType == CCType.NUM_CC) {
            System.out.println("Can't switch isolation level when nextCCType = NUM_CC");
        }
        if (old)
            return currentCCType;
        else
            return nextCCType;
    }

    public static TAdapter getInstance() {
        return INSTANCE;
    }

    public boolean blockForSwitch() {
        if (inSwitchPhase && benchmark.getCCType() == CCType.DYNAMIC_B) {
            return true;
        }
        return false;
    }

    public boolean abortForSwitch() {
        if (inSwitchPhase && benchmark.getCCType() == CCType.DYNAMIC_A) {
            return true;
        }
        return false;
    }

    public boolean mixForSwitch() {
        if (inSwitchPhase && benchmark.getCCType() == CCType.DYNAMIC) {
            return true;
        }
        return false;
    }

    public void setNextCCType(CCType ccType) {
        if (!used) {
            System.out.println("Can't switch isolation level when used = false");
            return;
        }
        nextCCType = ccType;
        System.out.println("Switch to " + nextCCType + " current: " + currentCCType);
        if (nextCCType == currentCCType) {
            nextCCType = CCType.NUM_CC;
        } else {
            // TODO: switch to new isolation level
            /*
             * 1. (not necessary) set the isolation level in benchmark module
             * 2. notify all workers the new isolation to block the transaction that no enter validation: transition phase
             * 3. travel through the worker list to determine whether all workers finish the switch
             * 4. notify all workers to use the validation in new isolation level
             */
            if (benchmark.getCCType() == CCType.DYNAMIC) {
                System.out.println("=====*===== Switch from " + currentCCType + " to " + nextCCType);
                long startTime = System.currentTimeMillis();
                setSwitchPhaseCCType();
                inSwitchPhase = true;
                while (!allSwitchPhaseReady() && !Thread.interrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted");
                    }
                }
                allWorkersReadyForSwitch = true;
                long endTime = System.currentTimeMillis();
                System.out.println("=====*===== All workers ready for switch" + (endTime - startTime) + "ms");
                while (!allSwitchFinish() && !Thread.interrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted");
                    }
                }
                currentCCType = nextCCType;
                inSwitchPhase = false;
                allWorkersReadyForSwitch = false;
                resetWorkerStatus();
                endTime = System.currentTimeMillis();
                System.out.println("=====*===== All workers finish switchment" + (endTime - startTime) + "ms");
                LockTable.getInstance().refreshSwitchValidation(benchmark.getBenchmarkName());
                endTime = System.currentTimeMillis();
                System.out.println("=====*===== Switch phase takes " + (endTime - startTime) + "ms");
                nextCCType = CCType.NUM_CC;
            } else if (benchmark.getCCType() == CCType.DYNAMIC_B) {
                System.out.println("=====*===== Switch from " + currentCCType + " to " + nextCCType);
                long startTime = System.currentTimeMillis();
                inSwitchPhase = true;
                allWorkersReadyForSwitch = true;
                while (!allSwitchFinish() && !Thread.interrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted");
                    }
                }
                System.out.println("=====*===== All workers finish switchment");
                currentCCType = nextCCType;
                inSwitchPhase = false;
                resetWorkerStatus();
                long endTime = System.currentTimeMillis();
                System.out.println("=====*===== Switch phase takes " + (endTime - startTime) + "ms");
                nextCCType = CCType.NUM_CC;
            } else if (benchmark.getCCType() == CCType.DYNAMIC_A) {
                System.out.println("=====*===== Switch from " + currentCCType + " to " + nextCCType);
                long startTime = System.currentTimeMillis();
                inSwitchPhase = true;
                allWorkersReadyForSwitch = true;
                while (!allAbortSwitchFinish() && !Thread.interrupted()) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("Thread interrupted");
                    }
                }
                System.out.println("=====*===== All workers finish switchment");
                currentCCType = nextCCType;
                inSwitchPhase = false;
                resetWorkerStatus();
                long endTime = System.currentTimeMillis();
                System.out.println("=====*===== Switch phase takes " + (endTime - startTime) + "ms");
                nextCCType = CCType.NUM_CC;
            }
        }
    }

    public void setNextCCType(String ccType) {
        ccType = ccType.trim();
        if (ccType.equals("0")) {
            this.setNextCCType(CCType.SER);
        } else if (ccType.equals("1")) {
            this.setNextCCType(CCType.SI_TAILOR);
        } else if (ccType.equals("2")) {
            this.setNextCCType(CCType.RC_TAILOR);
        } else {
            System.out.println("ERROR");
        }
    }

    private boolean allSwitchFinish() {
        for (Worker worker: workers) {
            if (!worker.isSwitchFinish()) {
                System.out.println("worker #" + worker.getId() + " is not finished");
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    System.out.println("Thread.sleep() interrupted");
                }
                return false;
            }
        }
        return true;
    }

    private boolean allAbortSwitchFinish() {
        for (Worker worker: workers) {
            if (!worker.isSwitchFinish()) {
                if (worker.isPreSleep() || worker.isPostSleep()) {
                    worker.setNeedAbort(true);
                    continue;
                }
                System.out.println("worker #" + worker.getId() + " is not finished");
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    System.out.println("Thread.sleep() interrupted");
                }
                return false;
            }
        }
        return true;
    }

    private void resetWorkerStatus() {
        for (Worker worker: workers) {
            worker.setSwitchPhaseReady(false);
            worker.setSwitchFinish(false);
        }
    }

    private boolean allSwitchPhaseReady() {
        // ready if its validation is
        for (Worker worker: workers) {
            if (!worker.isSwitchPhaseReady()) {
                if (!worker.isExecutionFinish()) {
                    // worker.setSwitchPhaseReady(true);
                    System.out.println("worker #" + worker.getId() + " set to be ready");
                    continue;
                }
                System.out.println("worker #" + worker.getId() + " is not ready, execution finished: " + worker.isExecutionFinish() + ", " + System.currentTimeMillis());
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                }
                return false;
            }
        }
        return true;
    }

    private void setSwitchPhaseCCType() {
        if (underRC(currentCCType) || underRC(nextCCType)) {
            this.switchPhaseCCType = CCType.RC_TAILOR;
        } else if (underSI(currentCCType) || underSI(nextCCType)) {
            this.switchPhaseCCType = CCType.SI_TAILOR;
        } else {
            this.switchPhaseCCType = CCType.SER;
        }
    }

    private boolean underRC(CCType type) {
        return type == CCType.RC || type == CCType.RC_TAILOR_LOCK || type == CCType.RC_TAILOR || type == CCType.RC_FOR_UPDATE || type == CCType.RC_ELT;
    }

    private boolean underSI(CCType type) {
        return type == CCType.SI || type == CCType.SI_TAILOR || type == CCType.SI_FOR_UPDATE || type == CCType.SI_ELT;
    }
}
