#!/usr/bin/python3
import os
from datetime import datetime
import time
import argparse
import sys


prefix_cmd = "java -cp target/tristar/tristar/lib/ -jar target/tristar/tristar/tristar.jar "
              # "-b tpcc -c config/postgres/sample_tpcc_config.xml --execute=true"
result_prefix = "results/"
workloads = ["ycsb", "tpcc", "smallbank"]
engines = ["mysql", "postgresql"]
functions = ["scalability", "hotspot-128", "hotspot-256", "skew-128", "skew-256"]


def exec_cmd(cmd: str):
    print("command: " + cmd)
    # exit_status = os.system(cmd)
    #
    # if exit_status == 0:
    #     print("Command executed successfully")
    # else:
    #     print(f"Command failed with exit status {exit_status}")


def traverse_dir(dir_name: str) -> list:
    xml_files = []

    for root, dirs, files in os.walk(dir_name):
        for file in files:
            if file.startswith('.'):
                continue
            if file.endswith('.xml'):
                xml_files.append(os.path.join(root, file))

    return xml_files


def create_output_file(filepath: str):
    os.makedirs(filepath, exist_ok=True)

    file_name = "stdout.log"
    file_path = os.path.join(filepath, file_name)
    open(file_path, 'w').close()

    sys.stdout = open(file_path, 'w')

    print("create new file: " + file_path)


def refresh_output_channel():
    sys.stdout.close()
    sys.stdout = sys.__stdout__


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("-f", "--function", dest='func', nargs='+', choices=functions, type=str,
                        help="specify the function")
    parser.add_argument("-w", "--workload", dest='wl', choices=workloads, type=str, required=True,
                        help="specify the workload")
    parser.add_argument("-e", "--engine", dest="engine", choices=engines, type=str, required=True,
                        help="specify the workload")
    parser.add_argument("-n", "--cnt", dest="cnt", type=int, required=False, default=1,
                        help="count of execution")

    return parser.parse_args()


def run_once(f: str):
    # traverse the dir
    config_path = "config/" + args.wl + "/" + f + "/" + args.engine + "/"
    print("config_path: " + config_path)
    unique_ts = datetime.now().strftime('%Y-%m-%d-%H-%M-%S')
    for conf_file in traverse_dir(config_path):
        result_dir = result_prefix + f + "/" + unique_ts + "/"
        case_name = os.path.splitext(os.path.basename(conf_file))[0]
        create_output_file(result_dir + case_name)
        print("Run config - { " + case_name + " }")
        java_cmd = (prefix_cmd + "-b " + args.wl + " -c " + config_path + case_name +
                    " --execute=true -d " + result_dir + case_name)
        exec_cmd(java_cmd)
        print("Finish config - { " + case_name + " }")
        # time.sleep(5)
        refresh_output_channel()


def run_cnt(f: str, cnt: int):
    for i in range(cnt):
        run_once(f)


if __name__ == "__main__":
    args = parse_args()
    start_time = datetime.now()
    print("workload: " + args.wl + " engine: " + args.engine + " cnt: " + str(args.cnt))
    ff = functions
    if args.func is not None:
        ff = args.func

    for f in ff:
        run_cnt(f, args.cnt)

    print("start time: ", start_time)
    print("end time: ", datetime.now())
