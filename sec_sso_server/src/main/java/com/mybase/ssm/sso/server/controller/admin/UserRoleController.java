package com.mybase.ssm.sso.server.controller.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mybase.ssm.sso.controller.BaseController;
import com.mybase.ssm.sso.enums.TrueFalseEnum;
import com.mybase.ssm.sso.model.Result;
import com.mybase.ssm.sso.server.model.App;
import com.mybase.ssm.sso.server.model.Role;
import com.mybase.ssm.sso.server.model.UserRole;
import com.mybase.ssm.sso.server.service.AppService;
import com.mybase.ssm.sso.server.service.RoleService;
import com.mybase.ssm.sso.server.service.UserRoleService;
import com.mybase.ssm.sso.validator.Validator;
import com.mybase.ssm.sso.validator.annotation.ValidateParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Joe
 */
@Api(tags = "管理员角色关系管理")
@Controller
@RequestMapping("/admin/userRole")
public class UserRoleController extends BaseController {

	@Resource
	private AppService appService;
	@Resource
	private RoleService roleService;
	@Resource
	private UserRoleService userRoleService;

	@ApiOperation("初始页")
	@RequestMapping(value = "/allocate", method = RequestMethod.GET)
	public String edit(
			@ApiParam(value = "管理员id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer userId, Model model) {
		List<App> appList = appService.findByUserId(TrueFalseEnum.TRUE.getValue(), userId);
		model.addAttribute("userId", userId);
		model.addAttribute("appList", appList);
		model.addAttribute("roleList", getRoleList(userId, CollectionUtils.isEmpty(appList) ? null : appList.get(0).getId()));
		return "/admin/userRole";
	}
	
	@ApiOperation("管理员应用关系数据")
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public @ResponseBody Result changeApp(
			@ApiParam(value = "应用id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer appId,
			@ApiParam(value = "管理员id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer userId) {
		return Result.createSuccessResult().setData(getRoleList(userId, appId));
	}

	@ApiOperation("管理员角色关联提交")
	@RequestMapping(value = "/allocateSave", method = RequestMethod.POST)
	public @ResponseBody Result allocateSave(
			@ApiParam(value = "应用id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer appId,
			@ApiParam(value = "管理员id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer userId,
			@ApiParam(value = "角色ids") String roleIds) {
		List<Integer> idList = getAjaxIds(roleIds);
		List<UserRole> list = new ArrayList<UserRole>();
		UserRole bean = null;
		for (Integer roleId : idList) {
			bean = new UserRole();
			bean.setAppId(appId);
			bean.setUserId(userId);
			bean.setRoleId(roleId);
			list.add(bean);
		}
		userRoleService.allocate(userId, appId, list);
		return Result.createSuccessResult().setMessage("授权成功");
	}

	private List<Role> getRoleList(Integer userId, Integer appId) {
		List<Role> list = roleService.findByAppId(TrueFalseEnum.TRUE.getValue(), appId);
		for (Role role : list) {
			UserRole userRole = userRoleService.findByUserRoleId(userId, role.getId());
			if (null != userRole) {
				role.setIsChecked(true);
			}
			else {
				role.setIsChecked(false);
			}
		}
		return list;
	}
}