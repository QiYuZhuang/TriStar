#!/usr/bin/python3
import json

transactionType = [
    "NewOrder",
    "Payment",
    "OrderStatus",
    "Delivery",
    "StockLevel",
]


def get_transaction_idx(txn_name: str):
    if txn_name in transactionType:
        return transactionType.index(txn_name)
    else:
        return -1


class Summary(object):
    filepath: str
    execute_ts: int
    benchmark: str
    scale_factor: int
    dbms_version: str
    terminals: int
    throughput: float
    good_throughput: float
    # latency in ms
    max_latency: float
    avg_latency: float
    min_latency: float
    p25_latency: float
    p50_latency: float
    p75_latency: float
    p90_latency: float
    p95_latency: float
    p99_latency: float
    cc_type: str

    def __new__(cls, filepath: str, cc_type="SER"):
        instance = object.__new__(cls)
        cls.filepath = filepath
        cls.cc_type = cc_type
        return instance

    def __init__(self, filepath: str, cc_type="SER"):
        self.cc_type = cc_type
        self.filepath = filepath
        with open(filepath) as f:
            data = json.load(f)

        self.execute_ts = data["Elapsed Time (nanoseconds)"]
        self.dbms_version = data["DBMS Version"]
        self.benchmark = data["Benchmark Type"]
        measured_requests = data["Measured Requests"]
        self.scale_factor = data["scalefactor"]
        self.terminals = data["terminals"]
        self.throughput = data["Throughput (requests/second)"]
        self.good_throughput = data["Goodput (requests/second)"]

        latency_distribution = data["Latency Distribution"]
        self.max_latency = latency_distribution["Maximum Latency (microseconds)"] / 1000
        self.avg_latency = latency_distribution["Average Latency (microseconds)"] / 1000
        self.min_latency = latency_distribution["Minimum Latency (microseconds)"] / 1000
        self.p25_latency = latency_distribution["25th Percentile Latency (microseconds)"] / 1000
        self.p50_latency = latency_distribution["Median Latency (microseconds)"] / 1000
        self.p75_latency = latency_distribution["75th Percentile Latency (microseconds)"] / 1000
        self.p90_latency = latency_distribution["90th Percentile Latency (microseconds)"] / 1000
        self.p95_latency = latency_distribution["95th Percentile Latency (microseconds)"] / 1000
        self.p99_latency = latency_distribution["99th Percentile Latency (microseconds)"] / 1000

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            return self.terminals < other.terminals
        return False


class NewOrderSummary(Summary):
    no_ratio: float
    skew: float
    txn_name = "neworder"

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        s_idx, c_idx = tokens.index("zipf"), tokens.index("cc")
        self.skew = float(tokens[s_idx + 1])
        no_idx = tokens.index(self.txn_name)
        self.no_ratio = float(str(tokens[no_idx + 1]))
        super().__init__(filepath, '_'.join(tokens[c_idx + 1:]))
        print("read neworder summary: {", self.no_ratio, self.cc_type, "}")

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            if self.skew < other.skew:
                return True
            elif abs(self.skew - other.skew) < 1e-5:
                return self.no_ratio < other.no_ratio
        return False
    
class PaymentSummary(Summary):
    pa_ratio: float
    skew: float
    txn_name = "payment"

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        s_idx, c_idx = tokens.index("zipf"), tokens.index("cc")
        self.skew = float(tokens[s_idx + 1])
        pa_idx = tokens.index(self.txn_name)
        self.pa_ratio = float(str(tokens[pa_idx + 1]))
        super().__init__(filepath, '_'.join(tokens[c_idx + 1:]))
        print("read payment summary: {", self.pa_ratio, self.cc_type, "}")

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            if self.skew < other.skew:
                return True
            elif abs(self.skew - other.skew) < 1e-5:
                return self.pa_ratio < other.pa_ratio
        return False

class SkewSummary(Summary):
    skew: float

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        print(tokens)
        s_idx, c_idx = tokens.index("zipf"), tokens.index("cc")
        self.skew = float(tokens[s_idx + 1])
        super().__init__(filepath, '_'.join(tokens[c_idx + 1:]))
        print("read skew summary: {", self.skew, self.cc_type, "}")

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            if self.skew < other.skew:
                return True
        return False
