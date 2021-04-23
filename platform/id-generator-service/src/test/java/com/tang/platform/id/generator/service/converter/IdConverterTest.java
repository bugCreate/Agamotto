package com.tang.platform.id.generator.service.converter;

import com.tang.platform.id.generator.service.Id;
import com.tang.platform.id.generator.service.IdServiceConfig;
import com.tang.platform.id.generator.service.ServiceMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class IdConverterTest {
    @BeforeAll
    public static void before(){
        Properties properties = new Properties();
        properties.put("type","0");
        properties.put("mode","1");
        IdServiceConfig serviceConfig = new IdServiceConfig(properties);
        ServiceMeta.getInstance().init(serviceConfig);
    }
    @Test
    public void convertAndReconvert(){
        Id id = new Id();
        id.setType(0);
        id.setMode(1);
        id.setTime(Instant.now().getEpochSecond());
        id.setSeq(10);
        id.setMachineId(20);
        IdConverter idConverter = new IdConverterImpl();
        long idNum = idConverter.convert(id);
        Id id2= idConverter.reConvert(idNum);
        assertEquals(id, id2);
    }
}
