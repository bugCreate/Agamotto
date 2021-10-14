package com.tang.platform.consistency.jraft.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Msg> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Msg msg, ByteBuf out)
        throws Exception {
        out.writeByte(msg.getType());
        out.writeByte(msg.getService());
        out.writeInt(msg.getNodeId());
        out.writeLong(msg.getMsgId());
        out.writeLong(msg.getLength());
        out.writeBytes(msg.getContents());
    }
}
