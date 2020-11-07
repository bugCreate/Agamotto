package com.tang.platform.config.server.service.impl;

import com.github.pagehelper.PageHelper;
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
  public PageInfo<Configuration> findConfigByGroup(int pageNum, int pageSize, String orderBy, int groupId) {
    return PageHelper.startPage(pageNum, pageSize, orderBy).doSelectPageInfo(
        () -> configurationDao.findConfigsByGroup(groupId));
  }

  @Override
  public PageInfo<ConfigurationHistory> findConfigHistoryByConfigId(int pageNum, int pageSize, String orderBy,
      int configId) {
    return PageHelper.startPage(pageNum, pageSize, orderBy).doSelectPageInfo(
        () -> historyDao.findConfigHistories(configId));
  }

  @Override
  public void createConfig(Configuration configuration) {
    configurationDao.create(configuration);
  }

  @Override
  public void updateConfig(Configuration configuration) {
    Configuration old = configurationDao.findConfigById(configuration.getId());
    configurationDao.update(configuration);
    ConfigurationHistory configurationHistory = new ConfigurationHistory(configuration);//member TODO
    historyDao.create(configurationHistory);
  }

  @Override
  public void deleteConfig(int id) {
    Configuration old = configurationDao.findConfigById(id);
    configurationDao.delete(id);
    ConfigurationHistory configurationHistory = new ConfigurationHistory(old);//member TODO
    historyDao.create(configurationHistory);
  }

  @Override
  public void recoverConfig(int historyId) {
    ConfigurationHistory history = historyDao.findConfigHistoryByid(historyId);
    Configuration configuration = new Configuration(history);
    Configuration old = configurationDao.findConfigById(history.getConfigId());
    if (null == old) {
      configurationDao.create(configuration);
    } else {
      configurationDao.update(configuration);
      historyDao.create(new ConfigurationHistory(old));
    }
  }

  @Override
  public Configuration findConfigByApp(int groupId, String name, String appName) {
    return configurationDao.findConfigByCondition(name, appName, groupId);
  }
}
