package org.dbiir.tristar.adapter;

import lombok.Getter;
import lombok.Setter;
import org.dbiir.tristar.benchmarks.api.BenchmarkModule;
import org.dbiir.tristar.benchmarks.api.Worker;
import org.dbiir.tristar.common.CCType;

import java.util.ArrayList;
import java.util.List;

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

    static {
        INSTANCE = new TAdapter();
    }

    public TAdapter() {

    }

    public CCType getCCType() {
        if (inSwitchPhase)
            return switchPhaseCCType;
        else
            return currentCCType;
    }

    public static TAdapter getInstance() {
        return INSTANCE;
    }

    public void setNextCCType(CCType ccType) {
        if (!used) {
            return;
        }
        nextCCType = ccType;
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
            endTime = System.currentTimeMillis();
            System.out.println("=====*===== Switch phase takes " + (endTime - startTime) + "ms");
            nextCCType = CCType.NUM_CC;
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
