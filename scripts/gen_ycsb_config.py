#!/usr/bin/python3
import os
from xml.etree import ElementTree
import xml.dom.minidom as minidom
from itertools import product

scaleFactor = 4
warmupTime = 20
execTime = 60  # ms

transactionType = [
    "ReadRecord",
    "InsertRecord",
    "ScanRecord",
    "UpdateRecord",
    "DeleteRecord",
    "ReadModifyWriteRecord",
    "ReadWriteRecord"
]


cc_map = {
    "SERIALIZABLE": "SER",
    "SI_ELT": "SI+E",
    "RC_ELT": "RC+E",
    "SI_FOR_UPDATE": "SI+P",
    "RC_FOR_UPDATE": "RC+P",
    "SI_TAILOR": "SI+TV",
    "RC_TAILOR": "RC+TV",
    "RC_TAILOR_LOCK": "RC+TL",
    "DYNAMIC": "DYNAMIC"
}


def generate_mysql_ycsb_config(cc_type: str, zipf: float, wrtxn: float, wrtup: float, terminals, weight, rate="",
                               dir="../config"):
    # 创建根节点
    root = ElementTree.Element('parameters')
    # 添加子节点
    ElementTree.SubElement(root, 'type').text = "POSTGRES"
    ElementTree.SubElement(root, 'driver').text = "org.postgresql.Driver"
    ElementTree.SubElement(root, "url").text = ("jdbc:postgresql://localhost:5432/osprey?sslmode=disable&amp"
                                                ";ApplicationName=smallbank&amp;reWriteBatchedInserts=true")
    ElementTree.SubElement(root, "username").text = "postgres"
    ElementTree.SubElement(root, "password").text = "Ss123!@#"
    ElementTree.SubElement(root, "isolation").text = "TRANSACTION_SERIALIZABLE"
    ElementTree.SubElement(root, "batchsize").text = "128"
    ElementTree.SubElement(root, "concurrencyControlType").text = cc_type

    ElementTree.SubElement(root, "zipf").text = str(zipf)
    ElementTree.SubElement(root, "wrtup").text = str(wrtup)
    ElementTree.SubElement(root, "wrtxn").text = str(wrtxn)
    ElementTree.SubElement(root, "scalefactor").text = str(scaleFactor)
    ElementTree.SubElement(root, "terminals").text = str(terminals)

    works = ElementTree.SubElement(root, "works")
    if int(len(rate)) == int(0):
        generate_work(works, weight, "unlimited")
    else:
        generate_work(works, weight, rate)
    transactions = ElementTree.SubElement(root, "transactiontypes")
    generate_transation(transactions)

    # 将根目录转化为树行结构
    ElementTree.ElementTree(root)
    rough_str = ElementTree.tostring(root, 'utf-8')
    # 格式化
    reparsed = minidom.parseString(rough_str)
    new_str = reparsed.toprettyxml(indent='\t')

    filename = "/terminal_" + str(terminals)
    filename += "_zipf_{:03.2f}".format(zipf)
    filename += "_wrtxn_{:03.2f}".format(wrtxn)
    filename += "_wrtup_{:03.2f}".format(wrtup)
    if len(rate):
        filename += "_rate_" + str(rate)

    filename += "_cc_" + cc_map[cc_type]

    f = open(dir + filename + ".xml", 'w', encoding='utf-8')
    f.write(new_str)
    f.close()


def generate_work(root: ElementTree, weights, rate):
    work = ElementTree.SubElement(root, "work")
    ElementTree.SubElement(work, "warmup").text = str(warmupTime)
    ElementTree.SubElement(work, "time").text = str(execTime)
    ElementTree.SubElement(work, "rate").text = rate
    ElementTree.SubElement(work, "weights").text = str(weights)[1:-1]


def generate_transation(root: ElementTree):
    for entry in transactionType:
        transaction = ElementTree.SubElement(root, "transactiontype")
        ElementTree.SubElement(transaction, "name").text = entry


def ycsb_wr(terminal=128):
    dir_name = "../config/ycsb/wr_ratio-" + str(terminal) + "/postgresql"
    if not os.path.exists(dir_name):
        os.makedirs(dir_name, exist_ok=True)
    zipf = [0.1]
    wrtxn = [1]
    wrtup = [0, 0.2, 0.4, 0.6, 0.8, 1.0]
    cc = ["SERIALIZABLE", "SI_ELT", "RC_ELT", "SI_FOR_UPDATE", "RC_FOR_UPDATE", "RC_TAILOR", "SI_TAILOR", "RC_TAILOR_LOCK"]
    weight = [0, 0, 0, 0, 0, 0, 100]

    experiments = product(cc, zipf, wrtxn, wrtup, [terminal])
    for exp in experiments:
        generate_mysql_ycsb_config(exp[0], exp[1], exp[2], exp[3], exp[4], weight, dir=dir_name)


def ycsb_scalability():
    dir_name = "../config/ycsb/scalability/postgresql"
    if not os.path.exists(dir_name):
        os.makedirs(dir_name, exist_ok=True)
    terminals = [4, 8, 16, 32, 64, 128, 256, 512]
    zipf = [0.1, 0.3, 0.5]
    wrtxn = [1]
    wrtup = [0.2]
    cc = ["SERIALIZABLE", "SI_ELT", "RC_ELT", "SI_FOR_UPDATE", "RC_FOR_UPDATE", "RC_TAILOR", "SI_TAILOR", "RC_TAILOR_LOCK"]
    # weight = list(default_weight_by_dis_ration(dis_ratio))
    weight = [0, 0, 0, 0, 0, 0, 100]

    experiments = product(cc, zipf, wrtxn, wrtup, terminals)
    for exp in experiments:
        generate_mysql_ycsb_config(exp[0], exp[1], exp[2], exp[3], exp[4], weight, dir=dir_name)


def ycsb_skew(terminal=128):
    dir_name = "../config/ycsb/skew-" + str(terminal) + "/postgresql"
    if not os.path.exists(dir_name):
        os.makedirs(dir_name, exist_ok=True)
    zipf = [0.1, 0.3, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5]
    wrtxn = [0.5]
    wrtup = [0.2]
    cc = ["SERIALIZABLE", "SI_ELT", "RC_ELT", "SI_FOR_UPDATE", "RC_FOR_UPDATE", "RC_TAILOR", "SI_TAILOR"]
    weight = [0, 0, 0, 0, 0, 0, 100]

    experiments = product(cc, zipf, wrtxn, wrtup, [terminal])
    for exp in experiments:
        generate_mysql_ycsb_config(exp[0], exp[1], exp[2], exp[3], exp[4], weight, dir=dir_name)


if __name__ == '__main__':
    if not os.path.exists("../config"):
        os.mkdir("../config")

    scaleFactor = 100
    ycsb_scalability()
    ycsb_skew(128)
    ycsb_wr(128)
