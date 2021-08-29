package com.tang.platform.consistency.jraft.meta.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryMeta {
    private int kind;
    private int index;
    private int term;
}
