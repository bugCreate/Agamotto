package com.tang.platform.consistency.jraft.meta.log;

import com.tang.platform.consistency.jraft.meta.log.AbstractEntry;
import com.tang.platform.consistency.jraft.meta.log.EntryMeta;

public class NoOpEntry extends AbstractEntry {
    public NoOpEntry(int index, int term) {
        super(KIND_NO_OP, index, term);
    }

    @Override
    public byte[] getCommandBytes() {
        return new byte[0];
    }

    @Override
    public EntryMeta getMeta() {
        return new EntryMeta(KIND_NO_OP,getIndex(),getTerm());
    }
}
