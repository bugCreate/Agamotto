package com.tang.platform.config.server.service;

import com.tang.platform.config.server.meta.model.Group;

import java.util.List;

public interface GroupService {
    List<Group> findAll();

    int create(Group group);

    int update(Group group);

    int delete(int groupId);
}
