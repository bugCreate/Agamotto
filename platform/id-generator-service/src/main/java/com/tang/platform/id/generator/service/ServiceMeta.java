package com.tang.platform.id.generator.service;

import com.tang.platform.id.generator.service.exception.IdGeneratorException;
import lombok.Getter;

@Getter
public class ServiceMeta {
    private IdServiceConfig idServiceConfig;
    private IdMeta idMeta;
    private static ServiceMeta instance = new ServiceMeta();

    public static ServiceMeta getInstance() {
        return instance;
    }

    public void init(IdServiceConfig idServiceConfig) {
        this.idServiceConfig = idServiceConfig;
        initIdMeta();
    }

    private void initIdMeta() {
        IdType idType = idServiceConfig.getIdType();
        if (idType.equals(IdType.MIN_GRANULARITY)) {
            this.idMeta = new IdMeta((byte) 10, (byte) 10, (byte) 41, (byte) 1, (byte) 1);
        } else {
            this.idMeta = new IdMeta((byte) 10, (byte) 20, (byte) 31, (byte) 1, (byte) 1);
        }
    }

    private void validate() {
        if (null == idServiceConfig) {
            throw new IllegalStateException("service not init.");
        }
    }

}
