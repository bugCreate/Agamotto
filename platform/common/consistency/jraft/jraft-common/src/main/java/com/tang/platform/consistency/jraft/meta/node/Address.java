package com.tang.platform.consistency.jraft.meta.node;

import com.google.common.base.Preconditions;
import lombok.Data;

@Data
public class Address {
    private final String host;
    private final int port;

    public Address(String host, int port) {
        Preconditions.checkNotNull(host);
        this.host = host;
        this.port = port;
    }
}
