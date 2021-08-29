package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;

public abstract class AbstractEntry implements Entry {
    private final int kind;
    private final int index;
    private final int term;

    public AbstractEntry(int kind, int index, int term) {
        this.kind = kind;
        this.index = index;
        this.term = term;
    }

    @Override
    public int getKind() {
        return kind;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public int getTerm() {
        return term;
    }

    private EntryMeta getEntryMeta() {
        return new EntryMeta(kind, index, term);
    }
}
