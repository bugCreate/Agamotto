package com.tang.platform.config.server.meta.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {
  private int id;
  private String name;
  private String appName;
  private String comment;
  private int groupId;
  private int version;
  private String config;
  private int configType;

  public Configuration(ConfigurationHistory history) {
    this.name = history.getName();
    this.appName = history.getAppName();
    this.comment = history.getComment();
    this.groupId = history.getGroupId();
    this.config = history.getConfig();
    this.configType = history.getConfigType();
  }
}
