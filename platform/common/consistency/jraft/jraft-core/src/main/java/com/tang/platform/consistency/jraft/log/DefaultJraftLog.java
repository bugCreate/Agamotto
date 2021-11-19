package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.communication.AppendEntries;
import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;
import com.tang.platform.consistency.jraft.meta.log.GeneralEntry;
import com.tang.platform.consistency.jraft.meta.log.NoOpEntry;
import com.tang.platform.consistency.jraft.meta.node.NodeId;

import java.util.List;

public class DefaultJraftLog implements JraftLog {

    @Override
    public EntryMeta getLastEntryMeta() {
        return null;
    }

    @Override
    public AppendEntries createAppendEntries(int term, NodeId selfId, int nextIndex, int maxEntries) {
        return null;
    }

    @Override
    public int getNextIndex() {
        return 0;
    }

    @Override
    public int getCommitIndex() {
        return 0;
    }

    @Override
    public boolean isNewerThan(int lastLogIndex, int lastLogTerm) {
        return false;
    }

    @Override
    public NoOpEntry appendEntry(int term) {
        return null;
    }

    @Override
    public GeneralEntry appendEntry(int term, byte[] command) {
        return null;
    }

    @Override
    public boolean appendEntriesFromLeader(int prevLogIndex, int prevLogTerm, List<Entry> entries) {
        return false;
    }

    @Override
    public void advanceCommitIndex(int newCommitIndex, int currentTerm) {

    }

    @Override
    public void close() {

    }
}
