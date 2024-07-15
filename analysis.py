import re
from collections import defaultdict

# 定义正则表达式来匹配 acquire 和 release 操作
acquire_pattern = re.compile(r'YCSBWorker<(\d+)> acquire id: (\d+) SH, count: (\d+)')
release_pattern = re.compile(r'YCSBWorker<(\d+)>release id: (\d+) SH, SH count: (\d+)')

# 用于存储每个 Worker 的锁状态
lock_status = defaultdict(lambda: defaultdict(int))

# 读取日志文件
log_file = 'results/ycsb/dynamic-128/2024-07-05-16-26-53/terminal_128_zipf_0.10_wrtxn_0.67_wrtup_0.10_cc_DYNAMIC/stdout.log'

with open(log_file, 'r') as file:
    for line in file:
        acquire_match = acquire_pattern.match(line)
        release_match = release_pattern.match(line)
        
        if acquire_match:
            worker_id, lock_id, count = acquire_match.groups()
            lock_status[worker_id][lock_id] += 1
        elif release_match:
            worker_id, lock_id, count = release_match.groups()
            lock_status[worker_id][lock_id] -= 1

# 找出 acquire 了锁但没有 release 的 Worker
for worker_id, locks in lock_status.items():
    for lock_id, count in locks.items():
        if count > 0:
            print(f"Worker {worker_id} acquire lock {lock_id} but did not release it.")
        if count >= 10:
            print(f"Worker {worker_id} acquire lock {lock_id} but did not release it. error")
