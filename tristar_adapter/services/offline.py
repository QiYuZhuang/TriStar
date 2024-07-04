import os
from typing import Any

import torch.nn
import torch.nn.functional as F
from torch import optim
from torch_geometric.data import Data
from torch_geometric.loader import DataLoader

from tristar_adapter.graph_construct.graph import Graph
from tristar_adapter.graph_training.train import GCN


class OfflineService:
    # filepath prefix and postfix
    __model_prefix = "models/"
    __model_postfix = ".pt"
    # params
    model: torch.nn.Module
    optimizer = None
    epoch_size = 100

    # static scenarios' graph
    __gs: list[list[Graph]] = []
    __g_labels: list[list[float]] = []
    __graph_batch: list[Data] = []

    def __init__(self, workload: str = None):
        if workload is not None:
            model_path = self.__model_prefix + workload + self.__model_postfix
            if os.path.exists(model_path) and os.path.isfile(model_path):
                self.model = torch.load(model_path)
                self.optimizer = optim.Adam(self.model.parameters(), lr=0.01)
            else:
                self.model = None

    def service(self, service_name: str, *args: Any, **kwargs: Any) -> Any:
        if service_name.lower() == "train":
            if len(args) == 1:
                self.traverse_folders(args[0])
            elif len(args) > 1:
                for ts in args[2]:
                    fpath = args[1] + f + ts
                    self.traverse_folders(fpath)
                    print(fpath)
            # return self.train()
            return "success"

    def __read_label_file(self, file_path: str) -> list[float]:
        labels = []
        with open(file_path, 'r') as file:
            for line in file:
                labels.extend([float(label) for label in line.strip().split(',')])
        return labels

    def traverse_folders(self, folder_path):
        for entry in os.scandir(folder_path):
            if not entry.is_dir():
                continue
            self.__gs.append([])
            for sub_entry in os.scandir(entry.path):
                if not sub_entry.is_file():
                    continue
                if 'label' in sub_entry.name:
                    self.__g_labels.append(self.__read_label_file(sub_entry.path))
                else:
                    self.__gs[-1].append(Graph(sub_entry.path))

            assert len(self.__gs) == len(self.__g_labels)

    def train(self):
        if self.model is None:
            self.model = GCN(num_node_features=1, hidden_channels=128, num_classes=3)
            self.optimizer = optim.Adam(self.model.parameters(), lr=0.01)

        for i in range(len(self.__gs)):
            for g in self.__gs[i]:
                self.__graph_batch.append(
                    Data(x=torch.tensor(g.nodes, dtype=torch.long),
                         edge_index=torch.tensor(g.edges, dtype=torch.long),
                         edge_attr=torch.tensor(g.edge_feature, dtype=torch.float),
                         y=torch.tensor(self.__g_labels[i], dtype=torch.int8))
                )
        loader = DataLoader(self.__graph_batch, batch_size=32, shuffle=True)

        for epoch in range(1, self.epoch_size + 1):
            self.train_epoch(loader)
            train_acc = self.test_epoch(loader)
            print(f'Epoch: {epoch:03d}, Train Acc: {train_acc:.4f}')

    def train_epoch(self, loader: DataLoader) -> None:
        self.model.train()
        for data in loader.dataset:
            self.optimizer.zero_grad()
            out = self.model(data.x, data.edge_index, data.batch)
            loss = F.cross_entropy(out, data.y)
            loss.backward()
            self.optimizer.step()

    def test_epoch(self, loader: DataLoader) -> float:
        self.model.eval()
        correct = 0
        for data in loader.dataset:
            out = self.model(data.x, data.edge_index, data.batch)
            pred = out.argmax(dim=1)
            correct += (pred == data.y).sum().item()
        return correct / len(loader.dataset)
