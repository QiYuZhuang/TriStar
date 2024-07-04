package org.dbiir.tristar.transaction.isolation;

import org.dbiir.tristar.common.NodeType;
import org.dbiir.tristar.type.Operation;
import org.dbiir.tristar.type.Template;

import java.util.HashSet;
import java.util.Set;

public class Inspector {
    private final Set<Template> templateSet;

    Inspector() {
        templateSet = new HashSet<>();
    }

    public Inspector(InspectorBuilder builder) {
        templateSet = builder.templateSet;
    }

    public boolean RCRobust() {
        for (Template t1: templateSet) {
            for (Operation o1: t1.getOperations()) {
                // p1 \in t1 and i \in {1, 2}
                for (Operation p1: t1.getOperations()) {
                    if (o1.equals(p1)) continue;
                    for (int h = 1; h <= 2; h++) {
                        ConflictFreeGraph G = prefixConflictFreeGraph(o1, p1, h, t1, templateSet);
                        transitiveClosure(G);
                        if (!processConnected(G, t1, o1, p1, h))
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean SIRobust() {
        return true;
    }

    private boolean processConnected(ConflictFreeGraph g, Template t1, Operation o1, Operation p1, int h) {
        for (Template t2: templateSet) {
            if (t2.equals(t1)) continue;
            for (Template tm: templateSet) {
                if (tm.equals(t1) || tm.equals(t2)) continue;
                for (Operation p2: t2.getOperations()) {
                    for (Operation om: tm.getOperations()) {
                        if (p1.variableCoincide(om) &&
                                o1.potentialRWConflict(p2) &&
                                (o1.getOffsetInTemplate() < p1.getOffsetInTemplate() ||
                                        om.potentialRWConflict(p2))) {
                            Node node1 = new Node(t2, p2, 1, NodeType.IN);
                            Node node2 = new Node(tm, om, h, NodeType.OUT);
                            if (g.Connected(node1, node2))
                                return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private ConflictFreeGraph prefixConflictFreeGraph(Operation o1, Operation p1, int h, Template t1, Set<Template> templateSet) {
        ConflictFreeGraph.CFGBuilder builder = ConflictFreeGraph.newBuilder();
        // add node
        for (Template t: templateSet) {
            if (t.equals(t1)) continue;
            for (Operation o: t.getOperations()) {
                for (int i = 1; i <= 3; i++) {
                    Node nodeIn = new Node(t, o, i, NodeType.IN);
                    if (nodeIn.validInCFG(t1, o1, p1, h, i)) {
                        builder.addNode(nodeIn);
                    }
                    Node nodeOut = new Node(t, o, i, NodeType.OUT);
                    if (nodeOut.validInCFG(t1, o1, p1, h, i)) {
                        builder.addNode(nodeOut);
                    }
                }
            }
        }
        ConflictFreeGraph g = builder.build();

        // add edge
        g.generateEdges();
        return g;
    }

    private void transitiveClosure(ConflictFreeGraph graph) {
        graph.transitiveClosure();
    }

    public InspectorBuilder newBuilder() {
        return new InspectorBuilder();
    }

    public static class InspectorBuilder {
        private final Set<Template> templateSet = new HashSet<>();

        public InspectorBuilder addTemplate(Template template) {
            this.templateSet.add(template);
            return this;
        }

        public Inspector build() {
            return new Inspector(this);
        }
    }
}
