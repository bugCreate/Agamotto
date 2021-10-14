package com.tang.platform.consistency.jraft.netty.server;

import com.tang.platform.consistency.jraft.netty.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Msg message = (Msg) msg;
        if (message.getType() == 0) {
            log.info("node connected.[nodeId={}]", message.getNodeId());
            ConnectionHolder.register(message.getNodeId(), (SocketChannel) ctx.channel());
        }
    }
}
