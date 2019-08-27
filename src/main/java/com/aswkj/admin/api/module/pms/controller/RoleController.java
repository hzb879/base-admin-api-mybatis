package com.aswkj.admin.api.module.pms.controller;


import com.aswkj.admin.api.common.BaseQueryParams;
import com.aswkj.admin.api.common.constant.Constants;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.module.pms.entity.Role;
import com.aswkj.admin.api.module.pms.service.IRoleService;
import com.aswkj.admin.api.module.pms.vo.RoleVo;
import com.aswkj.admin.api.util.MybatisPlusQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@RestController
@RequestMapping("/pms/role")
@PreAuthorize("hasRole('admin')")
public class RoleController {

  @Autowired
  private IRoleService roleService;

  private static class QueryParams extends BaseQueryParams {
    public String keyWord;
    public String name;

  }

  @ApiOperation(value = "根据条件分页获取角色列表", notes = "备注")
  @PostMapping("/page")
  public ResponseData<IPage<Role>> getPage(@RequestBody(required = false) QueryParams queryParams) {
    MybatisPlusQueryUtil.PageQueryBuilder pageQueryBuilder = MybatisPlusQueryUtil.pageQueryBuilder(queryParams);

    pageQueryBuilder.addKeyWorldQuery("keyWord", "cnName", "description")
            .addEqQuery("name");
    Page page = pageQueryBuilder.buildPage();
    QueryWrapper queryWrapper = pageQueryBuilder.buildQueryWrapper();
    //默认不展示超级管理员角色
    queryWrapper.ne("name", Constants.SUPER_ADMIN_NAME);
    return ResponseData.success(roleService.page(page, queryWrapper));
  }

  @ApiOperation(value = "获取角色列表", notes = "备注")
  @GetMapping("/list")
  public ResponseData<List<RoleVo>> getList() {
    return ResponseData.success(roleService.selectVoList());
  }


  @ApiOperation(value = "保存角色信息", notes = "备注")
  @PostMapping
  public ResponseData<Role> save(@RequestBody Role role) {
    roleService.saveRole(role);
    return ResponseData.success(role);
  }


  @ApiOperation(value = "修改角色信息", notes = "备注")
  @PostMapping("/update")
  public ResponseData update(@RequestBody Role role) {
    roleService.updateRole(role);
    return ResponseData.successSign();
  }

  @ApiOperation(value = "删除角色信息", notes = "备注")
  @PostMapping("/remove")
  public ResponseData remove(@RequestParam String id) {
    roleService.deleteRole(id);
    return ResponseData.successSign();
  }

}
