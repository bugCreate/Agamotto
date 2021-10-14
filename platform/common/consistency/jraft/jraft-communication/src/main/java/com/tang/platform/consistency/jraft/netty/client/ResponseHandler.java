package com.tang.platform.consistency.jraft.netty.client;

import com.tang.platform.consistency.jraft.netty.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ResponseHandler extends ChannelInboundHandlerAdapter {
    private final Client client;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Msg message = (Msg) msg;
        client.reply(message);
    }

}
