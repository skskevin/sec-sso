package com.mybase.ssm.sso.server.controller.admin;

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
import com.mybase.ssm.sso.server.model.Permission;
import com.mybase.ssm.sso.server.service.AppService;
import com.mybase.ssm.sso.server.service.PermissionService;
import com.mybase.ssm.sso.validator.Validator;
import com.mybase.ssm.sso.validator.annotation.ValidateParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Joe
 */
@Api(tags = "权限(含菜单)管理")
@Controller
@RequestMapping("/admin/permission")
public class PermissionController extends BaseController {

	@Resource
	private PermissionService permissionService;
	@Resource
	private AppService appService;

	@ApiOperation("初始页")
	@RequestMapping(method = RequestMethod.GET)
	public String execute(Model model) {
		model.addAttribute("appList", getAppList());
		return "/admin/permission";
	}

	@ApiOperation("权限树节点")
	@RequestMapping(value = "/nodes", method = RequestMethod.GET)
	public @ResponseBody List<Permission> nodes(
			@ApiParam(value = "应用id") Integer appId,
			@ApiParam(value = "名称") String name,
			@ApiParam(value = "是否启用 ") Boolean isEnable) {
		List<Permission> list = permissionService.findByName(name, appId, isEnable);
		Permission permission = new Permission();
		permission.setId(null);
		permission.setParentId(-1);
		permission.setName("根节点");
		permission.setAppId(appId);
		list.add(0, permission);
		return list;
	}

	@ApiOperation("新增/修改提交")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Result save(
			@ApiParam(value = "id") Integer id,
			@ApiParam(value = "应用id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer appId,
			@ApiParam(value = "父id", required = true) Integer parentId,
			@ApiParam(value = "图标") String icon,
			@ApiParam(value = "名称", required = true) @ValidateParam({ Validator.NOT_BLANK }) String name,
			@ApiParam(value = "权限URL", required = true) @ValidateParam({ Validator.NOT_BLANK }) String url,
			@ApiParam(value = "排序", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer sort,
			@ApiParam(value = "是否菜单", required = true) @ValidateParam({ Validator.NOT_BLANK }) Boolean isMenu,
			@ApiParam(value = "是否启用", required = true) @ValidateParam({ Validator.NOT_BLANK }) Boolean isEnable) {
		Permission permission;
		if (id == null) {
			permission = new Permission();
		}
		else {
			permission = permissionService.get(id);
		}
		permission.setAppId(appId);
		permission.setParentId(parentId);
		permission.setIcon(icon);
		permission.setName(name);
		permission.setUrl(url);
		permission.setSort(sort);
		permission.setIsMenu(isMenu);
		permission.setIsEnable(isEnable);
		permissionService.save(permission);
		return Result.createSuccessResult().setMessage("保存成功");
	}

	@ApiOperation("删除")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Result delete(
			@ApiParam(value = "id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer id,
			@ApiParam(value = "应用id", required = true) @ValidateParam({ Validator.NOT_BLANK }) Integer appId) {
		permissionService.deletePermission(id, appId);
		return Result.createSuccessResult().setMessage("删除成功");
	}

	private List<App> getAppList() {
		return appService.findByAll(null);
	}
}