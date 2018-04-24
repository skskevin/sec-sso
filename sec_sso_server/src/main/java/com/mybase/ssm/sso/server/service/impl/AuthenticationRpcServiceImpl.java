package com.mybase.ssm.sso.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mybase.ssm.sso.rpc.AuthenticationRpcService;
import com.mybase.ssm.sso.rpc.RpcPermission;
import com.mybase.ssm.sso.rpc.RpcUser;
import com.mybase.ssm.sso.server.common.LoginUser;
import com.mybase.ssm.sso.server.common.TokenManager;
import com.mybase.ssm.sso.server.service.PermissionService;
import com.mybase.ssm.sso.server.service.UserService;
import com.mybase.ssm.sso.util.StringUtils;

@Service("authenticationRpcService")
public class AuthenticationRpcServiceImpl implements AuthenticationRpcService {

	@Resource
	private PermissionService permissionService;
	@Resource
	private UserService userService;
	@Resource
	private TokenManager tokenManager;

	@Override
	public boolean validate(String token) {
		return tokenManager.validate(token) != null;
	}
	
	@Override
	public RpcUser findAuthInfo(String token) {
		LoginUser user = tokenManager.validate(token);
		if (user != null) {
			return new RpcUser(user.getAccount());
		}
		return null;
	}
	
	@Override
	public List<RpcPermission> findPermissionList(String token, String appCode) {
		if (StringUtils.isBlank(token)) {
			return permissionService.findListById(appCode, null);
		}
		else {
			LoginUser user = tokenManager.validate(token);
			if (user != null) {
				return permissionService.findListById(appCode, user.getUserId());
			}
			else {
				return new ArrayList<RpcPermission>(0);
			}
		}
	}
}
