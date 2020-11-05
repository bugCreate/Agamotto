package com.tang.platform.config.server.meta.repo;

import com.tang.platform.config.server.meta.model.Configuration;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConfigurationDao {
    @Select("select id, name, app_name, comment, group_id, version, config, config_type from configuration " +
            "where id = #{id}")
    @Results(id = "config", value = {@Result(property = "appName", column = "app_name"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "configType", column = "config_type")
    })
    Configuration findConfigById(@Param("id") int id);

    @Select("select id, name, app_name, comment, group_id, version, config, config_type from configuration " +
            "where name = #{name} and app_name = #{appName} and group_id = #{groupId}")
    Configuration findConfigByCondition(@Param("name") String name, @Param("appName") String appName, @Param("groupId") int groupId);

    @Select("select id, name, app_name, comment, group_id, version, config, config_type from configuration " +
            "where group_id = #{groupId}")
    @ResultMap(value = "config")
    List<Configuration> findConfigsByGroup(@Param("groupId") int groupId);

    @Insert("insert into configuration values (#{name},#{appName},#{comment},#{groupId},#{version},#{config},#{configType})")
    void create(Configuration configuration);

    @Delete("delete from configuration where id= #{id}")
    void delete(@Param("id") int id);

    @Update("update configuration set name = #{name}, app_name = #{appName}, comment = #{comment} ," +
            "group_id = #{groupId}, version= #{version}, config = #{config}, config_type = #{configType} where id = #{id}")
    void update(Configuration configuration);
}
