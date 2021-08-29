package com.tang.platform.consistency.jraft.cluster;

import com.tang.platform.consistency.jraft.meta.node.NodeEndpoint;
import lombok.Data;

import java.util.Map;

@Data
public class ClusterConfig {
    private int minElectionTimetout;
    private int maxElectionTimeout;
    private int logReplicationDelay;
    private int logReplicationInterval;
    private NodeEndpoint endPoint;
    private String id;
    private Map<String, NodeEndpoint> clusterMap;
}
