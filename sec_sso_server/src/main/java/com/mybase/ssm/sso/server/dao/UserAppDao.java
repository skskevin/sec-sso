package com.mybase.ssm.sso.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mybase.ssm.sso.dao.mybatis.Dao;
import com.mybase.ssm.sso.server.model.UserApp;

/**
 * 管理员角色映射持久化接口
 * 
 * @author Joe
 */
public interface UserAppDao extends Dao<UserApp, Integer> {

	public UserApp findByUserAppId(@Param("userId") Integer userId, @Param("appId") Integer appId);

	public int deleteByAppIds(@Param("idList") List<Integer> idList);

	public int deleteByUserIds(@Param("idList") List<Integer> idList);
}
