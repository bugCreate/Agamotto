package com.tang.platform.consistency.jraft.netty.client;

import com.tang.platform.consistency.jraft.meta.node.NodeMeta;
import com.tang.platform.consistency.jraft.netty.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class InitHandler extends ChannelInboundHandlerAdapter {

    private final NodeMeta nodeMeta;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Msg registerMsg = new Msg(0, -1l, 0, new byte[0]);
        registerMsg.setNodeId(Integer.valueOf(nodeMeta.getId().getName()));
        ctx.writeAndFlush(registerMsg);
    }
}
