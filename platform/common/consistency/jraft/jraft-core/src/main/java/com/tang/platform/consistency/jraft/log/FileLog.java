package com.tang.platform.consistency.jraft.log;

import com.tang.platform.consistency.jraft.log.dao.FileEntryDao;
import com.tang.platform.consistency.jraft.log.dao.LogGeneration;
import com.tang.platform.consistency.jraft.log.dao.RootDir;

import java.io.File;

public class FileLog extends AbstractJraftLog {
    private final RootDir rootDir;

    public FileLog(File baseDir) {
        rootDir = new RootDir(baseDir);
        LogGeneration latestGeneration = rootDir.getLatestGeneration();
        if (null != latestGeneration) {
            entryDao = new FileEntryDao(latestGeneration, latestGeneration.getLastIncludedIndex());
        } else {
            LogGeneration logGeneration = rootDir.createFirstGeneration();
            entryDao = new FileEntryDao(logGeneration, 1);
        }
    }
}
