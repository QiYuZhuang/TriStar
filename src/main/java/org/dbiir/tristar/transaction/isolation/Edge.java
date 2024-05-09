package org.dbiir.tristar.transaction.isolation;

import lombok.Getter;
import org.dbiir.tristar.common.EdgeType;

@Getter
public class Edge {
    public Node src;
    public Node dst;
    public EdgeType nodeType;

    public Edge(Node src, Node dst, EdgeType type) {
        this.src = src;
        this.dst = dst;
        this.nodeType = type;
    }
}
