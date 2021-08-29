package com.tang.platform.consistency.jraft.log.dao;

import com.google.common.io.Files;
import com.tang.platform.consistency.jraft.exception.JraftLogException;

import java.io.File;
import java.io.IOException;

public abstract class AbstractLogDir implements LogDir {
    final File dir;

    public AbstractLogDir(File dir) {
        this.dir = dir;
    }

    @Override
    public void init() {
        if (!dir.exists() && !dir.mkdir()){
            throw new JraftLogException("failed to create directory "+dir);
        }
        try {
            Files.touch(getEntriesFile());
            Files.touch(getEntryIndexFile());
        } catch (IOException e) {
            throw new JraftLogException("faield to create file",e);
        }

    }

    @Override
    public boolean exists() {
        return dir.exists();
    }

    @Override
    public File getEntriesFile() {
        return new File(dir,LogFileConstant.ENTRIES_FILE_NAME);
    }

    @Override
    public File getEntryIndexFile() {
        return new File(dir,LogFileConstant.ENTRY_INDEX_FILE_NAME);
    }

    @Override
    public File get() {
        return dir;
    }

    @Override
    public boolean renameTo(LogDir logDir) {
        return dir.renameTo(logDir.get());
    }
}
