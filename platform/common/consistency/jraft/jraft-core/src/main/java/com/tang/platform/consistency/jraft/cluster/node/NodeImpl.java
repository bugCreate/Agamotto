package com.tang.platform.consistency.jraft.cluster.node;

import com.google.common.eventbus.Subscribe;
import com.tang.platform.consistency.jraft.cluster.ClusterMember;
import com.tang.platform.consistency.jraft.communication.AppendEntries;
import com.tang.platform.consistency.jraft.communication.AppendEntriesResult;
import com.tang.platform.consistency.jraft.communication.RequestVote;
import com.tang.platform.consistency.jraft.communication.RequestVoteResult;
import com.tang.platform.consistency.jraft.election.*;
import com.tang.platform.consistency.jraft.innermq.AppendEntriesMsg;
import com.tang.platform.consistency.jraft.innermq.AppendEntriesResultMsg;
import com.tang.platform.consistency.jraft.innermq.RequestVoteResultMsg;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import com.tang.platform.consistency.jraft.scheduler.ElectionTimeout;
import com.tang.platform.consistency.jraft.scheduler.LogReplicationScheduledContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class NodeImpl implements Node {
    private boolean started;
    private AbstractNodeRole role;
    private final NodeContext context;

    public NodeImpl(NodeContext context) {
        this.context = context;
    }


    @Override
    public synchronized void start() {
        if (started) {
            log.debug("node has started.");
            return;
        }
        int currentTerm = context.getJraftLog().getLastEntryMeta().getTerm();
        // 第一次启动会等待一次选举超时
        changeToRole(new FollowerNodeRole(currentTerm, null, null, scheduleElectionTimeout()));
        started = true;
    }

    @Override
    public synchronized void stop() {
        if (!started) {
            throw new IllegalStateException("node not started.");

        }
        started = false;

    }

    @Override
    public void register() {
        context.getInnerMsgDeliver().register(this);
    }

    void changeToRole(AbstractNodeRole newRole) {
        log.debug("node {} change role from {} to {}", context.getSelfId(), this.role, newRole);
        role = newRole;
    }

    private ElectionTimeout scheduleElectionTimeout() {
        return context.getScheduler().scheduleElectionTimeout(this::electionTimeout);
    }

    void electionTimeout() {
        context.getTaskExecutor().submit(this::doProcessElectionTimeout);
    }

    private void doProcessElectionTimeout() {
        // leader 忽略选举超时
        if (role.getRoleName().equals(RoleName.LEADER)) {
            log.warn("current node is leader,ignore election timeout.[node={}]", context.getSelfId());
            return;
        }
        //对于follower 来说是发起选举
        //对于candidate 来说是再次发起选举
        //选举term +1
        EntryMeta lastEntryMeta = context.getJraftLog().getLastEntryMeta();
        int newTerm = role.getTerm() + 1;
        role.cancelTimeoutOrTask();
        log.info("start election.");
        changeToRole(new CandidateNodeRole(newTerm, 0, scheduleElectionTimeout()));
        RequestVote requestVote = new RequestVote();
        requestVote.setTerm(newTerm);
        requestVote.setNodeId(context.getSelfId());
        requestVote.setLastLogIndex(lastEntryMeta.getIndex());
        requestVote.setLastLogTrem(lastEntryMeta.getTerm());
        context.getMsgDeliver().sendRequestVote(requestVote, context.getNodeGroup().fetchOtherNodeEndpoints(context.getSelfId()));
    }

    private RequestVoteResult doProcessRequestVote(RequestVote vote) {

        // 如果对方term 比自己小，则不投票，返回自己的term
        if (vote.getTerm() < role.getTerm()) {
            log.debug("term from request < current term, not vote.[request node={},request term={},current term={}]", vote.getNodeId(), vote.getTerm(), role.getTerm());
            return new RequestVoteResult(role.getTerm(), false);
        }
        // 如果请求的term 比自己大，转换成follower
        if (vote.getTerm() > role.getTerm()) {
            becomeFollower(vote.getTerm(), voteForCandidate(vote.getLastLogIndex(), vote.getLastLogTrem()) ? vote.getNodeId() : null, null, true);
            return new RequestVoteResult(vote.getTerm(), voteForCandidate(vote.getLastLogIndex(), vote.getLastLogTrem()));
        }
        switch (role.getRoleName()) {
            case FOLLOWER:
                FollowerNodeRole followerNodeRole = (FollowerNodeRole) role;
                NodeId votedFor = followerNodeRole.getVotedFor();
                // 没有投过票，或者给对方投过
                if (votedFor == null && voteForCandidate(vote.getLastLogIndex(), vote.getLastLogTrem()) || votedFor.equals(vote.getNodeId())) {
                    becomeFollower(vote.getTerm(), vote.getNodeId(), null, true);
                    return new RequestVoteResult(role.getTerm(), true);
                }
                return new RequestVoteResult(role.getTerm(), false);
            case CANDIDATE:
            case LEADER:
                return new RequestVoteResult(role.getTerm(), false);
            default:
                throw new IllegalStateException("unexpected node role [" + role.getRoleName() + "]");
        }
    }

    private void becomeFollower(int term, NodeId voteFor, NodeId leaderId, boolean scheduleElectionTimeout) {
        role.cancelTimeoutOrTask();
//        if (leaderId!=null && leaderId.equals(context.getSelfId())){
//            log.info("current leader is");
//        }
        ElectionTimeout electionTimeout = scheduleElectionTimeout ? scheduleElectionTimeout() : ElectionTimeout.NONE;
        changeToRole(new FollowerNodeRole(term, voteFor, leaderId, electionTimeout));

    }

    // todo 暂时无条件投票
    private boolean voteForCandidate(int index, int term) {
        return !context.getJraftLog().isNewerThan(index, term);

    }

    @Subscribe
    public void processRequestVoteResult(RequestVoteResultMsg msg) {
        RequestVoteResult requestVoteResult = msg.getSource();
        context.getTaskExecutor().submit(() -> doProcessRequestVoteResult(requestVoteResult));
    }

    private void doProcessRequestVoteResult(RequestVoteResult voteResult) {
        // 对方term 比自己大，退化为follower
        if (voteResult.getTerm() > role.getTerm()) {
            becomeFollower(voteResult.getTerm(), null, null, true);
            return;
        }
        if (!role.getRoleName().equals(RoleName.CANDIDATE)) {
            log.debug("receive request vote result, but current role is not candidate,ignore");
            return;
        }
        // 对方term 比自己小，或者没有投票，忽略
        if (voteResult.getTerm() < role.getTerm() || !voteResult.isVoteGranted()) {
            return;
        }
        // 可能投两次票，是否需要校验是否已经投过票了
        int currentVotesCount = ((CandidateNodeRole) role).getVotesCount() + 1;
        int nodeSize = context.getNodeGroup().getNodeSize();
        log.debug("[votes count={},node szie={}]", currentVotesCount, nodeSize);
        role.cancelTimeoutOrTask();
        if (currentVotesCount > nodeSize / 2) {
            log.info("become leader,[term={}]", role.getTerm());
            changeToRole(new LeaderNodeRole(role.getTerm(), scheduleLogReplicationTask()));
            //todo no-op log
        } else {
            // 修改投票数
            changeToRole(new CandidateNodeRole(role.getTerm(), currentVotesCount, scheduleElectionTimeout()));
        }
    }

    private LogReplicationScheduledContext scheduleLogReplicationTask() {
        return context.getScheduler().scheduleLogReplicationTask(this::replicateLog);
    }

    private void replicateLog() {
        List<ClusterMember> otherClusterMembers = context.getNodeGroup().fetchOtherClusterMember(context.getSelfId());
        for (ClusterMember clusterMember : otherClusterMembers) {
            context.getTaskExecutor().submit(() -> doReplicateLog(clusterMember, -1));
        }
    }

    private void doReplicateLog(ClusterMember member, int maxEntries) {
        log.debug("replication log");
        AppendEntries appendEntries = context.getJraftLog().createAppendEntries(role.getTerm(), context.getSelfId(), member.getNextIndex(), maxEntries);
        context.getMsgDeliver().sendAppendEntries(appendEntries, member.getNodeMeta().getEndpoint());
    }

    @Subscribe
    public void processAppendEntries(AppendEntriesMsg msg) {
        AppendEntries appendEntries = msg.getSource();
        context.getTaskExecutor().submit(() -> {
            context.getMsgDeliver().replyRequestAppendEntries(doProcessAppendEntries(appendEntries), context.getNodeGroup().fetchMember(appendEntries.getLeaderId()).getNodeMeta().getEndpoint());
        });
    }

    private AppendEntriesResult doProcessAppendEntries(AppendEntries appendEntries) {
        if (appendEntries.getTerm() < role.getTerm()) {
            return new AppendEntriesResult(role.getTerm(), false, context.getSelfId(), appendEntries.getLastEntryIndex());
        }
        if (appendEntries.getTerm() > role.getTerm()) {
            becomeFollower(appendEntries.getTerm(), null, appendEntries.getLeaderId(), true);
            return new AppendEntriesResult(appendEntries.getTerm(), appendEntries(appendEntries), context.getSelfId(), appendEntries.getLastEntryIndex());
        }
        switch (role.getRoleName()) {
            case FOLLOWER:
                becomeFollower(appendEntries.getTerm(), ((FollowerNodeRole) role).getVotedFor(), appendEntries.getLeaderId(), true);
                return new AppendEntriesResult(appendEntries.getTerm(), appendEntries(appendEntries), context.getSelfId(), appendEntries.getLastEntryIndex());
            case CANDIDATE:
                becomeFollower(appendEntries.getTerm(), null, appendEntries.getLeaderId(), true);
                return new AppendEntriesResult(appendEntries.getTerm(), appendEntries(appendEntries), context.getSelfId(), appendEntries.getLastEntryIndex());
            case LEADER:
                log.warn("receive append entries form another leader,ignore.[another leader={}]", appendEntries.getLeaderId());
                return new AppendEntriesResult(appendEntries.getTerm(), false, context.getSelfId(), appendEntries.getLastEntryIndex());
            default:
                throw new IllegalStateException("unexpected node role[" + role.getRoleName() + "]");

        }

    }

    private boolean appendEntries(AppendEntries appendEntries) {

        boolean result = context.getJraftLog().appendEntriesFromLeader(appendEntries.getPrevLogIndex(), appendEntries.getPrevLogTerm(), appendEntries.getEntries());
        if (result) {
            context.getJraftLog().advanceCommitIndex(Math.min(appendEntries.getLeaderCommit(), appendEntries.getLastEntryIndex()), appendEntries.getTerm());
        }
        return result;
    }

    @Subscribe
    public void onReceiveAppendEntriesResult(AppendEntriesResultMsg msg) {
        AppendEntriesResult appendEntriesResult = msg.getSource();
        context.getTaskExecutor().submit(() -> doProcessAppendEntriesResult(appendEntriesResult));
    }

    private void doProcessAppendEntriesResult(AppendEntriesResult result) {
        if (result.getTerm() > role.getTerm()) {
            // 对方比自己大，退化为follower
            becomeFollower(result.getTerm(), null, null, true);
            return;
        }
        if (!RoleName.LEADER.equals(role.getRoleName())) {
            log.warn("receive append entries result from other node,but current node is not leader,ignore.");
            return;
        }
        NodeId sourceNodeId = result.getNodeId();
        ClusterMember member = context.getNodeGroup().fetchMember(sourceNodeId);
        if (null == member) {
            //todo log
            return;
        }
        if (result.isSuccess()) {
            if (member.advanceReplicatingState(result.getLastLogIndex())) {
                context.getJraftLog().advanceCommitIndex(context.getNodeGroup().getMatchIndexOfMajor(), role.getTerm());
            }
        }else {
            // 对方回复失败，回退同步的nextindex，防止是match index问题导致同步失败。
            if (!member.backOffNextIndex()){
                //todo log
            }
        }
    }

}
