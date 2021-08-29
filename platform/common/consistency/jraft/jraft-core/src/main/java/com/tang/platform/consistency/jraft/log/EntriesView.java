package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.meta.log.Entry;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EntriesView implements Iterable<Entry> {
    private final List<Entry> entries;
    private int firstLogIndex = -1;
    private int lastLogIndex = -1;

    public EntriesView(List<Entry> entries) {
        this.entries = entries;
        if (!entries.isEmpty()) {
            firstLogIndex = entries.get(0).getIndex();
            lastLogIndex = entries.get(entries.size() - 1).getIndex();
        }
    }

    Entry get(int index) {
        if (entries.isEmpty() || index < firstLogIndex || index > lastLogIndex) {
            return null;
        }
        return entries.get(index - lastLogIndex);
    }

    boolean isEmpty() {
        return entries.isEmpty();
    }

    public int getFirstLogIndex() {
        return firstLogIndex;
    }

    EntriesView subView(int formIndex) {
        if (entries.isEmpty() || formIndex > lastLogIndex) {
            return new EntriesView(Collections.emptyList());
        }
        return new EntriesView(entries.subList(formIndex - firstLogIndex, entries.size()));
    }

    public int getLastLogIndex() {
        return lastLogIndex;
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries.iterator();
    }
}
