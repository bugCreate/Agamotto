package com.tang.platform.config.server.service;

import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Configuration;
import com.tang.platform.config.server.meta.model.ConfigurationHistory;

public interface ConfigurationService {
  PageInfo<Configuration> findConfigByGroup(int pageNum, int pageSize, String orderBy, int groupId);

  PageInfo<ConfigurationHistory> findConfigHistoryByConfigId(int pageNum, int pageSize, String orderBy, int configId);

  void createConfig(Configuration configuration);

  void updateConfig(Configuration configuration);

  void deleteConfig(int id);

  void recoverConfig(int historyId);

  Configuration findConfigByApp(int groupId, String name, String appName);
}
