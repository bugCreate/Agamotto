package com.tang.platform.consistency.jraft.communication;

import com.tang.platform.consistency.jraft.meta.node.NodeEndpoint;

import java.util.List;

public interface MsgDeliver {
    void init();

    void sendRequestVote(RequestVote vote, List<NodeEndpoint> distNodeEndpoints);

    void replyRequestVote(RequestVoteResult requestVoteResult, NodeEndpoint distNodeEndpoint);

    void sendAppendEntries(AppendEntries appendEntries, NodeEndpoint distNodeEndpoint);

    void replyRequestAppendEntries(AppendEntriesResult appendEntriesResult, NodeEndpoint distNodeEndpoint);

    void close();
}
