package com.tang.platform.id.generator.service.populator;

import com.tang.platform.id.generator.service.Id;
import com.tang.platform.id.generator.service.IdMeta;
import com.tang.platform.id.generator.service.IdPopulator;
import com.tang.platform.id.generator.service.IdType;

public abstract class AbstractIdPopulator implements IdPopulator {
    private IdMeta idMeta;
    private TimeGenerator timeGenerator;
    private long seq = 0;

    public AbstractIdPopulator(IdMeta idMeta, IdType idType) {
        this.idMeta = idMeta;
        this.timeGenerator=new TimeGenerator(idType);
    }


    protected void genTimeAndSeq(Id id) {
        long timestamp = timeGenerator.genTime();
        timeGenerator.validateTimestamp(timestamp);
        if (timeGenerator.inSameTime(timestamp)) {
            seq++;
            seq &= idMeta.getSeqBitsMask();
            // 超出seq范围
            if (seq == 0) {
                timestamp = timeGenerator.tillNextTime();
                timeGenerator.setLastTimestamp(timestamp);
            }
        } else {
            timeGenerator.setLastTimestamp(timestamp);
            seq = 0;
        }
        id.setSeq(seq);
        id.setTime(timestamp);
    }
}
