package com.tang.platform.consistency.jraft.netty.server;

import com.tang.platform.consistency.jraft.MsgConsumer;
import com.tang.platform.consistency.jraft.netty.Msg;
import com.tang.platform.consistency.jraft.netty.MsgDecoder;
import com.tang.platform.consistency.jraft.netty.MsgEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Server {
    private final MsgConsumer msgConsumer;
    private ChannelFuture future;
    public void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup, workerGroup).channel(
                NioServerSocketChannel.class).localAddress(port).childHandler(
                new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new MsgDecoder());
                        channel.pipeline().addLast(new MsgEncoder());
                        channel.pipeline().addLast(new RegisterHandler());
                        channel.pipeline().addLast(new ServerHandler(msgConsumer));
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
            future = sbs.bind(port).sync();
            log.info("jraft server listen at {}", port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("jraft server exception", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void reply(int nodeId, long msgId, byte[] contents) {
        Msg msg = new Msg(nodeId, msgId, contents.length, contents);
        SocketChannel connection = ConnectionHolder.getConnection(nodeId);
        if (null == connection || !connection.isActive()) {
            ConnectionHolder.unregister(nodeId);
            return;
        }
        connection.writeAndFlush(msg);
    }
    public void close() throws Exception{
        future.channel().close();
    }
}
