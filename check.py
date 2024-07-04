import re

def find_orphaned_ends(file_path):
    start_pattern = re.compile(r"Transaction start #(\d+)")
    end_pattern = re.compile(r"Transaction end #(\d+)")

    start_ids = set()
    end_ids = set()

    with open(file_path, 'r') as file:
        for line in file:
            start_match = start_pattern.search(line)
            if start_match:
                start_ids.add(start_match.group(1))
            
            end_match = end_pattern.search(line)
            if end_match:
                end_ids.add(end_match.group(1))
    
    print(len(start_ids), len(end_ids))
    orphaned_ends = start_ids - end_ids

    return sorted(orphaned_ends)

file_path = 'results/smallbank/skew-128/2024-06-16-21-17-04/terminal_128_zipf_1.30_cc_RC+TV/stdout.log'  # 替换为实际文件路径
orphaned_ends = find_orphaned_ends(file_path)
if orphaned_ends:
    print("Orphaned end transaction IDs:")
    for txn_id in orphaned_ends:
        print(txn_id)
else:
    print("No orphaned end transaction IDs found.")
    
