import torch
import torch.nn.functional as F
from torch.nn import ReLU, Linear, Sequential, Dropout
from torch_geometric.nn import NNConv, global_mean_pool
from torch_geometric.data import DataLoader, Data
from torch_geometric.datasets import TUDataset

class GraphClassificationModel(torch.nn.Module):
    def __init__(self, in_channels, edge_in_channels, hidden_channels, out_channels):
        super(GraphClassificationModel, self).__init__()

        # Define the edge network for the first NNConv layer
        self.edge_network1 = Sequential(Linear(edge_in_channels, hidden_channels),
                                 ReLU(),
                                 Linear(hidden_channels, in_channels * hidden_channels))

        # Define the first NNConv layer
        self.conv1 = NNConv(in_channels, hidden_channels, self.edge_network1, aggr='max')

        # Define the edge network for the second NNConv layer
        self.edge_network2 = Sequential(Linear(edge_in_channels, hidden_channels),
                                 ReLU(),
                                 Linear(hidden_channels, hidden_channels * hidden_channels))

        # Define the second NNConv layer
        self.conv2 = NNConv(hidden_channels, hidden_channels, self.edge_network2, aggr='max')

        # Define the edge network for the third NNConv layer
        self.edge_network3 = Sequential(Linear(edge_in_channels, hidden_channels),
                                 ReLU(),
                                 Linear(hidden_channels, hidden_channels * hidden_channels))

        # Define the third NNConv layer
        self.conv3 = NNConv(hidden_channels, hidden_channels, self.edge_network3, aggr='max')

        # Define a fully connected layer for classification
        self.fc1 = Linear(hidden_channels, hidden_channels)
        self.fc2 = Linear(hidden_channels, out_channels)
        
        self.dropout = Dropout(0.5)

    def forward(self, data):
        x, edge_index, edge_attr, batch = data.x, data.edge_index, data.edge_attr, data.batch

        # Apply NNConv layer
        x = self.conv1(x, edge_index, edge_attr)
        x = F.relu(x)
        # x = self.dropout(x)  # Apply Dropout after first conv layer
        x = self.conv2(x, edge_index, edge_attr)
        x = F.relu(x)
        # x = self.dropout(x)  
        x = self.conv3(x, edge_index, edge_attr)
        x = F.relu(x)
        # x = self.dropout(x)

        # Global mean pooling
        x = global_mean_pool(x, batch)

        # Apply fully connected layers
        x = F.relu(self.fc1(x))
        x = self.fc2(x)

        return F.log_softmax(x, dim=1)


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
