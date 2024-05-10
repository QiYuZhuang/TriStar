#!/usr/bin/python3

import numpy as np
import pandas as pd
import os
from datetime import datetime
from scripts.summary import Summary, HotspotSummary


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


if __name__ == "__main__":
    path = "../results/smallbank/hotspot-64"
    res = read_hotspots(path)
    print(res)
