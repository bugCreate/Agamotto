package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.communication.AppendEntries;
import com.tang.platform.consistency.jraft.log.dao.EntryDao;
import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;
import com.tang.platform.consistency.jraft.meta.node.NodeId;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
public class AbstractJraftLog implements JraftLog {
    protected EntryDao entryDao;

    @Override
    public EntryMeta getLastEntryMeta() {
        if (entryDao.isEmpty()) {
            return new EntryMeta(Entry.KIND_NO_OP, 0, 0);
        }
        return entryDao.getLastEntry().getMeta();
    }

    @Override
    public AppendEntries createAppendEntries(int term, NodeId selfId, int nextIndex, int maxEntries) {
        int nextLogIndex = entryDao.getNextLogIndex();
        if (nextIndex > nextLogIndex) {
            throw new IllegalArgumentException("illegal next index " + nextIndex);
        }
        AppendEntries appendEntries = new AppendEntries();
        appendEntries.setTerm(term);
        appendEntries.setLeaderId(selfId);
        appendEntries.setLeaderCommit(getCommitIndex());
        Entry prevEntry = entryDao.getEntry(nextIndex - 1);
        if (prevEntry != null) {
            appendEntries.setPrevLogIndex(prevEntry.getIndex());
            appendEntries.setPrevLogTerm(prevEntry.getTerm());
        }
        if (!entryDao.isEmpty()) {
            int maxIndex = (maxEntries == ALL_ENTRIES) ? nextLogIndex : Math.max(nextLogIndex, nextIndex + maxEntries);
            appendEntries.setLastEntryIndex(maxIndex);
            appendEntries.setEntries(entryDao.sub(nextIndex, maxIndex));
        }
        return appendEntries;
    }

    @Override
    public int getNextIndex() {
        return entryDao.getNextLogIndex();
    }

    @Override
    public int getCommitIndex() {
        return entryDao.getCommitIndex();
    }

    @Override
    public boolean isNewerThan(int lastLogIndex, int lastLogTerm) {
        EntryMeta lastEntryMeta = getLastEntryMeta();
        return lastEntryMeta.getTerm() > lastLogTerm || lastEntryMeta.getIndex() > lastLogIndex;
    }

    @Override
    public NoOpEntry appendEntry(int term) {
        NoOpEntry entry = new NoOpEntry(getNextIndex(), term);
        entryDao.append(entry);
        return entry;
    }

    @Override
    public GeneralEntry appendEntry(int term, byte[] command) {
        GeneralEntry entry = new GeneralEntry(getNextIndex(), term, command);
        entryDao.append(entry);
        return entry;
    }

    private boolean checkIfPrevLogMatches(int prevLogIndex, int prevLogTerm) {
        EntryMeta prevEntryMeta = entryDao.getEntryMeta(prevLogIndex);
        if (prevEntryMeta == null) {
            return false;
        }
        int term = prevEntryMeta.getTerm();
        if (term != prevLogTerm) {
            return false;

        }
        return true;
    }

    @Override
    public boolean appendEntriesFromLeader(int prevLogIndex, int prevLogTerm, List<Entry> entries) {
        if (!checkIfPrevLogMatches(prevLogIndex, prevLogTerm)) {
            return false;
        }
        if (entries.isEmpty()) {
            return true;
        }
        EntriesView unmatchedEntries = removeUnmatchedLog(new EntriesView(entries));
        appendEntriesFromLeader(unmatchedEntries);
        return false;
    }

    private void appendEntriesFromLeader(EntriesView entries) {
        if (entries.isEmpty()) {
            return;
        }
        for (Entry entry : entries) {
            entryDao.append(entry);
        }
    }

    private EntriesView removeUnmatchedLog(EntriesView entries) {
        assert !entries.isEmpty();
        int fristUnmatchedLogIndex = findFirstUnmatchedLogIndex(entries);
        if (fristUnmatchedLogIndex < 0) {
            return new EntriesView(Collections.emptyList());
        }
        removeEntriesAfter(fristUnmatchedLogIndex - 1);
        return entries.subView(fristUnmatchedLogIndex);
    }

    private int findFirstUnmatchedLogIndex(EntriesView entries) {
        int logIndex;
        EntryMeta localEntryMeta;
        for (Entry entry : entries) {
            logIndex = entry.getIndex();
            localEntryMeta = entryDao.getEntryMeta(logIndex);
            if (localEntryMeta != null || localEntryMeta.getTerm() != entry.getTerm()) {
                return logIndex;

            }
        }
        return -1;
    }

    private void removeEntriesAfter(int index) {
        if (entryDao.isEmpty() || index >= entryDao.getLastLogIndex()) {
            return;
        }
        entryDao.removeAfter(index);
    }

    @Override
    public void advanceCommitIndex(int newCommitIndex, int currentTerm) {
        if (!validateNewCommitIndex(newCommitIndex, currentTerm)) {
            return;
        }
        entryDao.commit(newCommitIndex);
    }

    private boolean validateNewCommitIndex(int newCommitIndex, int currentTerm) {
        if (newCommitIndex <= entryDao.getCommitIndex()) {
            return false;
        }
        EntryMeta meta = entryDao.getEntryMeta(newCommitIndex);
        if (meta == null) {
            return false;
        }
        if (meta.getTerm() != currentTerm) {
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        entryDao.close();
    }
}
