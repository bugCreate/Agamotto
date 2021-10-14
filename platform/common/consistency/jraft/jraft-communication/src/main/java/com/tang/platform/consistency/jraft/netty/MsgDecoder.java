package com.tang.platform.consistency.jraft.netty;

import com.tang.platform.consistency.jraft.netty.Msg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class MsgDecoder extends LengthFieldBasedFrameDecoder {

    private static final int HEADER_SIZE = 22;


    public MsgDecoder() {
        super(Integer.MAX_VALUE, 14, 8);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        in = (ByteBuf) super.decode(ctx, in);
        if (null == in) {
            return null;
        }
        if (in.readableBytes() < HEADER_SIZE) {
            throw new Exception("bytes not enough.");
        }
        byte type = in.readByte();
        byte service = in.readByte();
        int nodeId = in.readInt();
        long msgId = in.readLong();
        long length = in.readLong();
        if (in.readableBytes() < length) {
            throw new Exception("data length not right.");
        }
        byte[] contents = new byte[in.readableBytes()];
        in.readBytes(contents);
        Msg msg = new Msg(type, service, nodeId, msgId, length, contents);
        return msg;
    }
}
