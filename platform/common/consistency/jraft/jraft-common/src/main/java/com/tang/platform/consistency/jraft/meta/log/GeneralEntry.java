package com.tang.platform.consistency.jraft.meta.log;

import com.tang.platform.consistency.jraft.meta.log.AbstractEntry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;

public class GeneralEntry extends AbstractEntry {
    private final byte[] commondBytes;
    public GeneralEntry( int index, int term,byte[] commondBytes) {
        super(KIND_GENERAL, index, term);
        this.commondBytes=commondBytes;
    }

    @Override
    public byte[] getCommandBytes() {
        return commondBytes;
    }

    @Override
    public EntryMeta getMeta() {
        return new EntryMeta(KIND_GENERAL,getIndex(),getTerm());
    }
}
