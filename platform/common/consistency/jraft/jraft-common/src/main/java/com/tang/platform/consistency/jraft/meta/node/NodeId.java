package com.tang.platform.consistency.jraft.meta.node;


import lombok.Data;

import java.util.Objects;
public class NodeId {

    private final String name;

    public NodeId(String name) {
        this.name = name;
    }

    public static NodeId of(String name) {
        return new NodeId(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeId nodeId = (NodeId) o;
        return Objects.equals(name, nodeId.name);
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
