import torch
import torch.nn.functional as F
from torch_geometric.nn import GCNConv, global_mean_pool
from torch_geometric.data import Data, DataLoader


class GCN(torch.nn.Module):
    def __init__(self, num_node_features, hidden_channels, num_classes):
        super(GCN, self).__init__()
        self.conv1 = GCNConv(num_node_features, hidden_channels)
        self.conv2 = GCNConv(hidden_channels, hidden_channels)
        self.conv3 = GCNConv(hidden_channels, hidden_channels)
        self.lin = torch.nn.Linear(hidden_channels, num_classes)

    def forward(self, x, edge_index, batch):
        x = self.conv1(x, edge_index)
        x = F.relu(x)
        x = self.conv2(x, edge_index)
        x = F.relu(x)
        x = self.conv3(x, edge_index)
        x = global_mean_pool(x, batch)  # Global pooling
        x = self.lin(x)
        return x


# Example: Create a list of graphs
graph1 = Data(x=torch.tensor([[1, 2], [3, 4]], dtype=torch.float),
              edge_index=torch.tensor([[0, 1], [1, 0]], dtype=torch.long),
              y=torch.tensor([0]))  # Label for graph1

graph2 = Data(x=torch.tensor([[5, 6], [7, 8], [9, 10]], dtype=torch.float),
              edge_index=torch.tensor([[0, 1], [1, 2], [2, 0]], dtype=torch.long),
              y=torch.tensor([1]))  # Label for graph2

# Combine graphs into a list
dataset = [graph1, graph2]

# Create DataLoader to handle batches
loader = DataLoader(dataset, batch_size=2, shuffle=True)
