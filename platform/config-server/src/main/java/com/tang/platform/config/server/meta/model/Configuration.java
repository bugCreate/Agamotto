package com.tang.platform.config.server.meta.model;

import lombok.Data;

@Data
public class Configuration {
    private int id;
    private String name;
    private String appName;
    private String comment;
    private int groupId;
    private int version;
    private String config;
    private int configType;
}
