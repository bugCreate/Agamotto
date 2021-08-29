package com.tang.platform.consistency.jraft.cluster;

import com.tang.platform.consistency.jraft.meta.node.NodeMeta;
import lombok.Data;

@Data
public class ClusterMember {
    private NodeMeta nodeMeta;
    private ReplicatingState replicatingState;
    //todo log

    public ClusterMember(NodeMeta nodeMeta) {
        this(nodeMeta, null);
    }

    public ClusterMember(NodeMeta nodeMeta, ReplicatingState replicatingState) {
        this.nodeMeta = nodeMeta;
        this.replicatingState = replicatingState;
    }

    public ReplicatingState getReplicatingState() {
        return replicatingState;
    }

    public void setReplicatingState(ReplicatingState replicatingState) {
        this.replicatingState = replicatingState;
    }

    public boolean advanceReplicatingState(int index) {
        return replicatingState.advance(index);
    }

    public boolean backOffNextIndex() {
        return replicatingState.backOffNextIndex();
    }

    public int getNextIndex() {

        return replicatingState.getNextIndex();
    }

    public NodeMeta getNodeMeta() {
        return nodeMeta;
    }

    boolean isReplicatingStateSet() {
        return replicatingState != null;
    }

    private ReplicatingState ensureReplicatingState() {
        if (replicatingState == null) {
            throw new IllegalStateException("replicating state not set.");
        }
        return replicatingState;
    }

    void replicateNow() {
        replicateAt(System.currentTimeMillis());
    }

    void replicateAt(long replicateAt) {
        ReplicatingState replicatingState = ensureReplicatingState();
        replicatingState.setLastReplicatedAt(replicateAt);
        replicatingState.setReplicating(true);
    }

    boolean isReplicating() {
        return ensureReplicatingState().isReplicating();
    }

    void stopReplicate() {
        ensureReplicatingState().setReplicating(false);
    }

    boolean shouldReplicate(long readTimeout) {
        ReplicatingState replicatingState = ensureReplicatingState();
        return !replicatingState.isReplicating() || System.currentTimeMillis() - replicatingState.getLastReplicatedAt() > readTimeout;
    }
}
