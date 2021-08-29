package com.tang.platform.consistency.jraft.log.dao;

import java.io.File;

public class NormalLogDir extends AbstractLogDir {
    public NormalLogDir(File dir) {
        super(dir);
    }
}
