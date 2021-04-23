package com.tang.platform.id.generator.service;

import com.tang.platform.id.generator.service.machineid.provider.ProperityMachineIdProvider;

public class MachineIdProviderFactory {
    public static MachineIdProvider getMachineIdProvider(IdServiceConfig idServiceConfig) {
        long machineId = idServiceConfig.getMachineId();
        if (machineId == -1) {
            // todo 与注册中心结合
            throw new IllegalStateException("machine id provider not supported.");
        } else {
            ProperityMachineIdProvider properityMachineIdProvider = new ProperityMachineIdProvider();
            properityMachineIdProvider.setMachineId(machineId);
            return properityMachineIdProvider;
        }
    }
}
