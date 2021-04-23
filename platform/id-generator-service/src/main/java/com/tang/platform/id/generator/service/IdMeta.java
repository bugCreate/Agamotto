package com.tang.platform.id.generator.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdMeta {

    private byte machineBits;

    private byte seqBits;

    private byte timeBits;

    private byte modeBits;

    private byte typeBits;


    public long getMachineBitsMask() {
        return getBitsMask(machineBits);
    }

    public long getSeqStartPos() {
        return machineBits;
    }

    public long getSeqBitsMask() {
        return getBitsMask(seqBits);
    }

    private long getBitsMask(byte bits) {
        return ~(-1L << bits);
    }

    public long getTimeBitsMask() {
        return getBitsMask(timeBits);
    }

    public long getTimeStartPos() {
        return seqBits + machineBits;
    }

    public long getModeBitsMask() {
        return getBitsMask(modeBits);
    }

    public long getModeBitsStartPos() {
        return timeBits + seqBits + machineBits;
    }

    public long getTypeBitsStartPos() {
        return modeBits + timeBits + seqBits + machineBits;
    }

    public long getTypeBitsMask() {
        return getBitsMask(typeBits);
    }
}
