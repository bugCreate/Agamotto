package com.tang.platform.consistency.jraft.netty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {

    private byte type;
    private byte service;
    private int nodeId;
    private long msgId;
    private long length;
    private byte[] contents;

    public Msg(int nodeId, long msgId, long length, byte[] contents) {
        this.type = type;
        this.service = service;
        this.nodeId = nodeId;
        this.msgId = msgId;
        this.length = length;
        this.contents = contents;
    }
}
