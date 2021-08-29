package com.tang.platform.consistency.jraft;

import com.tang.platform.consistency.jraft.communication.*;
import com.tang.platform.consistency.jraft.meta.node.NodeEndpoint;

import java.util.List;
// todo
public class DefaultMsgDeliver implements MsgDeliver {
    @Override
    public void init() {

    }

    @Override
    public void sendRequestVote(RequestVote vote, List<NodeEndpoint> distNodeEndpoints) {

    }

    @Override
    public void replyRequestVote(RequestVoteResult requestVoteResult, NodeEndpoint distNodeEndpoint) {

    }

    @Override
    public void sendAppendEntries(AppendEntries appendEntries, NodeEndpoint distNodeEndpoints) {

    }

    @Override
    public void replyRequestAppendEntries(AppendEntriesResult appendEntriesResult, NodeEndpoint distNodeEndpoint) {

    }

    @Override
    public void close() {

    }
}
