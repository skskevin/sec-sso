package com.mybase.ssm.sso.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mybase.ssm.sso.model.Pagination;
import com.mybase.ssm.sso.server.dao.RoleDao;
import com.mybase.ssm.sso.server.model.Role;
import com.mybase.ssm.sso.server.service.RolePermissionService;
import com.mybase.ssm.sso.server.service.RoleService;
import com.mybase.ssm.sso.server.service.UserRoleService;
import com.mybase.ssm.sso.service.mybatis.impl.ServiceImpl;

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role, Integer> implements RoleService {

	@Resource
	private UserRoleService userRoleService;
	@Resource
	private RolePermissionService rolePermissionService;

	@Autowired
	public void setDao(RoleDao dao) {
		this.dao = dao;
	}

	public void enable(Boolean isEnable, List<Integer> idList) {
		verifyRows(dao.enable(isEnable, idList), idList.size(), "角色数据库更新失败");
	}

	public void save(Role t) {
		super.save(t);
	}

	public Pagination<Role> findPaginationByName(String name, Integer appId, Pagination<Role> p) {
		dao.findPaginationByName(name, null, appId, p);
		return p;
	}

	public List<Role> findByAppId(Boolean isEnable, Integer appId) {
		if (appId == null)
			return new ArrayList<Role>(0);
		return dao.findPaginationByName(null, isEnable, appId, null);
	}

	@Transactional
	public void deleteById(List<Integer> idList) {
		userRoleService.deleteByRoleIds(idList);
		rolePermissionService.deleteByRoleIds(idList);
		verifyRows(dao.deleteById(idList), idList.size(), "角色数据库删除失败");
	}

	public void deleteByAppIds(List<Integer> idList) {
		dao.deleteByAppIds(idList);
	}
}
