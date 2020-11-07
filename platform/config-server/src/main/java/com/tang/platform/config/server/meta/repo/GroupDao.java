package com.tang.platform.config.server.meta.repo;


import com.tang.platform.config.server.meta.model.Group;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GroupDao {
    @Select("select id,name,comment,group_type from config_group where id =#{id}")
    @Results(id = "group", value = {
            @Result(property = "groupType", column = "group_type")
    })
    Group findGroupById(@Param("id") int id);

    @Select("select id,name,comment,group_type from config_group where name =#{name}")
    @ResultMap(value = "group")
    Group findGroupByName(@Param("name") String name);

    @Insert("insert into config_group(name,comment,group_type) values(#{name},#{comment},#{groupType})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void create(Group group);

    @Update("update config_group set name=#{name},comment=#{comment},group_type =#{groupType}")
    void update(Group group);

    @Delete("delete from config_group where id=#{id}")
    void delete(@Param("id") int id);

    @Select("select id,name,comment,group_type from config_group")
    @ResultMap(value = "group")
    List<Group> findAll();
}
