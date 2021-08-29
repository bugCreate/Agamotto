package com.tang.platform.consistency.jraft.log.dao;

import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;

import java.util.List;

public interface EntryDao {
    boolean isEmpty();

    int getFristLogIndex();

    int getLastLogIndex();

    int getNextLogIndex();

    List<Entry> sub(int fromIndex);

    List<Entry> sub(int fromIndex, int toIndex);

    boolean isEntryPresent(int index);

    EntryMeta getEntryMeta(int index);

    Entry getEntry(int index);

    Entry getLastEntry();

    void append(Entry entry);

    void append(List<Entry> entries);

    void commit(int index);

    int getCommitIndex();

    void removeAfter(int index);

    void close();
}
