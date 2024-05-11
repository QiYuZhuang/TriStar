#!/usr/bin/python3
import os
from xml.etree import ElementTree
import xml.dom.minidom as minidom
from itertools import product

scaleFactor = 10
warmupTime = 20
execTime = 60  # ms
maxRetry = 16

transactionType = [
    "Amalgamate",
    "Balance",
    "DepositChecking",
    "SendPayment",
    "TransactSavings",
    "WriteCheck"
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


def generate_pg_sb_config(cc_type: str, terminals, weight, hsn=-1, hsp=-1.0, zipf=-0.1, rate="unlimited",
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

    if zipf > 0:
        ElementTree.SubElement(root, "zipf").text = str(zipf)
    if hsn > 0 and hsp > 0:
        ElementTree.SubElement(root, "hotspotNumber").text = str(hsn)
        ElementTree.SubElement(root, "hotspotPercentage").text = str(hsp)
    ElementTree.SubElement(root, "scalefactor").text = str(scaleFactor)
    ElementTree.SubElement(root, "terminals").text = str(terminals)

    works = ElementTree.SubElement(root, "works")
    generate_work(works, rate, weight)
    transactions = ElementTree.SubElement(root, "transactiontypes")
    generate_transation(transactions)

    # 将根目录转化为树行结构
    ElementTree.ElementTree(root)
    rough_str = ElementTree.tostring(root, 'utf-8')
    # 格式化
    reparsed = minidom.parseString(rough_str)
    new_str = reparsed.toprettyxml(indent='\t')

    filename = "/terminal_" + str(terminals)
    if zipf > 0:
        filename += "_zipf_{:03.2f}".format(zipf)
    elif hsn > 0 and hsp > 0:
        filename += "_hsn_{:05d}_hsp_{:03.2f}".format(hsn, hsp)

    filename += "_cc_" + cc_map[cc_type]

    f = open(dir + filename + ".xml", 'w', encoding='utf-8')
    f.write(new_str)
    f.close()


def generate_work(root: ElementTree, rate, weights):
    work = ElementTree.SubElement(root, "work")
    ElementTree.SubElement(work, "warmup").text = str(warmupTime)
    ElementTree.SubElement(work, "time").text = str(execTime)
    ElementTree.SubElement(work, "rate").text = rate
    ElementTree.SubElement(work, "retries").text = str(maxRetry)
    ElementTree.SubElement(work, "weights").text = str(weights)[1:-1]


def generate_transation(root: ElementTree):
    for entry in transactionType:
        transaction = ElementTree.SubElement(root, "transactiontype")
        ElementTree.SubElement(transaction, "name").text = entry


def default_weight_by_dis_ration(ratio: float):
    weights = [0] * len(transactionType)
    read_write_record_weight = int(ratio * 100)
    read_record_weight = (100 - read_write_record_weight) // 2
    update_record_weight = 100 - read_record_weight - read_write_record_weight
    weights[transactionType.index("ReadWriteRecord")] = read_write_record_weight
    weights[transactionType.index("ReadRecord")] = read_record_weight
    weights[transactionType.index("UpdateRecord")] = update_record_weight
    return list(weights)


def sb_scalability():
    dir_name = "../config/smallbank/scalability/postgresql"
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)
    terminals = [4, 8, 16, 32, 64, 128, 256]
    # "RC_TAILOR", "RC_TAILOR_LOCK", "DYNAMIC"
    cc = ["SERIALIZABLE", "SI_ELT", "RC_ELT", "SI_FOR_UPDATE", "RC_FOR_UPDATE", "SI_TAILOR"]
    # weight = list(default_weight_by_dis_ration(dis_ratio))
    weight = [20, 20, 20, 0, 20, 20]

    experiments = product(cc, terminals)
    for exp in experiments:
        generate_pg_sb_config(exp[0], exp[1], weight, dir=dir_name)


def sb_hotspot(terminal=128):
    dir_name = "../config/smallbank/hotspot-" + str(terminal) + "/postgresql"
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)
    cc = ["SERIALIZABLE", "SI_ELT", "RC_ELT", "SI_FOR_UPDATE", "RC_FOR_UPDATE", "SI_TAILOR"]
    hsn_list = [10, 100, 1000]
    hsp_list = [0.1, 0.3, 0.5, 0.7, 0.9]
    weight = [20, 20, 20, 0, 20, 20]

    experiments = product(cc, hsn_list, hsp_list)
    for exp in experiments:
        generate_pg_sb_config(exp[0], terminals=terminal, weight=weight, hsn=exp[1], hsp=exp[2], dir=dir_name)


def sb_zip_fain(terminal=128):
    dir_name = "../config/smallbank/skew-" + str(terminal) + "/postgresql"
    if not os.path.exists(dir_name):
        os.makedirs(dir_name)
    cc = ["SERIALIZABLE", "SI_ELT", "RC_ELT", "SI_FOR_UPDATE", "RC_FOR_UPDATE", "SI_TAILOR"]
    skew_list = [0.1, 0.3, 0.5, 0.7, 0.9, 1.1, 1.3, 1.5]
    weight = [20, 20, 20, 0, 20, 20]

    experiments = product(cc, skew_list)
    for exp in experiments:
        generate_pg_sb_config(exp[0], terminals=terminal, weight=weight, zipf=exp[1], dir=dir_name)


if __name__ == '__main__':
    if not os.path.exists("../config"):
        os.mkdir("../config")

    sb_scalability()
    sb_hotspot(128)
    sb_zip_fain(128)
