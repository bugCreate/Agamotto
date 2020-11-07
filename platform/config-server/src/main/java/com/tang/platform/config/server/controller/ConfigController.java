package com.tang.platform.config.server.controller;

import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Configuration;
import com.tang.platform.config.server.meta.model.ConfigurationHistory;
import com.tang.platform.config.server.service.ConfigurationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "配置管理api")
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {
  private final ConfigurationService configurationService;

  @GetMapping
  @ApiOperation("分页查询配置")
  public PageInfo<Configuration> findAll(@RequestParam int page, @RequestParam int size,
      @RequestParam(required = false, defaultValue = "id") String orderBy,
      @RequestParam(required = false, defaultValue = "true") boolean asc, @RequestParam int groupId) {
    orderBy += asc ? " ASC" : " DESC";
    return configurationService.findConfigByGroup(page, size, orderBy, groupId);
  }

  @PostMapping
  @ApiOperation("创建配置")
  public void createConfig(@RequestBody Configuration configuration) {
    configurationService.createConfig(configuration);
  }

  @PutMapping
  @ApiOperation("更新配置")
  public void updateConfig(@RequestBody Configuration configuration) {
    configurationService.updateConfig(configuration);
  }

  @DeleteMapping
  @Operation(summary = "删除配置")
  public void deleteConfig(@RequestParam int id) {
    configurationService.deleteConfig(id);
  }

  @GetMapping("/history")
  @ApiOperation("分页查询历史配置")
  public PageInfo<ConfigurationHistory> findAllHistory(@RequestParam int page, @RequestParam int size,
      @RequestParam(required = false, defaultValue = "id") String orderBy,
      @RequestParam(required = false, defaultValue = "true") boolean asc, @RequestParam int configId) {
    orderBy += asc ? " ASC" : " DESC";
    return configurationService.findConfigHistoryByConfigId(page, size, orderBy, configId);
  }

  @GetMapping("/detail")
  @ApiOperation("查询配置具体信息")
  public Configuration getConfig(@RequestParam String name, @RequestParam String appName, @RequestParam int groupId) {
    return configurationService.findConfigByApp(groupId, name, appName);
  }

  @PutMapping("/history/revocer")
  @ApiOperation("分页查询配置")
  public void recoverConfig(@RequestParam int historyId) {
    configurationService.recoverConfig(historyId);
  }
}
