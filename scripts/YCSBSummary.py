#!/usr/bin/python3
from summary import Summary

transactionType = [

]

def get_transaction_idx(txn_name: str):
    if txn_name in transactionType:
        return transactionType.index(txn_name)
    else:
        return -1


class YCSBBaseSummary(Summary):
    terminals: int
    skew: float
    wr_tup: float
    wr_txn: float

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        s_idx, t_idx, c_idx = tokens.index("zipf"), tokens.index("terminal"), tokens.index("cc")
        self.skew = float(tokens[s_idx + 1])
        self.terminals = int(tokens[t_idx + 1])
        self.wr_tup = float(tokens[tokens.index("wrtup") + 1])
        self.wr_txn = float(tokens[tokens.index("wrtxn") + 1])
        super().__init__(filepath, '_'.join(tokens[c_idx + 1:]))

    def __lt__(self, other):
        if self.cc_type < other.cc_type:
            return True
        elif self.cc_type == other.cc_type:
            if self.terminals < other.terminals:
                return True
            elif self.terminals == other.terminals:
                if self.skew < other.skew:
                    return True
                elif abs(self.skew - other.skew) < 1e-5:
                    if self.wr_tup < other.wr_tup:
                        return True
                    elif abs(self.wr_tup - other.wr_tup) < 1e-5:
                        return self.wr_txn < other.wr_txn
        return False


class YCSBRateSummary(YCSBBaseSummary):
    rate: int

    def __init__(self, filepath: str):
        filename = filepath.split("/")[-2]
        tokens = filename.split("_")
        s_idx, t_idx, r_idx, c_idx = tokens.index("zipf"), tokens.index("terminal"), tokens.index("rate"), tokens.index("cc")
        self.skew = float(tokens[s_idx + 1])
        self.terminals = int(tokens[t_idx + 1])
        self.wr_tup = float(tokens[tokens.index("wrtup") + 1])
        self.wr_txn = float(tokens[tokens.index("wrtxn") + 1])
        self.rate = 100000 if tokens[r_idx + 1] == "unlimited" else int(tokens[r_idx + 1])
        super().__init__(filepath, '_'.join(tokens[c_idx + 1:]))

    def __lt__(self, other):
        if self.rate < other.rate:
            return True
        elif self.rate == other.rate:
            return super().__lt__(other)
