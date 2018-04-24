package com.mybase.ssm.sso.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mybase.ssm.sso.captcha.CaptchaHelper;
import com.mybase.ssm.sso.client.SsoFilter;
import com.mybase.ssm.sso.controller.BaseController;
import com.mybase.ssm.sso.model.Result;
import com.mybase.ssm.sso.model.ResultCode;
import com.mybase.ssm.sso.provider.IdProvider;
import com.mybase.ssm.sso.provider.PasswordProvider;
import com.mybase.ssm.sso.server.common.LoginUser;
import com.mybase.ssm.sso.server.common.TokenManager;
import com.mybase.ssm.sso.server.model.User;
import com.mybase.ssm.sso.server.service.UserService;
import com.mybase.ssm.sso.util.CookieUtils;
import com.mybase.ssm.sso.util.StringUtils;
import com.mybase.ssm.sso.validator.Validator;
import com.mybase.ssm.sso.validator.annotation.ValidateParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Joe
 */
@Api(tags = "单点登录管理")
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController{
	
	// 登录页
	private static final String LOGIN_PATH = "/login";

	@Resource
	private TokenManager tokenManager;
	@Resource
	private UserService userService;

	@ApiOperation("登录页")
	@RequestMapping(method = RequestMethod.GET)
	public String login(
			@ApiParam(value = "返回链接", required = true) @ValidateParam({ Validator.NOT_BLANK }) String backUrl,
			HttpServletRequest request) {
		String token = CookieUtils.getCookie(request, "token");
		if (token == null) {
			return goLoginPath(backUrl, request);
		}
		else {
			LoginUser loginUser = tokenManager.validate(token);
			if (loginUser != null) {
				return "redirect:" + authBackUrl(backUrl, token);
			}
			else {
				return goLoginPath(backUrl, request);
			}
		}
	}

	@ApiOperation("登录提交")
	@RequestMapping(method = RequestMethod.POST)
	public String login(
			@ApiParam(value = "返回链接", required = true) @ValidateParam({ Validator.NOT_BLANK }) String backUrl,
			@ApiParam(value = "登录名", required = true) @ValidateParam({ Validator.NOT_BLANK }) String account,
			@ApiParam(value = "密码", required = true) @ValidateParam({ Validator.NOT_BLANK }) String password,
			@ApiParam(value = "验证码", required = true) @ValidateParam({ Validator.NOT_BLANK }) String captcha,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		if (!CaptchaHelper.validate(request, captcha)) {
			request.setAttribute("errorMessage", "验证码不正确");
			return goLoginPath(backUrl, request);
		}
		Result result = userService.login(getIpAddr(request), account, PasswordProvider.encrypt(password));
		if (!result.getCode().equals(ResultCode.SUCCESS)) {
			request.setAttribute("errorMessage", result.getMessage());
			return goLoginPath(backUrl, request);
		}
		else {
			User user = (User) result.getData();
			LoginUser loginUser = new LoginUser(user.getId(), user.getAccount());
			String token = CookieUtils.getCookie(request, "token");
			if (StringUtils.isBlank(token) || tokenManager.validate(token) == null) {// 没有登录的情况
				token = createToken(loginUser);
				addTokenInCookie(token, request, response);
			}

			// 跳转到原请求
			backUrl = URLDecoder.decode(backUrl, "utf-8");
			return "redirect:" + authBackUrl(backUrl, token);
		}
	}
	
	private String goLoginPath(String backUrl, HttpServletRequest request) {
		request.setAttribute("backUrl", backUrl);
		return LOGIN_PATH;
	}

	private String authBackUrl(String backUrl, String token) {
		StringBuilder sbf = new StringBuilder(backUrl);
		if (backUrl.indexOf("?") > 0) {
			sbf.append("&");
		}
		else {
			sbf.append("?");
		}
		sbf.append(SsoFilter.SSO_TOKEN_NAME).append("=").append(token);
		return sbf.toString();
	}

	private String createToken(LoginUser loginUser) {
		// 生成token
		String token = IdProvider.createUUIDId();

		// 缓存中添加token对应User
		tokenManager.addToken(token, loginUser);
		return token;
	}
	
	private void addTokenInCookie(String token, HttpServletRequest request, HttpServletResponse response) {
		// Cookie添加token
		Cookie cookie = new Cookie("token", token);
		cookie.setPath("/");
		if ("https".equals(request.getScheme())) {
			cookie.setSecure(true);
		}
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
}