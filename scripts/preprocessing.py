#!/usr/bin/python3
from typing import List

import numpy as np
import pandas as pd
import os
from datetime import datetime
from summary import Summary, HotspotSummary, SkewSummary


def read_hotspots(case_name: str, ts=""):
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
                results.append(HotspotSummary(f.path))
    return results


def read_skews(case_name: str, ts=""):
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
                results.append(SkewSummary(f.path))
    return results


def process_summary(summarys: List[Summary]):
    d: dict[str, str] = {}
    for r in summarys:
        if isinstance(r, HotspotSummary):
            print(r.percentage)
        if d.get(r.cc_type) is not None:
            d[r.cc_type] += (", " + str(r.good_throughput))
        else:
            d[r.cc_type] = "[" + str(r.good_throughput)

    for key, value in d.items():
        value += "]"
        print(key, value)


if __name__ == "__main__":
    path = "../results/smallbank/hotspot-64"
    res = read_hotspots(path)
    res.sort()
    process_summary(res)
