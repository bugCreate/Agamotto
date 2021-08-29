package com.tang.platform.consistency.jraft.log.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryIndexItem {
    private int index;
    private long offset;
    private int kind;
    private int term;
}
