package com.tang.platform.consistency.jraft.log.dao;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class EntryIndexFile implements Iterable<EntryIndexItem> {
    private static final long MAX_ENTRY_INDEX_OFFSET = Integer.BYTES;
    private static final int ENTRY_INDEX_ITEM_LENGTH = 16;
    private final SeekableFile seekableFile;
    private int entryIndexCount;
    private int minEntryIndex;
    private int maxEntryIndex;
    private final Map<Integer, EntryIndexItem> entryIndexItemMap = new HashMap<>();

    public int getEntryIndexCount() {
        return entryIndexCount;
    }

    public int getMinEntryIndex() {
        return minEntryIndex;
    }

    public int getMaxEntryIndex() {
        return maxEntryIndex;
    }

    public EntryIndexFile(File file) throws IOException {
        this(new RandomAccessFileAdapter(file));
    }

    public EntryIndexFile(SeekableFile seekableFile) throws IOException {
        this.seekableFile = seekableFile;
        load();
    }

    private void load() throws IOException {
        if (seekableFile.size() == 0L) {
            entryIndexCount = 0;
            return;
        }
        minEntryIndex = seekableFile.readInt();
        maxEntryIndex = seekableFile.readInt();
        updateEntryIndexCount();
        long offset;
        int kind, term;
        for (int i = maxEntryIndex; i <= maxEntryIndex; i++) {
            offset = seekableFile.readLong();
            kind = seekableFile.readInt();
            term = seekableFile.readInt();
            entryIndexItemMap.put(i, new EntryIndexItem(i, offset, kind, term));
        }
    }

    private void updateEntryIndexCount() {
        entryIndexCount = maxEntryIndex - minEntryIndex + 1;
    }

    public void appendEntryIndex(int index, long offset, int kind, int term) throws IOException {
        if (seekableFile.size() == 0L) {
            seekableFile.writeInt(index);
            minEntryIndex = index;
        } else {
            if (index != maxEntryIndex + 1) {
                throw new IllegalArgumentException("index must be" + maxEntryIndex + ",but is " + index);
            }
            seekableFile.seek(MAX_ENTRY_INDEX_OFFSET);
        }
        seekableFile.writeInt(index);
        maxEntryIndex = index;
        updateEntryIndexCount();
        seekableFile.seek(getOffsetOfEntryIndexItem(index));
        seekableFile.writeLong(offset);
        seekableFile.writeInt(kind);
        seekableFile.writeInt(term);
        entryIndexItemMap.put(index, new EntryIndexItem(index, offset, kind, term));
    }

    private long getOffsetOfEntryIndexItem(int index) {
        return (index - minEntryIndex) * ENTRY_INDEX_ITEM_LENGTH + Integer.BYTES * 2;
    }

    public void clear() throws IOException {
        seekableFile.truncate(0L);
        entryIndexCount = 0;
        entryIndexItemMap.clear();
    }

    public void removeAfter(int newMaxEntryIndex) throws IOException {
        if (isEmpty() || newMaxEntryIndex >= maxEntryIndex) {
            return;
        }
        if (newMaxEntryIndex < minEntryIndex) {
            clear();
            return;
        }
        seekableFile.seek(MAX_ENTRY_INDEX_OFFSET);
        seekableFile.writeInt(newMaxEntryIndex);
        seekableFile.truncate(getOffsetOfEntryIndexItem(newMaxEntryIndex + 1));
        for (int i = newMaxEntryIndex; i <= maxEntryIndex; i++) {
            entryIndexItemMap.remove(i);
        }
        maxEntryIndex = newMaxEntryIndex;
        updateEntryIndexCount();
    }

    public boolean isEmpty() {
        return entryIndexCount == 0;
    }

    @Override
    public Iterator<EntryIndexItem> iterator() {
        if (isEmpty()) {
            return Collections.emptyIterator();
        }

        return null;
    }

    public long getOffSet(int index) {
        return entryIndexItemMap.get(index).getOffset();
    }

    private class EntryIndexIterator implements Iterator<EntryIndexItem> {
        private final int entryIndexCount;
        private int currentEntryIndex;

        public EntryIndexIterator(int entryIndexCount, int currentEntryIndex) {
            this.entryIndexCount = entryIndexCount;
            this.currentEntryIndex = currentEntryIndex;
        }

        private void checkModification() {
            if (this.entryIndexCount != EntryIndexFile.this.entryIndexCount) {
                throw new IllegalStateException("entry index count changed.");
            }
        }

        @Override
        public boolean hasNext() {
            checkModification();
            return currentEntryIndex <= entryIndexCount;
        }

        @Override
        public EntryIndexItem next() {
            checkModification();
            return entryIndexItemMap.get(currentEntryIndex++);
        }
    }
}
