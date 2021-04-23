package com.tang.platform.id.generator.service.converter;

import com.tang.platform.id.generator.service.Id;
import com.tang.platform.id.generator.service.IdMeta;
import com.tang.platform.id.generator.service.ServiceMeta;

public class IdConverterImpl implements IdConverter {
    @Override
    public long convert(Id id) {
        return deConvert(id, ServiceMeta.getInstance().getIdMeta());
    }

    private long deConvert(Id id, IdMeta idMeta) {
        long ret = 0;
        ret |= id.getMachineId();
        ret |= id.getSeq() << idMeta.getSeqStartPos();
        ret |= id.getTime() << idMeta.getTimeStartPos();
        ret |= id.getMode() << idMeta.getModeBitsStartPos();
        ret |= id.getType() << idMeta.getTypeBitsStartPos();
        return ret;
    }

    @Override
    public Id reConvert(long id) {
        return doReConvert(id, ServiceMeta.getInstance().getIdMeta());
    }

    private Id doReConvert(long id, IdMeta idMeta) {
        Id ret = new Id();
        ret.setMachineId(id & idMeta.getMachineBitsMask());
        ret.setSeq((id >>> idMeta.getSeqStartPos()) & idMeta.getSeqBitsMask());
        ret.setTime((id >>> idMeta.getTimeStartPos()) & idMeta.getTimeBitsMask());
        ret.setMode((id >>> idMeta.getModeBitsStartPos()) & idMeta.getModeBitsMask());
        ret.setType((id >>> idMeta.getTypeBitsStartPos()) & idMeta.getTypeBitsMask());
        return ret;
    }
}
