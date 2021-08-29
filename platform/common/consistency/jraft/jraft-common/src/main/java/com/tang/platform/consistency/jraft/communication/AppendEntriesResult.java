package com.tang.platform.consistency.jraft.communication;

import com.tang.platform.consistency.jraft.meta.node.NodeId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppendEntriesResult {
    private final int term;
    private final boolean success;
    private final NodeId nodeId;
    private final int lastLogIndex;
}
