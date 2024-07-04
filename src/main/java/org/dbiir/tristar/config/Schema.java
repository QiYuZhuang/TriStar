package org.dbiir.tristar.config;

import lombok.Getter;
import lombok.Setter;
import org.dbiir.tristar.type.Relation;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Schema {
    private String name;
    private List<Relation> relations;

    public Schema(SchemaBuilder builder) {
        this.name = builder.name;
        this.relations = builder.relations;
    }

    public static SchemaBuilder newBuilder() {
        return new SchemaBuilder();
    }

    public static class SchemaBuilder {
        @Setter
        private String name;
        private List<Relation> relations;

        public SchemaBuilder() {
            relations = new ArrayList<>();
        }

        public void addRelation(Relation rel) {
            relations.add(rel);
        }

        public void addRelations(List<Relation> rels) {
            this.relations.addAll(rels);
        }
    }
}
