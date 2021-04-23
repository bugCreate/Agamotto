package com.tang.platform.id.generator.service.populator;

import com.tang.platform.id.generator.service.IdType;
import com.tang.platform.utils.time.TimeUtils;
import lombok.Data;


@Data
public class TimeGenerator {
    private boolean isMilli;
    private long lastTimestamp = -1;

    public TimeGenerator(IdType idType) {

        this.isMilli = (idType.equals(IdType.MIN_GRANULARITY));
    }

    public long genTime() {
        return isMilli ? TimeUtils.getInstance().getCurrentEpochMilliStamp() : TimeUtils.getInstance().getCurrentEpochStamp();
    }

    public void validateTimestamp(long currentTimestamp) {
        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards.  Refusing to generate id.");
        }
    }

    public boolean inSameTime(long timestamp) {
        return timestamp == lastTimestamp;
    }

    public long tillNextTime() {
        long timestamp = genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = genTime();
        }
        return timestamp;
    }
}
