package org.dbiir.tristar.type;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Template {
    String name;
    List<Operation> operations;
    Map<Integer, List<Operation>> groups;

    public Template() {
        this.operations = new ArrayList<>();
        this.groups = new HashMap<>();
    }

    private Template(TemplateBuilder builder) {
        this.operations = builder.operations;
        this.groups = builder.groups;
        this.name = builder.name;
    }

    public static TemplateBuilder newBuilder() {
        return new TemplateBuilder();
    }

    public static class TemplateBuilder {
        String name;
        List<Operation> operations;
        Map<Integer, List<Operation>> groups;

        public TemplateBuilder() {
            this.operations = new ArrayList<>();
            this.groups = new HashMap<>();
        }

        public TemplateBuilder addOperation(Operation op) {
            operations.add(op);
            return this;
        }

        public TemplateBuilder addOperation(Operation op, int groupId) {
            operations.add(op);
            if (!groups.containsKey(groupId))
                groups.put(groupId, new ArrayList<>());
            groups.get(groupId).add(op);
            return this;
        }

        public TemplateBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Template build() {
            return new Template(this);
        }
    }
}
