package com.tang.platform.consistency.jraft.election;

import com.tang.platform.consistency.jraft.scheduler.ElectionTimeout;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CandidateNodeRole extends AbstractNodeRole {
    private final int votesCount;
    private final ElectionTimeout electionTimeout;

    public CandidateNodeRole(int term, int votesCount, ElectionTimeout electionTimeout) {
        super(RoleName.CANDIDATE, term);
        this.votesCount = votesCount;
        this.electionTimeout = electionTimeout;
    }

    @Override
    public void cancelTimeoutOrTask() {
        electionTimeout.cancel();
    }
}
