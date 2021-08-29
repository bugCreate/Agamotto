package com.tang.platform.consistency.jraft.communication;

import com.tang.platform.consistency.jraft.meta.node.NodeId;
import lombok.Data;

@Data
public class RequestVote {
    private int term;
    private NodeId nodeId;
    private int lastLogIndex = 0;
    private int lastLogTrem = 0;
}
