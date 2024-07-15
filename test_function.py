# %%
from tristar_adapter.services.offline import OfflineService

offline_service = OfflineService("ycsb")
offline_service.service("train", "random-128", "/data/workspace/tristar/metas/ycsb", ["2024-07-10-00-23-30"])
# offline_service.service("train", "random-128", "/data/workspace/tristar/metas/ycsb", ["xxx"])

#/data/workspace/tristar/metas/smallbank/random-128 metas/ycsb/random-128/2024-06-26-17-41-36
# metas/ycsb/random-128/2024-07-10-00-23-30
