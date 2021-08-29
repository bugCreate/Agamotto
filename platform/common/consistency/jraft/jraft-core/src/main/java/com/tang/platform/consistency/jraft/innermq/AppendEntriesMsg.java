package com.tang.platform.consistency.jraft.innermq;

import com.tang.platform.consistency.jraft.communication.AppendEntries;

public class AppendEntriesMsg extends EventBusMsg<AppendEntries> {
    public AppendEntriesMsg(Object source) {
        super(source);
    }

    @Override
    public AppendEntries getSource() {
        return super.getSource();
    }
}
