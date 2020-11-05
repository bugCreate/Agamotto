package com.tang.platform.config.server.meta.repo;

import com.tang.platform.config.server.meta.model.ConfigurationHistory;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ConfigurationHistoryDao {
    @Select("select id, config_id, name, app_name, comment, group_id," +
            "version, config, config_type, mender from configuration_history where id = #{id}")
    @Results(id = "config", value = {
            @Result(property = "configId", column = "config_id"),
            @Result(property = "appName", column = "app_name"),
            @Result(property = "groupId", column = "group_id"),
            @Result(property = "configType", column = "config_type")})
    ConfigurationHistory findConfigHistoryByid(@Param("id") int id);

    @Select("select id, config_id, name, app_name, comment, group_id," +
            "version, config, config_type, mender from configuration_history where config_id = #{configId}")
    @ResultMap(value = "config")
    List<ConfigurationHistory> findConfigHistories(@Param("configId") int configId);

    @Insert("insert into configuration_history values (#{configId},#{name},#{appName}," +
            "#{comment},#{groupId},#{version},#{config},#{configType},#{mender})")
    void create(ConfigurationHistory history);
}
