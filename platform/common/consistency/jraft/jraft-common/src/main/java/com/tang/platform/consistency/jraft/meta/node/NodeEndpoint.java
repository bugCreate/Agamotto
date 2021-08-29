package com.tang.platform.consistency.jraft.meta.node;

import com.google.common.base.Preconditions;
import lombok.Data;

@Data
public class NodeEndpoint {
    private final NodeId id;
    private final Address address;

    public NodeEndpoint(String id, String host, int port) {
        this(new NodeId(id), new Address(host, port));
    }

    public NodeEndpoint(NodeId id, Address address) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(address);
        this.id = id;
        this.address = address;
    }
}
