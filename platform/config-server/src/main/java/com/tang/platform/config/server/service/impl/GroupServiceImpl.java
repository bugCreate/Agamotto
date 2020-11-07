package com.tang.platform.config.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tang.platform.config.server.meta.model.Group;
import com.tang.platform.config.server.meta.repo.GroupDao;
import com.tang.platform.config.server.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
  private final GroupDao groupDao;

  @Override
  public PageInfo<Group> findAll(int pageNum, int pageSize, String orderBy) {
    return PageHelper.startPage(pageNum, pageSize, orderBy).doSelectPageInfo(() -> groupDao.findAll());
  }

  @Override
  public Group findById(int id) {
    return groupDao.findGroupById(id);
  }

  @Override
  public int create(@NonNull Group group) {
    groupDao.create(group);
    return group.getId();
  }

  @Override
  public int update(@NonNull Group group) {
    groupDao.update(group);
    return group.getId();
  }

  @Override
  public int delete(@NonNull int groupId) {
    groupDao.delete(groupId);
    return groupId;
  }


}
