import torch
import torch.nn as nn
import torch.optim as optim

# 定义第一个MLP模型
class MLP1(nn.Module):
    def __init__(self, input_dim, hidden_dim):
        super(MLP1, self).__init__()
        self.fc1 = nn.Linear(input_dim, hidden_dim)

    def forward(self, x):
        x = torch.sigmoid(self.fc1(x))
        return x

# 定义第二个MLP模型
class MLP2(nn.Module):
    def __init__(self, input_dim, hidden_dim, output_dim):
        super(MLP2, self).__init__()
        self.fc1 = nn.Linear(input_dim, hidden_dim)
        self.fc2 = nn.Linear(hidden_dim, output_dim)

    def forward(self, x):
        x = torch.sigmoid(self.fc1(x))
        x = self.fc2(x)
        return x

# 定义训练数据
n = 10  # 数据项数量
input_dim = 2  # 输入维度
hidden_dim = 1000  # 隐藏层维度
output_dim = 3  # 输出维度
labels = torch.tensor([[1, 0, 0], [0, 1, 0], [0, 0, 1], [1, 0, 0], [0, 1, 0], [0, 0, 1], [1, 0, 0], [0, 1, 0], [0, 0, 1], [1, 0, 0]])

# 创建第一个MLP模型实例
mlp1 = MLP1(input_dim, hidden_dim)

# 创建第二个MLP模型实例
mlp2 = MLP2(hidden_dim, hidden_dim, output_dim)

# 定义损失函数和优化器
criterion = nn.MSELoss()
optimizer = optim.SGD(list(mlp1.parameters()) + list(mlp2.parameters()), lr=0.01)

# 训练模型
for epoch in range(100):
    optimizer.zero_grad()
    inputs = torch.randn(n, input_dim)  # 随机生成输入数据
    outputs1 = mlp1(inputs)  # 第一个MLP前向传播
    summed_output = torch.sum(outputs1, dim=0)  # 求和
    outputs2 = mlp2(summed_output.unsqueeze(0))  # 第二个MLP前向传播
    loss = criterion(outputs2, labels[0])  # 计算损失
    loss.backward()  # 反向传播
    optimizer.step()  # 更新模型参数

    if (epoch+1) % 10 == 0:
        print(f"Epoch: {epoch+1}, Loss: {loss.item()}")

# 测试模型
test_inputs = torch.randn(n, input_dim)
test_outputs1 = mlp1(test_inputs)
test_summed_output = torch.sum(test_outputs1, dim=0)
test_outputs2 = mlp2(test_summed_output.unsqueeze(0))
print("Test Output:", test_outputs2)