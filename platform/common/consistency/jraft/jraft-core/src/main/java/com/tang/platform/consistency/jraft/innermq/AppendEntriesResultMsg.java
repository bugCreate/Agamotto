package com.tang.platform.consistency.jraft.innermq;

import com.tang.platform.consistency.jraft.communication.AppendEntriesResult;

public class AppendEntriesResultMsg extends EventBusMsg<AppendEntriesResult> {
    public AppendEntriesResultMsg(Object source) {
        super(source);
    }

    @Override
    public AppendEntriesResult getSource() {
        return super.getSource();
    }
}
