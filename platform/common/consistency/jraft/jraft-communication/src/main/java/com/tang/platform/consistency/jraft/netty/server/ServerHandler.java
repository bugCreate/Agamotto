package com.tang.platform.consistency.jraft.netty.server;

import com.tang.platform.consistency.jraft.MsgConsumer;
import com.tang.platform.consistency.jraft.netty.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private final MsgConsumer msgConsumer;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Msg message = (Msg) msg;
        log.info("receive msg {}", message);
        msgConsumer.consume(message);
    }
}
