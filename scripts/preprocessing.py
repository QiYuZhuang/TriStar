#!/usr/bin/python3
from typing import List

import numpy as np
import pandas as pd
import os
import sys
from datetime import datetime
from summary import *


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

    for e in exps:
        for f in os.scandir(e):
            if f.name.__contains__("summary"):
                if case_name.__contains__("scalability"):
                    results.append(ScalabilitySummary(f.path))
                elif case_name.__contains__("skew"):
                    results.append(SkewSummary(f.path))
                elif case_name.__contains__("hotspot"):
                    results.append(HotspotSummary(f.path))
                elif case_name.__contains__("bal_ratio"):
                    results.append(BalanceSummary(f.path))
                elif case_name.__contains__("wc_ratio"):
                    results.append(WriteCheckSummary(f.path))
                elif case_name.__contains__("rate"):
                    results.append(RateSummary(f.path))
                else:
                    print("can not find Summary class for " + case_name)
    return results, case_dir


def process_summary(summaries: List[Summary]):
    d1: dict[str, str] = {}
    d2: dict[str, str] = {}
    d3: dict[str, str] = {}

    print_key(summaries)
    for r in summaries:
        if d1.get(r.cc_type) is not None:
            d1[r.cc_type] += (", " + str(r.good_throughput))
            d2[r.cc_type] += (", " + str(r.p50_latency))
            d3[r.cc_type] += (", " + str(r.p95_latency))
        else:
            d1[r.cc_type] = "[" + str(r.good_throughput)
            d2[r.cc_type] = "[" + str(r.p50_latency)
            d3[r.cc_type] = "[" + str(r.p95_latency)

    print("=" * 20 + " performance " + "=" * 20)
    for key, value in d1.items():
        value += "]"
        print("\"" + key + "\"", ": ", value)

    print("=" * 20 + " p50 " + "=" * 20)
    for key, value in d2.items():
        value += "]"
        print("\"" + key + "\"", ": ", value)

    print("=" * 20 + " p95 " + "=" * 20)
    for key, value in d3.items():
        value += "]"
        print("\"" + key + "\"", ": ", value)


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
    process_summary(res)
    refresh_output_channel()
