package com.tang.platform.consistency.jraft.netty.server;

import io.netty.channel.socket.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionHolder {

    private static Map<Integer, SocketChannel> connectionMap = new ConcurrentHashMap<>();

    public static void register(int nodeId, SocketChannel channel) {
        connectionMap.put(nodeId, channel);
    }

    public static SocketChannel getConnection(int nodeId) {
        return connectionMap.get(nodeId);
    }

    public static void unregister(int nodeId) {
        connectionMap.remove(nodeId);
    }
}
