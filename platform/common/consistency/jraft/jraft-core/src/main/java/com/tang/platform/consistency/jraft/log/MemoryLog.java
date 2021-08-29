package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.log.dao.EntryDao;

public class MemoryLog extends AbstractJraftLog {
    public MemoryLog() {
    }

    public MemoryLog(EntryDao entryDao) {
        this.entryDao = entryDao;
    }
}
