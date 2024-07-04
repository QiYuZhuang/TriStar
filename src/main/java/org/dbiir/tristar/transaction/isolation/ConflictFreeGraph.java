package org.dbiir.tristar.transaction.isolation;

import org.dbiir.tristar.common.NodeType;
import org.dbiir.tristar.common.EdgeType;

import java.util.ArrayList;
import java.util.List;


public class ConflictFreeGraph {
    /*
     *
     */
    List<Node> nodes;
    List<Edge> edges;

    int[][] nodeMap;

    public ConflictFreeGraph(CFGBuilder cfgBuilder) {
        this.nodes = cfgBuilder.nodes;
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setOffsetInCFG(i);
        }
    }

    public static CFGBuilder newBuilder() {
        return new CFGBuilder();
    }

    public void generateEdges() {
        nodeMap = new int[nodes.size()][nodes.size()];
        for (Node node1: nodes) {
            for (Node node2: nodes) {
                if (node1.equals(node2)) continue;
                if (node1.getTemplate().equals(node2.getTemplate())) {
                    // inner edges
                    if (node1.getType() == NodeType.IN && node2.getType() == NodeType.OUT) {
                        Edge edge = new Edge(node1, node2, EdgeType.INNER);
                        edges.add(edge);
                        nodeMap[node1.getOffsetInCFG()][node2.getOffsetInCFG()] = 1;
                    }
                } else {
                    // outer edges
                    if (node1.getType() == NodeType.OUT && node2.getType() == NodeType.IN &&
                            node1.getMappingType() == node2.getMappingType() &&
                            node1.getOperation().variableCoincide(node2.getOperation())) {
                        Edge edge = new Edge(node1, node2, EdgeType.OUTER);
                        edges.add(edge);
                        nodeMap[node1.getOffsetInCFG()][node2.getOffsetInCFG()] = 1;
                    }
                }
            }
        }
    }

    public void transitiveClosure() {
        for (int i = 0; i < nodes.size(); i++)
            nodeMap[i][i] = 1;

        for (int k = 0; k < nodes.size(); k++) {
            for (int i = 0; i < nodes.size(); i++) {
                for (int j = 0; j < nodes.size(); j++) {
                    if (nodeMap[i][k] == 1 && nodeMap[k][j] == 1) {
                        nodeMap[i][j] = 1;
                    }
                }
            }
        }
    }

    public boolean Connected(Node n1, Node n2) {
        int idx1 = -1, idx2 = -1;
        for (Node n: nodes) {
            if (idx1 == -1 && n.Equal(n1)) idx1 = n.getOffsetInCFG();
            else if (idx2 == -1 && n.Equal(n2)) idx2 = n.getOffsetInCFG();
            else if (idx1 != -1 && idx2 != -1) break;
        }
        return nodeMap[idx1][idx2] > 0;
    }

    public static class CFGBuilder {
        final List<Node> nodes;

        public CFGBuilder() {
            nodes = new ArrayList<>();
        }

        public CFGBuilder addNode(Node node) {
            this.nodes.add(node);
            return this;
        }

        public ConflictFreeGraph build() {
            return new ConflictFreeGraph(this);
        }
    }
}
