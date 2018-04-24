package com.mybase.ssm.sso.server.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybase.ssm.sso.controller.BaseController;
import com.mybase.ssm.sso.model.Result;
import com.mybase.ssm.sso.server.model.App;
import com.mybase.ssm.sso.server.model.UserApp;
import com.mybase.ssm.sso.server.service.AppService;
import com.mybase.ssm.sso.server.service.UserAppService;
import com.mybase.ssm.sso.validator.Validator;
import com.mybase.ssm.sso.validator.annotation.ValidateParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Joe
 */
@Api(tags = "管理员应用关系管理")
@Controller
@RequestMapping("/admin/userApp")
public class UserAppController extends BaseController {

	@Resource
	private AppService appService;
	@Resource
	private UserAppService userAppService;
	
	@ApiOperation("初始页")
	@RequestMapping(value = "/allocate", method = RequestMethod.GET)
	public String edit(
			@ApiParam(value = "管理员id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer userId, Model model) {
		model.addAttribute("userId", userId);
		model.addAttribute("appList", getAppList(userId));
		return "/admin/userApp";
	}

	@ApiOperation("管理员应用关联提交")
	@RequestMapping(value = "/allocateSave", method = RequestMethod.POST)
	public @ResponseBody Result allocateSave(
			@ApiParam(value = "管理员id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer userId,
			@ApiParam(value = "应用ids") String appIds) {
		List<Integer> idList = getAjaxIds(appIds);
		List<UserApp> list = new ArrayList<UserApp>();
		UserApp bean = null;
		for (Integer appId : idList) {
			bean = new UserApp();
			bean.setAppId(appId);
			bean.setUserId(userId);
			list.add(bean);
		}
		userAppService.allocate(userId, idList, list);
		return Result.createSuccessResult().setMessage("授权成功");
	}
	
	private List<App> getAppList(Integer userId) {
		List<App> list = appService.findByAll(null);
		for (App app : list) {
			UserApp userApp = userAppService.findByUserAppId(userId, app.getId());
			if (null != userApp) {
				app.setIsChecked(true);
			}
			else {
				app.setIsChecked(false);
			}
		}
		return list;
	}
}