package com.tang.platform.consistency.jraft.meta.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NodeMeta {
    private NodeId id;
    private NodeEndpoint endpoint;

}
