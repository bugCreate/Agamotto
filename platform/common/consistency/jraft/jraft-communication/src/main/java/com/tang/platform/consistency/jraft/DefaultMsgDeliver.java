package com.tang.platform.consistency.jraft;

import com.tang.platform.consistency.jraft.communication.*;
import com.tang.platform.consistency.jraft.meta.node.NodeEndpoint;

import com.tang.platform.consistency.jraft.meta.node.NodeMeta;
import com.tang.platform.consistency.jraft.netty.client.Client;
import com.tang.platform.consistency.jraft.netty.server.Server;
import java.util.List;
// todo
public class DefaultMsgDeliver implements MsgDeliver {
    private NodeMeta nodeMeta;
    private MsgConsumer msgConsumer;
    private Client client;
    private Server server;

    public DefaultMsgDeliver(NodeMeta nodeMeta) {
        this.nodeMeta =nodeMeta;
        this.client = new Client(nodeMeta);
        this.server = new Server(msgConsumer);
    }

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
