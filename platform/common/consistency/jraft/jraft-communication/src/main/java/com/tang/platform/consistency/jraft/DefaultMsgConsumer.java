package com.tang.platform.consistency.jraft;

import com.google.protobuf.InvalidProtocolBufferException;
import com.tang.platform.consistency.jraft.communication.AppendEntries;
import com.tang.platform.consistency.jraft.innermq.AppendEntriesMsg;
import com.tang.platform.consistency.jraft.innermq.InnerMsgDeliver;
import com.tang.platform.consistency.jraft.netty.Msg;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultMsgConsumer implements MsgConsumer{
    private final InnerMsgDeliver innerMsgDeliver;
    @Override
    public void consume(Msg msg) throws Exception {
        switch (msg.getType()){
            case 1:
                JraftProtoMsg.AppendEntries appendEntries =JraftProtoMsg.AppendEntries.parseFrom(msg.getContents());
                //AppendEntries entries = ProtoMsgConvertor.
                //AppendEntriesMsg appendEntriesMsg = new AppendEntriesMsg()
            case 2:
            case 3:
            case 4:
        }
    }
}
