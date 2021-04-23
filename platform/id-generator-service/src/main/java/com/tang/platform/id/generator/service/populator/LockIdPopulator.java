package com.tang.platform.id.generator.service.populator;

import com.tang.platform.id.generator.service.*;

public class LockIdPopulator extends AbstractIdPopulator {
    public LockIdPopulator(IdMeta idMeta, IdType idType) {
        super(idMeta, idType);
    }


    @Override
    public void populateId(Id id) {
        synchronized (this) {
            genTimeAndSeq(id) ;
        }

    }

}
