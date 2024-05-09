package org.dbiir.tristar.transaction.isolation;

import lombok.Getter;
import lombok.Setter;
import org.dbiir.tristar.common.NodeType;
import org.dbiir.tristar.common.OperationType;
import org.dbiir.tristar.type.Operation;
import org.dbiir.tristar.type.Template;

@Getter
public class Node {
    private final Template template;
    private final Operation operation;   /* operation's index in template.operationList */
    private final int mappingType;
    private final NodeType type;
    @Setter
    private int offsetInCFG;

    public Node(Template template, Operation operation, int i, NodeType j) {
        this.template = template;
        this.operation = operation;
        this.mappingType = i;
        this.type = j;
    }

    /*
     * check if current operation potentially conflicts with $o_{1}^{'} \in prefix_{o_1}(\tau_1)$
     * @param idx: index of o1/p1
     */
    public boolean validInCFG(Template t1, Operation o1, Operation p1, int h, int i) {
        if (operation.getOperationType() == OperationType.ReadOperation)
            return true;

        // operation splitting, o1 if h = 1, o2 if h = 2
        Operation os;
        for (int k = 0; k < o1.getOffsetInTemplate(); k++) {
            // operation oi \in prefix()
            Operation oi = t1.getOperations().get(k);
            if (oi.getOperationType() == OperationType.ReadOperation)
                continue;
            if ((i == 1 && oi.variableCoincide(o1) && oi.variableCoincide(operation)) ||
                    (i == h && oi.variableCoincide(p1) && oi.variableCoincide(operation)))
                return false;
        }
        return true;
    }

    public boolean Equal(Node other) {
        return template.equals(other.template) &&
                operation.equals(other.operation) &&
                mappingType == other.mappingType &&
                type == other.type;
    }
}
