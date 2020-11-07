package com.tang.platform.config.server.controller;

import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Group;
import com.tang.platform.config.server.service.GroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "group api")
@RestController
@RequestMapping("/group")
@RequiredArgsConstructor
public class GroupController {
  private final GroupService groupService;
  @ApiOperation("get all")
  @GetMapping
  public PageInfo<Group> findAll(@RequestParam int page, @RequestParam int size,
      @RequestParam(required = false, defaultValue = "id") String orderBy,
      @RequestParam(required = false, defaultValue = "true") boolean asc) {
    orderBy += asc ? " ASC" : " DESC";
    return groupService.findAll(page, size, orderBy);
  }
  @ApiOperation("get one")
  @GetMapping("/detail")
  public Group getGroup(@RequestParam int id) {
    return groupService.findById(id);
  }
  @ApiOperation("create")
  @PostMapping
  public void create(@RequestBody Group group) {
    groupService.create(group);
  }
  @ApiOperation("update")
  @PutMapping
  public void update(@RequestBody Group group) {
    groupService.update(group);
  }
  @ApiOperation("delete")
  @DeleteMapping
  public void delete(@RequestParam int id) {
    groupService.delete(id);
  }
}
