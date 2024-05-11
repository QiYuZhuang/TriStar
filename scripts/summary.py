#!/usr/bin/python3
import json


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


class HotspotSummary(Summary):
    hotspot: int
    percentage: float

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        h_idx, p_idx = tokens.index("hotspot"), tokens.index("pro")
        self.hotspot = int(tokens[h_idx + 1])
        self.percentage = float(tokens[p_idx + 1])
        super().__init__(filepath, '_'.join(tokens[p_idx + 2:]))
        print("read hotspot summary: {", self.hotspot, self.percentage, self.cc_type, "}")

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            if self.hotspot < other.hotspot:
                return True
            elif self.hotspot == other.hotspot:
                if self.terminals < other.terminals:
                    return True
                elif self.terminals == other.terminals:
                    return self.percentage < other.percentage
        return False


class SkewSummary(Summary):
    skew: float

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        s_idx = tokens.index("skew")
        self.skew = float(tokens[s_idx + 1])
        super().__init__(filepath, '_'.join(tokens[s_idx + 2:]))
        print("read skew summary: {", self.skew, self.cc_type, "}")

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            if self.skew < other.skew:
                return True
        return False
