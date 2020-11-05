package com.tang.platform.config.server.service.impl;

import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Configuration;
import com.tang.platform.config.server.meta.model.ConfigurationHistory;
import com.tang.platform.config.server.meta.repo.ConfigurationDao;
import com.tang.platform.config.server.meta.repo.ConfigurationHistoryDao;
import com.tang.platform.config.server.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationDao configurationDao;
    private final ConfigurationHistoryDao historyDao;

    @Override
    public PageInfo<Configuration> findConfigByGroup(int groupId) {
        return null;
    }

    @Override
    public PageInfo<ConfigurationHistory> findConfigHistoryByConfigId(int configId) {
        return null;
    }

    @Override
    public int createConfig(Configuration configuration) {
        return 0;
    }

    @Override
    public int updateConfig(Configuration configuration) {
        return 0;
    }

    @Override
    public int deleteConfig(Configuration configuration) {
        return 0;
    }

    @Override
    public int recoverConfig(int historyId) {
        return 0;
    }

    @Override
    public Configuration findConfigByApp(int groupId, String appName) {
        return null;
    }
}
