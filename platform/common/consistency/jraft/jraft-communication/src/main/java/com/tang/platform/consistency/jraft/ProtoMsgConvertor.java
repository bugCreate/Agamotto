package com.tang.platform.consistency.jraft;

import com.tang.platform.consistency.jraft.communication.AppendEntries;
import com.tang.platform.consistency.jraft.communication.RequestVote;
import com.tang.platform.consistency.jraft.communication.RequestVoteResult;
import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryFactory;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import java.util.ArrayList;
import java.util.List;

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
        return JraftProtoMsg.RequestVoteResult.newBuilder().setTerm(voteResult.getTerm())
            .setVotedGranted(voteResult.isVoteGranted()).build();
    }

    public static AppendEntries convert(JraftProtoMsg.AppendEntries protoAppendEntries) {
        AppendEntries appendEntries = new AppendEntries();
        appendEntries.setTerm(protoAppendEntries.getTerm());
        appendEntries.setLeaderId(new NodeId(protoAppendEntries.getLeaderId()));
        appendEntries.setPrevLogIndex(protoAppendEntries.getPrevLogIndex());
        appendEntries.setPrevLogTerm(protoAppendEntries.getPrevLogTerm());
        appendEntries.setLastEntryIndex(protoAppendEntries.getLastEntryIndex());

        appendEntries.setLeaderCommit(protoAppendEntries.getLeaderCommit());
        List<Entry> entries = new ArrayList<>();
        for (JraftProtoMsg.AppendEntries.Entry pEntry : protoAppendEntries.getEntriesList()) {
            entries.add(EntryFactory.create(pEntry.getKind(), pEntry.getIndex(), pEntry.getTerm(),
                pEntry.getPayload().toByteArray()));
        }
        appendEntries.setEntries(entries);
        return appendEntries;
    }

    public static JraftProtoMsg.AppendEntries convertToProto(AppendEntries appendEntries) {
        return JraftProtoMsg.AppendEntries.newBuilder().setTerm(appendEntries.getTerm())
            .setLeaderId(appendEntries.getLeaderId().getName()).setPrevLogIndex(
                appendEntries.getPrevLogIndex()).setPrevLogTerm(appendEntries.getPrevLogTerm())
            .setLastEntryIndex(
                appendEntries.getLastEntryIndex()).build();
    }


}
