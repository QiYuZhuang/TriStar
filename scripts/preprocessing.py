#!/usr/bin/python3
from typing import List

import numpy as np
import pandas as pd
import os
import sys
from datetime import datetime
from summary import *
from YCSBSummary import *
from TPCCSummary import *


def create_output_file(filepath: str):
    file_name = "summary.res"
    file_path = os.path.join(filepath, file_name)
    open(file_path, 'w').close()

    sys.stdout = open(file_path, 'w')

    return file_path


def refresh_output_channel():
    sys.stdout.close()
    sys.stdout = sys.__stdout__


def read_files(case_name: str, ts=""):
    results = []
    case_dir = ""
    if len(ts) > 0:
        case_dir = case_name + "/" + ts
    else:
        ts = [f.path for f in os.scandir(case_name) if f.is_dir()]
        ts.sort(key=lambda x: datetime.strptime(os.path.basename(x), '%Y-%m-%d-%H-%M-%S'))
        case_dir = ts[-1]

    exps = []
    for d in os.scandir(case_dir):
        if d.is_dir():
            exps.append(d)

    return generate_summary(exps=exps, case_name=case_name), case_dir


def generate_summary(exps: List, case_name: str) -> List[Summary]:
    results = []
    ycsb_case: bool = False
    if case_name.__contains__("ycsb"):
        ycsb_case = True

    for e in exps:
        for f in os.scandir(e):
            if f.name.__contains__("summary"):
                if case_name.__contains__("scalability"):
                    if ycsb_case:
                        results.append(YCSBBaseSummary(f.path))
                    else:
                        results.append(ScalabilitySummary(f.path))
                elif case_name.__contains__("skew"):
                    if ycsb_case:
                        results.append(YCSBBaseSummary(f.path))
                    else:
                        results.append(SkewSummary(f.path))
                elif case_name.__contains__("hotspot"):
                    results.append(HotspotSummary(f.path))
                elif case_name.__contains__("bal_ratio"):
                    results.append(BalanceSummary(f.path))
                elif case_name.__contains__("wc_ratio"):
                    results.append(WriteCheckSummary(f.path))
                elif case_name.__contains__("wr_ratio"):
                    results.append(YCSBBaseSummary(f.path))
                elif case_name.__contains__("rate"):
                    if ycsb_case:
                        results.append(YCSBRateSummary(f.path))
                    else:
                        results.append(RateSummary(f.path))
                elif case_name.__contains__("no_ratio"):
                    results.append(NewOrderSummary(f.path))
                elif case_name.__contains__("pa_ratio"):
                    results.append(PaymentSummary(f.path))
                elif case_name.__contains__("random"):
                    if ycsb_case:
                        print("can not find Summary class for " + case_name)
                    else:
                        results.append(RandomSummary(f.path))
                else:
                    print("can not find Summary class for " + case_name)
    return results

def process_summary(summaries: List[Summary], case_name: str):
    d: dict[str, List[Summary]] = {}
    d1: dict[str, str] = {}
    d2: dict[str, str] = {}
    d3: dict[str, str] = {}

    for r in summaries:
        if d.get(r.cc_type) is not None:
            d[r.cc_type].append(r)
        else:
            d[r.cc_type] = [r]

    group_per_case = group_num(case_name)
    for key, rs in d.items():
        num_per_case = len(rs) // group_per_case
        idx = 0
        if d1.get(key) is None:
            d1[key] = "["
            d2[key] = "["
            d3[key] = "["
        for r in rs:
            if idx % num_per_case == 0:
                d1[key] += "[" + str(r.good_throughput)
                d2[key] += "[" + str(r.p50_latency)
                d3[key] += "[" + str(r.p95_latency)
            else:
                d1[key] += (", " + str(r.good_throughput))
                d2[key] += (", " + str(r.p50_latency))
                d3[key] += (", " + str(r.p95_latency))

            idx += 1
            if idx % num_per_case == 0:
                d1[key] += "], "
                d2[key] += "], "
                d3[key] += "], "
        d1[key] += "]"
        d2[key] += "]"
        d3[key] += "]"

    print("=" * 20 + " performance " + "=" * 20)
    for key, value in d1.items():
        value += ","
        print("\"" + key + "\"", ":", value)

    print("=" * 20 + " p50 " + "=" * 20)
    for key, value in d2.items():
        value += ","
        print("\"" + key + "\"", ":", value)

    print("=" * 20 + " p95 " + "=" * 20)
    for key, value in d3.items():
        value += ","
        print("\"" + key + "\"", ":", value)


def group_num(case_name: str) -> int:
    if case_name.__contains__("scalability"):
        return 1
    elif case_name.__contains__("skew"):
        return 1
    elif case_name.__contains__("hotspot"):
        return 3
    elif case_name.__contains__("bal_ratio"):
        return 3
    elif case_name.__contains__("wc_ratio"):
        return 3
    elif case_name.__contains__("rate") and not case_name.__contains__("ycsb"):
        return 3
    else:
        return 1


def print_key(summaries: List[Summary]):
    res = ""
    idx: int = 0
    for r in summaries:
        if isinstance(r, HotspotSummary):
            res += str(r.hotspot) + "-" + str(r.percentage) + ", "
        elif isinstance(r, SkewSummary):
            res += str(r.skew) + ", "
        elif isinstance(r, BalanceSummary):
            res += str(r.bal_ratio) + ", "
        elif isinstance(r, WriteCheckSummary):
            res += str(r.wc_ratio) + ", "
        elif isinstance(r, RateSummary):
            res += str(r.rate) + ", "
        idx += 1
        if idx % 10 == 0:
            res += "\n"
    print(res)


if __name__ == "__main__":
    path = "../results/skew-128"
    args = sys.argv[1:]
    if len(args) > 0:
        path = args[0]

    res, case_path = read_files(path)
    res.sort()
    create_output_file(case_path)
    process_summary(res, path)
    refresh_output_channel()
