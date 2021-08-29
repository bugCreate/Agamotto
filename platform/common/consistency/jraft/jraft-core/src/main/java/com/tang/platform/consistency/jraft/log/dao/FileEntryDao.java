package com.tang.platform.consistency.jraft.log.dao;

import com.tang.platform.consistency.jraft.exception.JraftLogException;
import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FileEntryDao extends AbstractEntryDao {
    private final EntriesFile entriesFile;
    private final EntryIndexFile entryIndexFile;
    private final LinkedList<Entry> pendingEntries = new LinkedList<>();
    private int commitIndex = 0;

    public FileEntryDao(LogDir logDir, int logIndexOffset) {
        super(logIndexOffset);
        try {
            this.entriesFile = new EntriesFile(logDir.getEntriesFile());
            this.entryIndexFile = new EntryIndexFile(logDir.getEntryIndexFile());
            init();
        } catch (Exception e) {
            throw new JraftLogException("failed to open log file.", e);
        }
    }

    public FileEntryDao(int logIndexOffset, EntriesFile entriesFile, EntryIndexFile entryIndexFile) {
        super(logIndexOffset);
        this.entriesFile = entriesFile;
        this.entryIndexFile = entryIndexFile;
        init();
    }

    private void init() {
        if (entryIndexFile.isEmpty()) {
            return;
        }
        logIndexOffset = entryIndexFile.getMinEntryIndex();
        nextLogIndex = entryIndexFile.getMaxEntryIndex() + 1;
    }

    @Override
    protected Entry doGetEntry(int index) {
        if (!pendingEntries.isEmpty()) {
            int firstPendingEntryIndex = pendingEntries.getFirst().getIndex();
            if (index >= firstPendingEntryIndex) {
                return pendingEntries.get(index - firstPendingEntryIndex);
            }
        }
        return getEntriesInFile(index);
    }

    @Override
    protected List<Entry> doSub(int fromIndex, int toIndex) {
        List<Entry> result = new ArrayList<>();
        if (!entryIndexFile.isEmpty() && fromIndex <= entryIndexFile.getMaxEntryIndex()) {
            int maxIndex = Math.max(entryIndexFile.getMaxEntryIndex(), toIndex);
            for (int i = fromIndex; i < maxIndex; i++) {
                result.add(getEntriesInFile(i));
            }
        }
        if (!pendingEntries.isEmpty() && toIndex > pendingEntries.getFirst().getIndex()) {
            Iterator<Entry> iterator = pendingEntries.iterator();
            Entry entry;
            int index;
            while (iterator.hasNext()) {
                entry = iterator.next();
                index = entry.getIndex();
                if (index >= toIndex) {
                    break;
                }
                if (index >= fromIndex) {
                    result.add(entry);
                }
            }
        }
        return result;
    }

    private Entry getEntriesInFile(int index) {
        long offset = entryIndexFile.getOffSet(index);
        try {
            return entriesFile.loadEntry(offset);
        } catch (Exception e) {
            throw new JraftLogException("faile to load entry " + index, e);
        }
    }

    @Override
    protected void doAppend(Entry entry) {
        pendingEntries.add(entry);
    }

    @Override
    protected void doRemoveAfter(int index) {
        if (!pendingEntries.isEmpty() && index >= pendingEntries.getFirst().getIndex() - 1) {
            int lastIndex = pendingEntries.getLast().getIndex();
            for (int i = index + 1; i < lastIndex; i++) {
                pendingEntries.removeLast();
            }
            nextLogIndex = index + 1;
            return;
        }
        try {
            pendingEntries.clear();
            if (index >= doGetFristLogIndex()) {

                entriesFile.truncate(entryIndexFile.getOffSet(index + 1));
                entryIndexFile.removeAfter(index);
                nextLogIndex = index + 1;
                commitIndex = index;
            } else {
                entriesFile.clear();
                entryIndexFile.clear();
                nextLogIndex = logIndexOffset;
                commitIndex = logIndexOffset - 1;

            }
        } catch (Exception e) {
            throw new JraftLogException("failed to remove entries.", e);
        }
    }


    @Override
    public void commit(int index) {
        if (index < commitIndex) {
            throw new IllegalArgumentException("commit index <" + commitIndex);
        }
        if (index == commitIndex) {
            return;
        }
        // 已经写入文件，只更新commitIndex
        if (!entryIndexFile.isEmpty() && index <= entryIndexFile.getMaxEntryIndex()) {
            commitIndex = index;
            return;
        }
        if (pendingEntries.isEmpty() || pendingEntries.getFirst().getIndex() > index || pendingEntries.getLast().getIndex() < index) {
            throw new IllegalArgumentException("no entry to commit.");
        }
        long offset;
        Entry entry = null;
        try {
            for (int i = pendingEntries.getFirst().getIndex(); i <= index; i++) {
                entry = pendingEntries.removeFirst();
                offset = entriesFile.appendEntry(entry);
                entryIndexFile.appendEntryIndex(i, offset, entry.getKind(), entry.getTerm());
                commitIndex = i;
            }
        } catch (Exception e) {
            throw new JraftLogException("failed to commit entry " + entry, e);
        }
    }

    @Override
    public int getCommitIndex() {
        return commitIndex;
    }

    @Override
    public void close() {

    }
}
