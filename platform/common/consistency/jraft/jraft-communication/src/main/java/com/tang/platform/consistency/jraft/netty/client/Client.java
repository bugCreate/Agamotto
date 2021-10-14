package com.tang.platform.consistency.jraft.netty.client;

import com.tang.platform.consistency.jraft.meta.node.NodeMeta;
import com.tang.platform.consistency.jraft.netty.Msg;
import com.tang.platform.consistency.jraft.netty.MsgDecoder;
import com.tang.platform.consistency.jraft.netty.MsgEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Client {

    private final NodeMeta nodeMeta;
    private Map<Integer, ChannelFuture> clientMap = new ConcurrentHashMap<>();
    private Map<Long, CompletableFuture<Msg>> futureMap = new ConcurrentHashMap<>();

    private void connect(String host, int port, Client c) throws Exception {
        EventLoopGroup client = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(client).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                .handler(
                    new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new MsgEncoder());
                            channel.pipeline().addLast(new MsgDecoder());
                            channel.pipeline().addLast(new InitHandler(nodeMeta));
                            channel.pipeline().addLast(new ResponseHandler(c));
                        }
                    });
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();

        } finally {
            client.shutdownGracefully();
        }
    }

    private void send(int nodeId, Msg msg) throws Exception {
        if (!clientMap.containsKey(nodeId)) {
            connect(nodeMeta.getEndpoint().getAddress().getHost(),
                nodeMeta.getEndpoint().getAddress().getPort(), this);
        }
        ChannelFuture channelFuture = clientMap.get(nodeId);
        if (null == channelFuture || !channelFuture.channel().isActive()) {
            throw new Exception("no active channel.");
        }
        channelFuture.channel().writeAndFlush(msg);
    }

    public CompletableFuture<Msg> invoke(int nodeId, Msg msg) throws Exception {
        CompletableFuture<Msg> future = new CompletableFuture<>();
        futureMap.put(msg.getMsgId(), future);
        send(nodeId, msg);
        return future;
    }

    public void reply(Msg msg) throws Exception {
        if (!futureMap.containsKey(msg.getMsgId())) {
            return;
        }
        CompletableFuture<Msg> future = futureMap.get(msg.getMsgId());
        future.complete(msg);
    }
}
