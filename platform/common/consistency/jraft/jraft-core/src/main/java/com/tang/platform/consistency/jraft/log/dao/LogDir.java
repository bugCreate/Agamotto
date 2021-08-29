package com.tang.platform.consistency.jraft.log.dao;

import java.io.File;

public interface LogDir {
    void init();

    boolean exists();

    File getEntriesFile();

    File getEntryIndexFile();

    File get();

    boolean renameTo(LogDir logDir);
}
