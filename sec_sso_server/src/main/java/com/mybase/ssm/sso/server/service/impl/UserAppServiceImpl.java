package com.mybase.ssm.sso.server.service.impl;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.mybase.ssm.sso.server.dao.UserAppDao;
import com.mybase.ssm.sso.server.model.UserApp;
import com.mybase.ssm.sso.server.service.UserAppService;
import com.mybase.ssm.sso.server.service.UserRoleService;
import com.mybase.ssm.sso.service.mybatis.impl.ServiceImpl;

@Service("userAppService")
public class UserAppServiceImpl extends ServiceImpl<UserAppDao, UserApp, Integer> implements UserAppService {

	@Resource
	private UserRoleService userRoleService;
	
	@Autowired
	public void setDao(UserAppDao dao) {
		this.dao = dao;
	}
	
	@Transactional
	public void allocate(Integer userId, List<Integer> idList, List<UserApp> list) {
		userRoleService.deleteForChangeApp(userId, idList);
		dao.deleteByUserIds(Arrays.asList(userId));
		if(!CollectionUtils.isEmpty(list)) {
			super.save(list);
		}
	}
	
	public UserApp findByUserAppId(Integer userId, Integer roleId) {
		return dao.findByUserAppId(userId, roleId);
	}
	
	public void deleteByUserIds(List<Integer> idList) {
		dao.deleteByUserIds(idList);
	}
	
	public void deleteByAppIds(List<Integer> idList) {
		dao.deleteByAppIds(idList);
	}
}
