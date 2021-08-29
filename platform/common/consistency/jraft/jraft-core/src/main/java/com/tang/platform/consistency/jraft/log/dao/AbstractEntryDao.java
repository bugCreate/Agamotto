package com.tang.platform.consistency.jraft.log.dao;

import com.tang.platform.consistency.jraft.exception.EmptyDataException;
import com.tang.platform.consistency.jraft.exception.OutOfIndexException;
import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;

import java.util.Collections;
import java.util.List;

public abstract class AbstractEntryDao implements EntryDao {
    int logIndexOffset;
    int nextLogIndex;

    public AbstractEntryDao(int logIndexOffset) {
        this.logIndexOffset = logIndexOffset;
        this.nextLogIndex = logIndexOffset;
    }

    @Override
    public boolean isEmpty() {
        return logIndexOffset == nextLogIndex;
    }

    @Override
    public int getFristLogIndex() {
        if (isEmpty()) {
            throw new EmptyDataException("entry not exist.");
        }
        return doGetFristLogIndex();
    }

    int doGetFristLogIndex() {
        return logIndexOffset;
    }

    @Override
    public int getLastLogIndex() {
        if (isEmpty()) {
            throw new EmptyDataException("entry not exist");
        }
        return doGetLastLogIndex();
    }

    int doGetLastLogIndex() {
        return nextLogIndex - 1;
    }

    @Override
    public int getNextLogIndex() {
        return nextLogIndex;
    }

    @Override
    public boolean isEntryPresent(int index) {
        return !isEmpty() && index >= doGetFristLogIndex() && index <= doGetLastLogIndex();
    }

    @Override
    public EntryMeta getEntryMeta(int index) {
        if (!isEntryPresent(index)) {
            throw new OutOfIndexException("index [" + index + "] out of bound");
        }
        Entry entry = getEntry(index);
        return entry.getMeta();
    }

    @Override
    public Entry getEntry(int index) {
        if (!isEntryPresent(index)) {
            throw new OutOfIndexException("index [" + index + "] out of bound");
        }
        return doGetEntry(index);
    }

    @Override
    public Entry getLastEntry() {
        return isEmpty() ? null : doGetEntry(doGetLastLogIndex());
    }

    protected abstract Entry doGetEntry(int index);

    @Override
    public List<Entry> sub(int fromIndex) {
        if (isEmpty() || fromIndex > doGetLastLogIndex()) {
            return Collections.emptyList();
        }
        return sub(Math.max(fromIndex, doGetFristLogIndex()), doGetLastLogIndex());
    }

    @Override
    public List<Entry> sub(int fromIndex, int toIndex) {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        if (fromIndex < doGetLastLogIndex() || toIndex > doGetLastLogIndex() + 1 || fromIndex > toIndex) {
            throw new IllegalArgumentException("illegal fromIndex or toIndex,[fromIndex=" + fromIndex + ",toIndex=" + toIndex + "]");
        }
        return doSub(fromIndex, toIndex);
    }

    protected abstract List<Entry> doSub(int fromIndex, int toIndex);

    @Override
    public void append(Entry entry) {
        if (entry.getIndex() != nextLogIndex) {
            throw new IllegalArgumentException("the entry to append index not equal current next log index.[entry index=" + entry.getIndex() + ",nextLogIndex=" + nextLogIndex + "]");
        }
        doAppend(entry);
        nextLogIndex++;
    }

    protected abstract void doAppend(Entry entry);

    @Override
    public void append(List<Entry> entries) {
        entries.forEach(e -> append(e));
    }
    
    @Override
    public void removeAfter(int index) {
        if (isEmpty() || index>=doGetLastLogIndex()){
            return;
        }
        doRemoveAfter(index);
    }

    protected abstract void doRemoveAfter(int index);
}
