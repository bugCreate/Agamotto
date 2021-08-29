package com.tang.platform.consistency.jraft.log.dao;

import java.io.File;

public class RootDir {
    private final File baseDir;

    public RootDir(File baseDir) {
        if (!baseDir.exists()) {
            throw new IllegalArgumentException("dir " + baseDir + "not existed.");
        }
        this.baseDir = baseDir;
    }

    public LogDir getGeneratingLogDir() {
        return getOrCreateNormallyLogDir(LogFileConstant.GENERATING_DIR_NAME);
    }

    private NormalLogDir getOrCreateNormallyLogDir(String name) {
        NormalLogDir logDir = new NormalLogDir(new File(baseDir, name));
        if (!logDir.exists()) {
            logDir.init();
        }
        return logDir;
    }

    public LogDir rename(LogDir dir, int lastIncluedIndex) {
        LogGeneration destDir = new LogGeneration(baseDir, lastIncluedIndex);
        if (destDir.exists()) {
            throw new IllegalStateException("failed to rename " + dir + " to " + destDir);
        }
        return destDir;
    }

    public LogGeneration createFirstGeneration() {
        LogGeneration generation = new LogGeneration(baseDir, 0);
        generation.init();
        return generation;
    }

    public LogGeneration getLatestGeneration() {
        File[] files = baseDir.listFiles();
        if (files == null) {
            return null;
        }
        String fileName;
        LogGeneration latest = null;
        LogGeneration generation;
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            fileName = file.getName();
            if (!LogGeneration.isValidDirName(fileName)) {
                continue;
            }
            generation = new LogGeneration(file);
            if (latest == null || generation.compareTo(latest) > 0) {
                latest = generation;

            }

        }
        return latest;
    }
}
