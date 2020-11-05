package com.tang.platform.config.server.service;

import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Configuration;
import com.tang.platform.config.server.meta.model.ConfigurationHistory;

public interface ConfigurationService {
    PageInfo<Configuration> findConfigByGroup(int groupId);

    PageInfo<ConfigurationHistory> findConfigHistoryByConfigId(int configId);

    int createConfig(Configuration configuration);

    int updateConfig(Configuration configuration);

    int deleteConfig(Configuration configuration);

    int recoverConfig(int historyId);

    Configuration findConfigByApp(int groupId, String appName);
}
