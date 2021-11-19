package com.tang.platform.consistency.jraft;

import com.google.protobuf.InvalidProtocolBufferException;
import com.tang.platform.consistency.jraft.netty.Msg;

public interface MsgConsumer {

    void consume(Msg msg) throws Exception;
}
