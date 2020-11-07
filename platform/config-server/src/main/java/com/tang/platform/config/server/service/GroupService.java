package com.tang.platform.config.server.service;

import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Group;

public interface GroupService {
  PageInfo<Group> findAll(int pageNum, int pageSize, String orderBy);

  Group findById(int id);

  int create(Group group);

  int update(Group group);

  int delete(int groupId);
}
