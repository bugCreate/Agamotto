package com.tang.platform.consistency.jraft.communication;

import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AppendEntries {
    private int term;
    private NodeId leaderId;
    private int prevLogIndex=0;
    private int prevLogTerm;
    private int lastEntryIndex;
    private List<Entry> entries = Collections.emptyList();
    private int leaderCommit;
}
