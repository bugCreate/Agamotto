package com.tang.platform.id.generator.service;

import com.tang.platform.id.generator.service.converter.IdConverter;
import com.tang.platform.id.generator.service.converter.IdConverterImpl;
import com.tang.platform.id.generator.service.populator.IdPopulatorFactory;

import java.util.Date;

public class IdServiceImpl implements IdService {
    private final ServiceMeta serviceMeta;
    private final IdPopulator idPopulator;
    private final IdConverter idConverter;
    private final MachineIdProvider machineIdProvider;

    public IdServiceImpl(ServiceMeta serviceMeta) {
        this.serviceMeta = serviceMeta;
        this.idPopulator = IdPopulatorFactory.getIdPopulator(serviceMeta.getIdServiceConfig());
        this.idConverter = new IdConverterImpl();
        this.machineIdProvider = MachineIdProviderFactory.getMachineIdProvider(serviceMeta.getIdServiceConfig());
    }

    @Override
    public long genId() {
        Id id = buildDefaultId();
        idPopulator.populateId(id);

        return idConverter.convert(id);
    }

    private Id buildDefaultId() {
        Id id = new Id();
        id.setType(serviceMeta.getIdServiceConfig().getGenType());
        id.setMode(serviceMeta.getIdServiceConfig().getGenMode());
        id.setMachineId(machineIdProvider.getMachineId());
        return id;
    }

    @Override
    public Id expId(long id) {
        return idConverter.reConvert(id);
    }

    @Override
    public long makeId(long time, long seq) {
        Id id = buildDefaultId();
        id.setTime(time);
        id.setSeq(seq);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long time, long seq, long machineId) {
        Id id = buildDefaultId();
        id.setTime(time);
        id.setSeq(seq);
        id.setMachineId(machineId);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long time, long seq, long machineId, int type) {
        Id id = buildDefaultId();
        id.setTime(time);
        id.setSeq(seq);
        id.setMachineId(machineId);
        id.setType(type);
        return idConverter.convert(id);
    }

    @Override
    public long makeId(long time, long seq, long machineId, int type, int mode) {
        Id id = buildDefaultId();
        id.setTime(time);
        id.setSeq(seq);
        id.setMachineId(machineId);
        id.setType(type);
        id.setMode(mode);
        return idConverter.convert(id);
    }

    @Override
    public Date transTime(long time) {
        return null;
    }
}
