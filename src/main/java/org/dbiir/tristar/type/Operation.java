package org.dbiir.tristar.type;

import lombok.Getter;
import org.dbiir.tristar.common.OperationType;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Operation {
    private final OperationType operationType;
    private final String databaseName;
    private final String relationName;
    private final List<String> attrNames;
    private final List<String> updateAttrNames;
    private final int variableId;
    private int offsetInTemplate;

    public Operation() {
        this.operationType = OperationType.NoneType;
        this.databaseName = "unknown";
        this.relationName = "unknown";
        this.variableId = -1;
        this.attrNames = new ArrayList<>();
        this.updateAttrNames = new ArrayList<>();
    }

    public Operation(OperationType opType, String databaseName, String relationName, List<String> updateAttrNames) {
        this.operationType = opType;
        this.databaseName = databaseName;
        this.relationName = relationName;
        this.updateAttrNames = updateAttrNames;
        this.variableId = -1;
        this.attrNames = new ArrayList<>();
    }

    public Operation(OperationType opType, String databaseName, String relationName, List<String> attrNames, List<String> updateAttrNames) {
        this.operationType = opType;
        this.databaseName = databaseName;
        this.relationName = relationName;
        this.updateAttrNames = updateAttrNames;
        this.variableId = -1;
        this.attrNames = attrNames;
    }

    public Operation(OperationType opType, String databaseName, String relationName, List<String> attrNames, List<String> updateAttrNames, int variableId) {
        this.operationType = opType;
        this.databaseName = databaseName;
        this.relationName = relationName;
        this.updateAttrNames = updateAttrNames;
        this.variableId = variableId;
        this.attrNames = attrNames;
    }

    private Operation(OperationBuilder builder) {
        this.operationType = builder.operationType;
        this.databaseName = builder.databaseName;
        this.relationName = builder.relationName;
        this.variableId = builder.variableId;
        this.attrNames = builder.attrNames;
        this.offsetInTemplate = builder.offsetInTemplate;
        this.updateAttrNames = builder.updateAttrNames;
    }

    public static OperationBuilder newBuilder() {
        return new OperationBuilder();
    }

    public boolean variableCoincide(Operation other) {
        if (this.variableId != -1 && other.getVariableId() != -1) {
            return this.variableId == other.variableId;
        } else {
            if (this.databaseName.equals(other.databaseName)) {
                if (this.relationName.equals(other.relationName)) {
                    // Attr Level Concurrency Control
                    for (String attrName : attrNames) {
                        for (String otherAttrName : other.attrNames) {
                            if (attrName.equals(otherAttrName))
                                return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /*
     * this is a read operation, other is a write operation
     * this & other operate on the same variable
     */
    public boolean potentialRWConflict(Operation other) {
        return variableCoincide(other) &&
                operationType == OperationType.ReadOperation &&
                other.operationType == OperationType.WriteOperation;
    }

    public static class OperationBuilder {
        private OperationType operationType;
        private String databaseName;
        private String relationName;
        private final List<String> attrNames;
        private int variableId;
        private int offsetInTemplate;
        private final List<String> updateAttrNames; /* used for update operation */

        public OperationBuilder() {
            attrNames = new ArrayList<>();
            updateAttrNames = new ArrayList<>();
        }

        public OperationBuilder setOperationType(OperationType type) {
            this.operationType = type;
            return this;
        }

        public OperationBuilder setOperationType(String type) {
            switch (type) {
                case "READ": {
                    this.operationType = OperationType.ReadOperation;
                    break;
                }
                case "WRITE": {
                    this.operationType = OperationType.WriteOperation;
                    break;
                }
                case "UPDATE": {
                    this.operationType = OperationType.UpdateOperation;
                    break;
                }
                default:
                    this.operationType = OperationType.NoneType;
            }
            return this;
        }

        public OperationBuilder setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public OperationBuilder setRelationName(String relationName) {
            this.relationName = relationName;
            return this;
        }

        public OperationBuilder addAttribute(String attrName) {
            this.attrNames.add(attrName);
            return this;
        }

        public OperationBuilder addAttributes(List<String> attrNames) {
            this.attrNames.addAll(attrNames);
            return this;
        }

        public OperationBuilder addUpdateAttribute(String updateAttrName) {
            this.updateAttrNames.add(updateAttrName);
            return this;
        }

        public OperationBuilder addUpdateAttributes(List<String> updateAttrNames) {
            this.updateAttrNames.addAll(updateAttrNames);
            return this;
        }

        public OperationBuilder setOffsetInTemplate(int offsetInTemplate) {
            this.offsetInTemplate = offsetInTemplate;
            return this;
        }

        public Operation build() {
            return new Operation(this);
        }
    }
}
