package com.tang.platform.consistency.jraft.cluster.node;

import com.tang.platform.consistency.jraft.cluster.NodeGroup;
import com.tang.platform.consistency.jraft.communication.MsgDeliver;
import com.tang.platform.consistency.jraft.innermq.InnerMsgDeliver;
import com.tang.platform.consistency.jraft.log.JraftLog;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import com.tang.platform.consistency.jraft.scheduler.Scheduler;
import com.tang.platform.consistency.jraft.task.TaskExecutor;

public class NodeBuilder {
    private final NodeContext nodeContext;

    private NodeBuilder() {
        this.nodeContext = new NodeContext();
    }

    public static NodeBuilder newBuilder() {
        return new NodeBuilder();
    }

    public NodeBuilder scheduler(Scheduler scheduler) {
        nodeContext.setScheduler(scheduler);
        return this;
    }

    public NodeBuilder id(NodeId nodeId) {
        nodeContext.setSelfId(nodeId);
        return this;
    }

    public NodeBuilder taskExewcutor(TaskExecutor taskExecutor) {
        nodeContext.setTaskExecutor(taskExecutor);
        return this;
    }

    public NodeBuilder msgDeliver(MsgDeliver msgDeliver) {
        nodeContext.setMsgDeliver(msgDeliver);
        return this;
    }

    public NodeBuilder innerMsgDeliver(InnerMsgDeliver innerMsgDeliver) {
        nodeContext.setInnerMsgDeliver(innerMsgDeliver);
        return this;
    }

    public NodeBuilder nodeGroup(NodeGroup nodeGroup) {
        nodeContext.setNodeGroup(nodeGroup);
        return this;
    }

    public NodeBuilder jraftLog(JraftLog jraftLog) {
        nodeContext.setJraftLog(jraftLog);
        return this;
    }

    public Node build() {
        return new NodeImpl(nodeContext);

    }
}
