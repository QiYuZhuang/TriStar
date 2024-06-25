import os.path
from typing import Any

import torch.nn
from torch_geometric.data import Data

from graph_construct.graph import Graph


class OnlineService:
    _model: torch.nn.Module = None
    _model_prefix = "models/"
    _model_postfix = ".pt"

    def __init__(self, workload: str = None):
        if workload is not None:
            model_path = self._model_prefix + workload + self._model_postfix
            if os.path.exists(model_path) and os.path.isfile(model_path):
                self._model = torch.load(model_path)

    def service(self, service_name: str, *args: Any, **kwargs: Any) -> Any:
        if service_name.lower() == "predict":
            return self.predict(args[0])

    def predict(self, filepath: str) -> int:
        g = Graph(filepath)
        assert self._model is not None
        graph = Data(x=torch.tensor(g.nodes),
                     edge_index=torch.tensor(g.edges),
                     edge_attr=torch.tensor(g.edge_feature))
        return self._model(graph).argmax(dim=1)
