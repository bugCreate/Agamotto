package com.tang.platform.consistency.jraft.cluster.node;

import com.tang.platform.consistency.jraft.communication.MsgDeliver;
import com.tang.platform.consistency.jraft.cluster.NodeGroup;
import com.tang.platform.consistency.jraft.innermq.InnerMsgDeliver;
import com.tang.platform.consistency.jraft.log.JraftLog;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import com.tang.platform.consistency.jraft.scheduler.Scheduler;
import com.tang.platform.consistency.jraft.task.TaskExecutor;
import lombok.Data;

@Data
public class NodeContext {
    private NodeId selfId;
    private NodeGroup nodeGroup;
    private TaskExecutor taskExecutor;
    private Scheduler scheduler;
    private MsgDeliver msgDeliver;
    private InnerMsgDeliver innerMsgDeliver;
    private JraftLog jraftLog;
}
