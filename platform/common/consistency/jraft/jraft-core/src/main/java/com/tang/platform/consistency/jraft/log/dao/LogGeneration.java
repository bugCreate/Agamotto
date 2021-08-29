package com.tang.platform.consistency.jraft.log.dao;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogGeneration extends AbstractLogDir implements Comparable<LogGeneration> {
    private static final Pattern DIR_NAME_PATTERN = Pattern.compile("log-(\\d+)");
    private final int lastIncludedIndex;

    public LogGeneration(File baseDir, int lastIncludedIndex) {

        super(baseDir);
        this.lastIncludedIndex = lastIncludedIndex;
    }

    LogGeneration(File dir) {
        super(dir);
        Matcher matcher = DIR_NAME_PATTERN.matcher(dir.getName());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("dir name is illegal.");
        }
        lastIncludedIndex = Integer.parseInt(matcher.group(1));
    }

    static boolean isValidDirName(String name) {
        return DIR_NAME_PATTERN.matcher(name).matches();
    }

    private static String generateDirName(int lastIncludedIndex) {
        return "log-" + lastIncludedIndex;
    }

    public int getLastIncludedIndex() {
        return lastIncludedIndex;
    }

    @Override
    public int compareTo(LogGeneration o) {
        return Integer.compare(lastIncludedIndex, o.lastIncludedIndex);
    }
}
