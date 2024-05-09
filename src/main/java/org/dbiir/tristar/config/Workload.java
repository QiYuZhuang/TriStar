package org.dbiir.tristar.config;

import lombok.Getter;
import org.dbiir.tristar.type.Template;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Workload {
    private String name;
    private String databaseName;
    private List<Template> templates;

    private Workload(WorkloadBuilder builder) {
        this.name = builder.name;
        this.databaseName = builder.databaseName;
        this.templates = builder.templates;
    }

    public static WorkloadBuilder newBuilder() {
        return new WorkloadBuilder();
    }



    public static class WorkloadBuilder {
        private String name;
        private String databaseName;
        private List<Template> templates;

        public WorkloadBuilder() {
            templates = new ArrayList<>();
        }

        public WorkloadBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public WorkloadBuilder setDataBaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public WorkloadBuilder addTemplate(Template t) {
            this.templates.add(t);
            return this;
        }

        public WorkloadBuilder addTemplates(List<Template> templates) {
            this.templates.addAll(templates);
            return this;
        }

        public Workload build() {
            return new Workload(this);
        }
        }
}
