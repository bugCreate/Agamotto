package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.communication.AppendEntries;
import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;
import com.tang.platform.consistency.jraft.meta.log.GeneralEntry;
import com.tang.platform.consistency.jraft.meta.log.NoOpEntry;
import com.tang.platform.consistency.jraft.meta.node.NodeId;

import java.util.List;

public interface JraftLog {
    int ALL_ENTRIES = -1;

    EntryMeta getLastEntryMeta();

    AppendEntries createAppendEntries(int term, NodeId selfId, int nextIndex, int maxEntries);

    int getNextIndex();

    int getCommitIndex();

    boolean isNewerThan(int lastLogIndex, int lastLogTerm);

    NoOpEntry appendEntry(int term);

    GeneralEntry appendEntry(int term, byte[] command);

    boolean appendEntriesFromLeader(int prevLogIndex, int prevLogTerm, List<Entry> entries);

    void advanceCommitIndex(int newCommitIndex, int currentTerm);

    void close();
}
