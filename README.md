# Usage Guide
TxnSails, based on [BenchBase](https://github.com/cmu-db/benchbase), currently supports [TPC-C, SmallBank, and YCSB benchmarks](BENCHMARK_SQL_Statement.pdf) running on PostgreSQL. Additionally, you can run the TxnSails, Promotion, and ELM methods on the these benchmarks.

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

