package com.tang.platform.consistency.jraft.log.dao;

import com.tang.platform.consistency.jraft.meta.log.Entry;

import java.util.ArrayList;
import java.util.List;

public class MemoryEntryDao extends AbstractEntryDao {
    private final List<Entry> entries = new ArrayList<>();
    private final int commitIndex=0;
    public MemoryEntryDao() {
        super(1);
    }

    public MemoryEntryDao(int logIndexOffset) {
        super(logIndexOffset);
    }

    @Override
    protected Entry doGetEntry(int index) {
        return entries.get(index-logIndexOffset);
    }

    @Override
    protected List<Entry> doSub(int fromIndex, int toIndex) {
        return entries.subList(fromIndex-logIndexOffset,toIndex-logIndexOffset);
    }

    @Override
    protected void doAppend(Entry entry) {
        entries.add(entry);
    }

    @Override
    protected void doRemoveAfter(int index) {
        if (index<doGetFristLogIndex()){
            entries.clear();
            nextLogIndex=logIndexOffset;
        }else {
            entries.subList(index-logIndexOffset+1,entries.size()).clear();
            nextLogIndex=index+1;
        }

    }

    @Override
    public void commit(int index) {

    }

    @Override
    public int getCommitIndex() {
        return 0;
    }

    @Override
    public void close() {

    }
}
