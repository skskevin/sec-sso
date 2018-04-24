package com.mybase.ssm.sso.server.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mybase.ssm.sso.client.SessionUtils;
import com.mybase.ssm.sso.server.common.TokenManager;
import com.mybase.ssm.sso.util.CookieUtils;
import com.mybase.ssm.sso.util.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


/**
 * @author Joe
 */
@Api(tags = "单点登出")
@Controller
@RequestMapping("/logout")
public class LogoutController {
	
	@Resource
	private TokenManager tokenManager;

	@ApiOperation("登出")
	@RequestMapping(method = RequestMethod.GET)
	public String logout(@ApiParam(value = "返回链接") String backUrl, HttpServletRequest request) {
		String token = CookieUtils.getCookie(request, "token");
		if (StringUtils.isNotBlank(token)) {
			tokenManager.remove(token);
		}
		SessionUtils.invalidate(request);
		return "redirect:" + (StringUtils.isBlank(backUrl) ? "/admin/admin" : backUrl);
	}
}