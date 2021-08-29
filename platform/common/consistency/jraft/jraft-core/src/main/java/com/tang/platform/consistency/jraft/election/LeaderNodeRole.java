package com.tang.platform.consistency.jraft.election;

import com.tang.platform.consistency.jraft.scheduler.LogReplicationScheduledContext;
import lombok.ToString;

@ToString
public class LeaderNodeRole extends AbstractNodeRole {
    private final LogReplicationScheduledContext logReplicationScheduledContext;

    public LeaderNodeRole(int term, LogReplicationScheduledContext logReplicationScheduledContext) {
        super(RoleName.LEADER, term);
        this.logReplicationScheduledContext = logReplicationScheduledContext;
    }

    @Override
    public void cancelTimeoutOrTask() {
        logReplicationScheduledContext.cancel();
    }
}
