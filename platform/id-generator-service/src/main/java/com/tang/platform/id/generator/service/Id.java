package com.tang.platform.id.generator.service;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Id {
    private long type;
    private long mode;
    private long time;
    private long seq;
    private long machineId;
}
