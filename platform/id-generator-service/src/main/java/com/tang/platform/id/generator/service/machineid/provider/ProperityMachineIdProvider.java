package com.tang.platform.id.generator.service.machineid.provider;

import com.tang.platform.id.generator.service.MachineIdProvider;

public class ProperityMachineIdProvider implements MachineIdProvider {
    private long machineId;
    @Override
    public long getMachineId() {
        return machineId;
    }

    public void setMachineId(long machineId) {
        this.machineId = machineId;
    }
}
