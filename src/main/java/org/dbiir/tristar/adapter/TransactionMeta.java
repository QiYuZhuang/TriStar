package org.dbiir.tristar.adapter;

import org.dbiir.tristar.benchmarks.api.TransactionType;
import org.dbiir.tristar.benchmarks.catalog.RWRecord;

import java.util.HashMap;
import java.util.Map;

public record TransactionMeta(int id, RWRecord[] rset, RWRecord[] wset, float processing) {
//    static String featureFormat = "%d,%.2f";
    static String featureFormat = "%d";
    public String transactionFeature() {
//        return featureFormat.formatted(id, processing);
        return featureFormat.formatted(id);
    }
}
