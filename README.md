# Usage Guide
TriSail is an extension based on [BenchBase](https://github.com/cmu-db/benchbase), currently supporting TPC-C, SmallBank, and YCSB benchmarks running on PostgreSQL.
## How to Build
TriSail requires JDK 21 and Maven 3.9+ for compilation. To run the build scripts, you need to ensure that Python 3.9+ is installed.


You can run the following command to build:
```
python3 build.py
```
This command will automatically compile and extract the relevant files.
## How to Run
We provide python scripts located in the /scripts folder to generate the corresponding `.xml` configuration files. Before running the tests, you should modify the information in the python script to ensure the generation of configuration files that meet the requirements, including the JDBC connection URL to connect to the database, and the database username and password.

For example, you can run the following command generate your tpcc configuration files:
```
python3 gen_tpcc_config.py
```
After you have completed the compilation and generated necessary configuration files, you can run the benchmark tests using the run_test.py script. Our testing program does not include the data loading part.
That means you should import the data into the database before running the tests.

You can run the following command to get help:
```
python3 run_test.py -h
```
The following options are provided:
```
  -h, --help            show this help message and exit
  -f {scalability,hotspot-128,hotspot-256,skew-128,skew_custom-128,skew_warehouse-128,skew-256,wc_ratio-256,bal_ratio-256,rate-256,bal_ratio-128,rate-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-1  -f {scalability,hotspot-128,hotspot-256,skew-128,skew_custom-128,skew_warehouse-128,skew-256,wc_ratio-256,bal_ratio-256,rate-256,bal_ratio-128,rate-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,ycsb-wr-128,ycsb-scalability,ycsb-skew} [{scalability,hotspot-128,hotspot-256,skew-128,skew_custom-128,skew_warehouse-128,skew-256,wc_ratio-256,bal_ratio-256,rate-256,bal_ratio-128,rate-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,ycsb-wr-128,ycsb-scalability,ycsb-skew} ...], --function {scalability,hotspot-128,hotspot-256,skew-128,skew_custom-128,skew_warehouse-128,skew-256,wc_ratio-256,bal_ratio-256,rate-256,bal_ratio-128,rate-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,ycsb-wr-128,ycsb-scalability,ycsb-skew} [{scalability,hotspot-128,hotspot-256,skew-128,skew_custom-128,skew_warehouse-128,skew-256,wc_ratio-256,bal_ratio-256,rate-256,bal_ratio-128,rate-128,wc_ratio-128,random-128,no_ratio-128,pa_ratio-128,ycsb-wr-128,ycsb-scalability,ycsb-skew} ...]
                        specify the function
  -w {ycsb,tpcc,smallbank}, --workload {ycsb,tpcc,smallbank}
                        specify the workload
  -e {mysql,postgresql}, --engine {mysql,postgresql}
                        specify the workload
  -n CNT, --cnt CNT     count of execution
```
For example, you can run the command to execute the hotspot-256 test of the SmallBank benchmark in PostgreSQL:
```
Python3 run_test.py -w smallbank -f hotspot-256 -e postgresql
```
