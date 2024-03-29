package com.tang.platform.consistency.jraft.meta.log;

import com.tang.platform.consistency.jraft.meta.log.Entry;
import com.tang.platform.consistency.jraft.meta.log.GeneralEntry;
import com.tang.platform.consistency.jraft.meta.log.NoOpEntry;

public class EntryFactory {
    public static Entry create(int kind, int index, int term, byte[] payload) {
        switch (kind) {
            case Entry.KIND_NO_OP:
                return new NoOpEntry(index, term);
            case Entry.KIND_GENERAL:
                return new GeneralEntry(index, term, payload);
            default:
                throw new IllegalArgumentException("unexpected entry kind " + kind);
        }
    }
}
