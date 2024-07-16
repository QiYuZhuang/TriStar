# Usage Guide
## How to Build
TriSail requires JDK 21 and Maven 3.9+ for compilation. To run the build scripts, you need to ensure that Python 3.9+ is installed.

You can run the following command to build:
```
Python3 build.py
```
This command will automatically compile and extract the relevant files.
## How to Run
After you have completed the compilation, you can run the benchmark tests using the run_test.py script. Our testing program does not include the data loading part,
so you should import the data into the database before running the tests.

You can run the following command to get help:
```
Python3 run_test.py -h
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
