package com.mybase.ssm.sso.server.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mybase.ssm.sso.server.dao.UserRoleDao;
import com.mybase.ssm.sso.server.model.UserRole;
import com.mybase.ssm.sso.server.service.UserRoleService;
import com.mybase.ssm.sso.service.mybatis.impl.ServiceImpl;

@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleDao, UserRole, Integer> implements UserRoleService {

	@Autowired
	public void setDao(UserRoleDao dao) {
		this.dao = dao;
	}
	
	@Transactional
	public void allocate(Integer userId, Integer appId, List<UserRole> list) {
		dao.deleteByUserIds(Arrays.asList(userId), appId);
		super.save(list);
	}
	
	public UserRole findByUserRoleId(Integer userId, Integer roleId) {
		return dao.findByUserRoleId(userId, roleId);
	}
	
	public void deleteByRoleIds(List<Integer> idList) {
		dao.deleteByRoleIds(idList);
	}
	
	public void deleteByUserIds(List<Integer> idList, Integer appId) {
		dao.deleteByUserIds(idList, appId);
	}
	
	public void deleteByAppIds(List<Integer> idList) {
		dao.deleteByAppIds(idList);
	}

	public void deleteForChangeApp(Integer userId, List<Integer> idList) {
		dao.deleteForChangeApp(userId, idList);
	}
}
