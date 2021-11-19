package com.tang.platform.consistency.jraft.innermq;

import java.util.EventObject;

public abstract class EventBusMsg<T> extends EventObject {

    public EventBusMsg(Object source) {
        super(source);
    }

    @Override
    public T getSource() {
        return (T)super.getSource();
    }
}
