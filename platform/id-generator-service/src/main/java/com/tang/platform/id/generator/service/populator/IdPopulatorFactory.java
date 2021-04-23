package com.tang.platform.id.generator.service.populator;

import com.tang.platform.id.generator.service.IdPopulator;
import com.tang.platform.id.generator.service.IdServiceConfig;
import com.tang.platform.id.generator.service.ServiceMeta;
import com.tang.platform.id.generator.service.exception.IdGeneratorException;

public class IdPopulatorFactory {
    public static IdPopulator getIdPopulator(IdServiceConfig config) {
        String syncType = config.getPopulatorSyncType();
        switch (syncType){
            case "cas":
                return null;
            case "lock":
                return new LockIdPopulator(ServiceMeta.getInstance().getIdMeta(),ServiceMeta.getInstance().getIdServiceConfig().getIdType());
            default:
                throw new IllegalStateException("populator sync type not supported.");
        }
    }
}
