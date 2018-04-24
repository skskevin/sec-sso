package com.mybase.ssm.sso.server.service;

import java.util.List;

import javax.naming.AuthenticationException;

import com.mybase.ssm.sso.model.Pagination;
import com.mybase.ssm.sso.model.Result;
import com.mybase.ssm.sso.server.model.User;
import com.mybase.ssm.sso.service.mybatis.Service;

/**
 * 管理员服务接口
 * 
 * @author Joe
 */
public interface UserService extends Service<User, Integer> {
	
	/**
	 * 登录
	 * 
	 * @param account
	 *            登录名
	 * @param password
	 *            密码
	 * @return 管理员ID和应用编码集合Map
	 * @throws AuthenticationException
	 *             认证异常
	 */
	public Result login(String ip, String account, String password);
	
	/**
	 * 启用禁用操作
	 * @param isEnable 是否启用
	 * @param idList 管理员ID集合
	 * @return
	 */
	public void enable(Boolean isEnable, List<Integer> idList);
	
	/**
	 * 重置密码
	 * @param password 初始化密码(已加密)
	 * @param idList 
	 */
	public void resetPassword(String password, List<Integer> idList);

	/**
	 * 根据登录名和应用ID查询分页列表
	 * @param account 登录名
	 * @param appId 应用ID
	 * @param pageNo 分页起始
	 * @param pageSize 分页记录数
	 * @return
	 */
	public Pagination<User> findPaginationByAccount(String account, Integer appId, Pagination<User> p);
	
	/**
	 * 根据登录名和应用ID查询
	 * @param account 登录名
	 * @param appId 应用ID
	 * @return
	 */
	public User findByAccount(String account);
	
	/**
	 * 更新密码
	 * 
	 * @param id
	 *            用户ID
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	public void updatePassword(Integer id, String newPassword);
	
	public void save(User user, List<Integer> appIdList);
}
