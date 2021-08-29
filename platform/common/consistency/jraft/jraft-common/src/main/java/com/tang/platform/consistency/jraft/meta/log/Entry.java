package com.tang.platform.consistency.jraft.meta.log;

public interface Entry {
    int KIND_NO_OP =0;
    int KIND_GENERAL =1;
    int getKind();
    int getIndex();
    int getTerm();
    byte[] getCommandBytes();
    EntryMeta getMeta();
}
