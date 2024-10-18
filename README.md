# Usage Guide
TxnSails, based on [BenchBase](https://github.com/cmu-db/benchbase), currently supports TPC-C, SmallBank, and YCSB benchmarks running on PostgreSQL. Additionally, you can run the TxnSails, Promotion, and ELM methods on the these benchmarks.
## How to Build
TxnSails requires JDK 21 and Maven 3.9+ for compilation. To run the build scripts, you need to ensure that Python 3.9+ is installed.


You can run the following command to build:
```bash
python3 build.py
```
This command will automatically compile and extract the relevant files.
## How to Run
We provide python scripts located in the `/scripts` folder to generate the corresponding `.xml` configuration files. Before running the tests, you should modify the information in the python script to ensure the generation of configuration files that meet the requirements, including the JDBC connection URL to connect to the database, and the database username and password.

For example, you can run the following command generate your tpcc configuration files:
```bash
python3 gen_tpcc_config.py
```
After you have completed the compilation and generated necessary configuration files, you can run the benchmark tests using the `run_test.py` script. TxnSails does not include the data loading part.
That means you should load data into the database by yourselves before running the tests. The schemas for all benchmarks (SmallBank, TPC-C, YCSB) are located in `/config`.

You can run the following command to get help:
```bash
python3 run_test.py -h
```
The following options are provided:
```bash
  -h, --help            show this help message and exit
  -f {scalability,hotspot-128,skew-128,wc_ratio-256,bal_ratio-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,wr_ratio-128} [{scalability,hotspot-128,skew-128,wc_ratio-256,bal_ratio-128,wc_ratio-
128,random-128,no_ratio-128,pa_ratio-128,wr_ratio-128} ...], --function {scalability,hotspot-128,skew-128,wc_ratio-256,bal_ratio-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,wr_ratio-128} [{scalability,hotspot-128,skew-128,wc_ratio-256,bal_ratio-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,wr_ratio-128} ...]
                        specify the function
  -w {ycsb,tpcc,smallbank}, --workload {ycsb,tpcc,smallbank}
                        specify the workload
  -e {postgresql}, --engine {postgresql}
                        specify the workload
  -n CNT, --cnt CNT     count of execution
  -p PHASE, --phase PHASE
                        online predict or offline training
```
For example, you can run the command to execute the hotspot-256 test of the SmallBank benchmark in PostgreSQL:
```bash
python3 run_test.py -w smallbank -f hotspot-256 -e postgresql
```
