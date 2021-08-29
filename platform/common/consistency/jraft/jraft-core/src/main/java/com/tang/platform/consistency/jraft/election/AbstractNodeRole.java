package com.tang.platform.consistency.jraft.election;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractNodeRole {
    private final RoleName roleName;
    private final int term;

    public abstract void cancelTimeoutOrTask();
}
