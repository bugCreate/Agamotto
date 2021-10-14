package com.tang.platform.consistency.jraft;

import com.tang.platform.consistency.jraft.netty.Msg;

public interface MsgConsumer {

    void consume(Msg msg);
}
