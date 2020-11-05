package com.tang.platform.config.server.meta.model;

import lombok.Data;

@Data
public class Group {
    private int id;
    private String name;
    private String comment;
    private int groupType;
}
