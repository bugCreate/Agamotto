package com.tang.platform.config.server.meta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationHistory {
  private int id;
  private int configId;
  private String name;
  private String appName;
  private String comment;
  private int groupId;
  private int version;
  private String config;
  private int configType;
  private int mender;

  public ConfigurationHistory(Configuration configuration) {
    this.configId = configuration.getId();
    this.name = configuration.getName();
    this.appName = configuration.getAppName();
    this.comment = configuration.getComment();
    this.groupId = configuration.getGroupId();
    this.version = configuration.getVersion();
    this.config = configuration.getConfig();
    this.configType = configuration.getConfigType();
  }
}
