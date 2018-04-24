package com.mybase.ssm.sso.server.service;

import java.util.List;

import com.mybase.ssm.sso.rpc.RpcPermission;
import com.mybase.ssm.sso.server.model.Permission;
import com.mybase.ssm.sso.service.mybatis.Service;

/**
 * 权限服务接口
 * 
 * @author Joe
 */
public interface PermissionService extends Service<Permission, Integer> {

	/**
	 * 根据名称和应用ID查询
	 * @param name 权限名称
	 * @param appId 应用ID
	 * @return
	 */
	public List<Permission> findByName(String name, Integer appId, Boolean isEnable);
	
	/**
	 * 删除权限
	 * @param id 权限ID
	 * @param appId 应用ID
	 * @return
	 */
	public void deletePermission(Integer id, Integer appId);
	
	/**
	 * 删除应用下所有权限
	 * @param idList 应用ID集合
	 * @return
	 */
	public void deleteByAppIds(List<Integer> idList);
	
	/**
	 * 根据应用编码和管理员ID查权限
	 * @param appCode 应用编码
	 * @param userId 管理员ID
	 * @return
	 */
	public List<RpcPermission> findListById(String appCode, Integer userId);
}
