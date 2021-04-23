package com.tang.platform.id.generator.service;

import java.util.Date;

public interface IdService {
    long genId();

    Id expId(long id);

    long makeId(long time, long seq);

    long makeId(long time, long seq, long machineId);

    long makeId(long time, long seq, long machineId, int type);

    long makeId(long time, long seq, long machineId, int type, int mode);

    Date transTime(long time);
}
