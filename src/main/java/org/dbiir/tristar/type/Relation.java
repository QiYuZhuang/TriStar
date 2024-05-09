package org.dbiir.tristar.type;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Relation {
    private String name;
    private List<Attribute> attributes;
    private List<Attribute> primaries;

    public Relation(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.primaries = new ArrayList<>();
    }

    public void addAttributes(List<Attribute> attributes) {
        this.attributes.addAll(attributes);
    }

    public void addPrimaries(List<Attribute> primaries) {
        this.primaries.addAll(primaries);
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public void addPrimary(Attribute attribute) {
        this.primaries.add(attribute);
    }
}
