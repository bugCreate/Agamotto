package com.tang.platform.consistency.jraft.cluster;

import lombok.Data;

@Data
public class ReplicatingState {
    private int nextIndex;
    private int matchIndex;
    private boolean replicating = false;
    private long lastReplicatedAt = 0;

    public ReplicatingState(int nextIndex) {
        this(nextIndex, 0);
    }

    public ReplicatingState(int nextIndex, int matchIndex) {
        this.nextIndex = nextIndex;
        this.matchIndex = matchIndex;
    }

    boolean advance(int lastReplicationLogIndex) {
        boolean result = (matchIndex != lastReplicationLogIndex || nextIndex != lastReplicationLogIndex + 1);
        matchIndex = lastReplicationLogIndex;
        nextIndex = lastReplicationLogIndex + 1;
        return result;
    }
    boolean backOffNextIndex(){
        if (nextIndex>1){
            nextIndex--;
            return true;
        }
        return false;
    }
}
