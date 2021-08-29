package com.tang.platform.consistency.jraft;

import com.tang.platform.consistency.jraft.communication.RequestVote;
import com.tang.platform.consistency.jraft.communication.RequestVoteResult;
import com.tang.platform.consistency.jraft.meta.node.NodeId;

public class ProtoMsgConvertor {
    public static NodeId convert(JraftProtoMsg.NodeId nodeId) {
        return new NodeId(nodeId.getId());
    }

    public static JraftProtoMsg.NodeId convertToProto(NodeId nodeId) {
        return JraftProtoMsg.NodeId.newBuilder().setId(nodeId.getName()).build();
    }

    public static RequestVote convert(JraftProtoMsg.RequestVote requestVote) {
        RequestVote result = new RequestVote();
        result.setTerm(requestVote.getTerm());
        result.setNodeId(NodeId.of(requestVote.getCandidateId()));
        result.setLastLogIndex(requestVote.getLastLogIndex());
        result.setLastLogTrem(requestVote.getLastLogTerm());
        return result;
    }

    public static JraftProtoMsg.RequestVote convertToProto(RequestVote vote) {
        return JraftProtoMsg.RequestVote.newBuilder().setCandidateId(vote.getNodeId().getName())
                .setLastLogIndex(vote.getLastLogIndex()).setLastLogTerm(vote.getLastLogTrem())
                .setTerm(vote.getTerm()).build();
    }

    public static RequestVoteResult convert(JraftProtoMsg.RequestVoteResult voteResult) {
        return new RequestVoteResult(voteResult.getTerm(), voteResult.getVotedGranted());
    }

    public static JraftProtoMsg.RequestVoteResult convertToProto(RequestVoteResult voteResult) {
        return JraftProtoMsg.RequestVoteResult.newBuilder().setTerm(voteResult.getTerm()).setVotedGranted(voteResult.isVoteGranted()).build();
    }

}
