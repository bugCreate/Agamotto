package com.tang.platform.id.generator.service;

import com.tang.platform.id.generator.service.exception.ConfigMissException;
import com.tang.platform.id.generator.service.exception.IdGeneratorException;

import java.util.Properties;

public class IdServiceConfig {
    private final Properties properties;

    public IdServiceConfig(Properties properties) {
        this.properties = properties;
        valicateProperties();
    }

    public String getPopulatorSyncType() {
        return properties.getProperty("sync-type", "lock");
    }

    private void valicateProperties() {
        if (!properties.containsKey("mode")) {
            throw new IllegalStateException("mode config is required.");
        }
    }

    public long getGenMode() {
        String mode = properties.getProperty("mode");
        return Long.parseLong(mode);
    }

    public long getGenType() {
        String type = properties.getProperty("type", "1");
        return Long.parseLong(type);
    }

    public IdType getIdType() {
        long type = getGenType();
        return IdType.parse(type);
    }
    public long getMachineId(){
        return Long.parseLong(properties.getProperty("machine-id","-1"));
    }
}
