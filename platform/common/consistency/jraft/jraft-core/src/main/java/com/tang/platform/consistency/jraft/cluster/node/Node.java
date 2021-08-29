package com.tang.platform.consistency.jraft.cluster.node;

public interface Node {
    void start();

    void stop();

    void register();
}
