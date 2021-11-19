package com.tang.platform.consistency.jraft.cluster;

import com.google.common.eventbus.EventBus;
import com.tang.platform.consistency.jraft.DefaultMsgDeliver;
import com.tang.platform.consistency.jraft.cluster.node.Node;
import com.tang.platform.consistency.jraft.cluster.node.NodeBuilder;
import com.tang.platform.consistency.jraft.communication.MsgDeliver;
import com.tang.platform.consistency.jraft.innermq.InnerMsgDeliver;
import com.tang.platform.consistency.jraft.log.DefaultJraftLog;
import com.tang.platform.consistency.jraft.log.JraftLog;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import com.tang.platform.consistency.jraft.meta.node.NodeMeta;
import com.tang.platform.consistency.jraft.scheduler.DefaultScheduler;
import com.tang.platform.consistency.jraft.scheduler.Scheduler;
import com.tang.platform.consistency.jraft.task.DirectTaskExecutor;
import com.tang.platform.consistency.jraft.task.TaskExecutor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@AllArgsConstructor
public class Cluster {
    private NodeId selfId;
    private NodeGroup nodeGroup;
    private Node self;

    public Cluster init(ClusterConfig clusterConfig) {
        DefaultScheduler defaultScheduler = new DefaultScheduler(clusterConfig.getMinElectionTimetout(),
                clusterConfig.getMaxElectionTimeout(),
                clusterConfig.getLogReplicationDelay(), clusterConfig.getLogReplicationInterval());
        DirectTaskExecutor directTaskExecutor = new DirectTaskExecutor();

        MsgDeliver msgDeliver = new DefaultMsgDeliver(nodeGroup.fetchMember(selfId).getNodeMeta());
        JraftLog jraftLog = new DefaultJraftLog();
        return init(clusterConfig, defaultScheduler, directTaskExecutor, msgDeliver, jraftLog);
    }

    public Cluster init(ClusterConfig clusterConfig, Scheduler scheduler, TaskExecutor taskExecutor, MsgDeliver msgDeliver, JraftLog jraftLog) {
        NodeBuilder nodeBuilder = NodeBuilder.newBuilder();
        self = nodeBuilder.id(new NodeId(clusterConfig.getId())).innerMsgDeliver(new InnerMsgDeliver(new EventBus()))
                .msgDeliver(msgDeliver).nodeGroup(transfer(clusterConfig)).scheduler(scheduler).taskExewcutor(taskExecutor).jraftLog(jraftLog).build();
        self.register();
        return this;
    }

    NodeGroup transfer(ClusterConfig clusterConfig) {
        List<NodeMeta> nodeMetas = new ArrayList<>();
        clusterConfig.getClusterMap().forEach((key, value) -> nodeMetas.add(new NodeMeta(new NodeId(key), value)));
        return new NodeGroup(nodeMetas);
    }

    public void start() {
        log.info("jraft cluster start....");
        self.start();
    }

    public void stop() {
        self.stop();
    }
}
