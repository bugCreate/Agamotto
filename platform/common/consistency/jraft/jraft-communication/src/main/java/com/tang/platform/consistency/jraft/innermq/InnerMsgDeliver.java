package com.tang.platform.consistency.jraft.innermq;

import com.google.common.eventbus.EventBus;

public class InnerMsgDeliver {
    private final EventBus eventBus;

    public InnerMsgDeliver(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void register(Object listener) {
        eventBus.register(listener);
    }

    public void post(Object msg) {
        eventBus.post(msg);
    }
}
