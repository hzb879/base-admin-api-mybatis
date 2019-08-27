package com.aswkj.admin.api.module.pms.controller;


import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.module.pms.service.IUserRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@Api(value = "用户角色管理")
@RestController
@RequestMapping("/pms/user-role")
@PreAuthorize("hasRole('admin')")
public class UserRoleController {

  @Autowired
  private IUserRoleService userRoleService;


  private static class RoleInfo {
    public String userId;
    public List<String> roleIds;
  }

  @ApiOperation(value = "设置用户角色信息", notes = "备注")
  @PostMapping("/set-user")
  public ResponseData setUserRoles(@RequestBody RoleInfo roleInfo) {
    userRoleService.setUserRoles(roleInfo.userId, roleInfo.roleIds);
    return ResponseData.successSign();
  }


}
