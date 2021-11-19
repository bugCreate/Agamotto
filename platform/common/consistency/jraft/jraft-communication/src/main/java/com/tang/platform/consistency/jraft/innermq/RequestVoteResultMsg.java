package com.tang.platform.consistency.jraft.innermq;

import com.tang.platform.consistency.jraft.communication.RequestVoteResult;

public class RequestVoteResultMsg extends EventBusMsg<RequestVoteResult> {
    public RequestVoteResultMsg(Object source) {
        super(source);
    }

    @Override
    public RequestVoteResult getSource() {
        return super.getSource();
    }
}
