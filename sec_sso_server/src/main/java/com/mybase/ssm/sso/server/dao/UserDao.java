package com.mybase.ssm.sso.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.mybase.ssm.sso.dao.mybatis.Dao;
import com.mybase.ssm.sso.model.Pagination;
import com.mybase.ssm.sso.server.model.User;

/**
 * 管理员持久化接口
 * 
 * @author Joe
 */
public interface UserDao extends Dao<User, Integer> {
	
	public int enable(@Param("isEnable") Boolean isEnable, @Param("idList") List<Integer> idList);
	
	public int resetPassword(@Param("password") String password, @Param("idList") List<Integer> idList);

	public List<User> findPaginationByAccount(@Param("account") String account, @Param("appId") Integer appId, Pagination<User> p);
	
	public User findByAccount(@Param("account") String account);
}
