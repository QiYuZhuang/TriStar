package org.dbiir.tristar.transaction.isolation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TemplateSQLMeta {
    private final String templateName;
    private final int op; // 0 for read, 1 for write
    private final String relationName;
    private final int indexInClientSide;
    private final String originSQL;
    private int indexInServerSide;
    private int skipIndex = -1;

    public TemplateSQLMeta(String templateName, int op, String relationName, int indexInClientSide, String originSQL) {
        this.templateName = templateName;
        this.op = op;
        this.relationName = relationName;
        this.indexInClientSide = indexInClientSide;
        this.originSQL = originSQL;
    }
}
