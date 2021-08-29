package com.tang.platform.consistency.jraft.election;

import com.tang.platform.consistency.jraft.meta.node.NodeId;
import com.tang.platform.consistency.jraft.scheduler.ElectionTimeout;

public class FollowerNodeRole extends AbstractNodeRole {
    private final NodeId votedFor;
    private final NodeId leaderId;
    private final ElectionTimeout electionTimeout;

    public FollowerNodeRole(int term, NodeId voterdFor, NodeId leaderId, ElectionTimeout electionTimeout) {
        super(RoleName.FOLLOWER, term);
        this.votedFor = voterdFor;
        this.leaderId = leaderId;
        this.electionTimeout = electionTimeout;
    }

    public NodeId getVotedFor() {
        return votedFor;
    }

    public NodeId getLeaderId() {
        return leaderId;
    }

    @Override
    public void cancelTimeoutOrTask() {
        electionTimeout.cancel();
    }
}
