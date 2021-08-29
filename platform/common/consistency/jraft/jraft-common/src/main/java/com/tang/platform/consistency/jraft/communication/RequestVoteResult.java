package com.tang.platform.consistency.jraft.communication;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestVoteResult {
    private final int term;
    private final boolean voteGranted;
}
