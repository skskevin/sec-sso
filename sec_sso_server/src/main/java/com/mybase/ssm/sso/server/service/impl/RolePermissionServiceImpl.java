package com.mybase.ssm.sso.server.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.mybase.ssm.sso.server.dao.RolePermissionDao;
import com.mybase.ssm.sso.server.model.RolePermission;
import com.mybase.ssm.sso.server.service.AppService;
import com.mybase.ssm.sso.server.service.PermissionJmsService;
import com.mybase.ssm.sso.server.service.RolePermissionService;
import com.mybase.ssm.sso.server.service.RoleService;
import com.mybase.ssm.sso.service.mybatis.impl.ServiceImpl;

@Service("rolePermissionService")
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionDao, RolePermission, Integer> implements RolePermissionService {
	
	@Resource
	private RoleService roleService;
	@Resource
	private AppService appService;
	@Resource
	private PermissionJmsService permissionJmsService;

	@Autowired
	public void setDao(RolePermissionDao dao) {
		this.dao = dao;
	}

	@Transactional
	public void allocate(Integer roleId, List<RolePermission> list) {
		dao.deleteByRoleIds(Arrays.asList(roleId));
		if(!CollectionUtils.isEmpty(list)) {
			super.save(list);
		}
		// JMS通知权限变更
		permissionJmsService.send(appService.get(roleService.get(roleId).getAppId()).getCode());
	}

	public List<RolePermission> findByRoleId(Integer roleId) {
		return dao.findByRoleId(roleId);
	}

	public void deleteByPermissionIds(List<Integer> idList) {
		dao.deleteByPermissionIds(idList);
	}
	
	public void deleteByRoleIds(List<Integer> idList) {
		dao.deleteByRoleIds(idList);
	}
	
	public void deleteByAppIds(List<Integer> idList) {
		dao.deleteByAppIds(idList);
	}
}
