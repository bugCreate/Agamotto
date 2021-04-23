package com.tang.platform.id.generator.service.populator;

import com.tang.platform.id.generator.service.Id;
import com.tang.platform.id.generator.service.IdServiceConfig;
import com.tang.platform.id.generator.service.ServiceMeta;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LockIdPopulatorTest {
    @BeforeAll
    public static void before(){
        Properties properties = new Properties();
        properties.put("type","1");
        properties.put("mode","1");
        IdServiceConfig serviceConfig = new IdServiceConfig(properties);
        ServiceMeta.getInstance().init(serviceConfig);
    }
    @Test
    public void populatIdTest(){
        LockIdPopulator lockIdPopulator = new LockIdPopulator(ServiceMeta.getInstance().getIdMeta(),ServiceMeta.getInstance().getIdServiceConfig().getIdType());
        Id id = new Id();
        lockIdPopulator.populateId(id);
        long timestamp =id.getTime();
        long seq =id.getSeq();
        int sum=0;
        int timesum=0;
        Instant instant =Instant.now();
        while (true){
            Id idtmp = new Id();
            lockIdPopulator.populateId(idtmp);

            if (timestamp!=idtmp.getTime()){
                // 为了防止测试时不是整毫秒开始，多次测试
                timesum++;
                if(timesum>10){
                    break;
                }else {
                    timestamp=idtmp.getTime();
                }
                // 确保单位时间单位内不会超发。
                assertTrue(sum<=1024);

            }

            seq=idtmp.getSeq();
            if (seq==0){
                // seq ==0 说明新的周期，重新计数
                sum=1;
            }else {
                sum++;
            }
        }
    }
}
