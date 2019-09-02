package com.aswkj.admin.api.module.pms.controller;


import cn.hutool.core.lang.Assert;
import com.aswkj.admin.api.common.BaseQueryParams;
import com.aswkj.admin.api.common.constant.CacheNameConstant;
import com.aswkj.admin.api.common.constant.Constants;
import com.aswkj.admin.api.common.enums.GenderEnum;
import com.aswkj.admin.api.common.enums.UserStatusEnum;
import com.aswkj.admin.api.common.enums.excel.UserExcelEnum;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.common.util.CommonUtil;
import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.model.UserModel;
import com.aswkj.admin.api.module.pms.service.IUserService;
import com.aswkj.admin.api.module.pms.vo.UserVo;
import com.aswkj.admin.api.util.MybatisPlusQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@Api(tags = "1.0.0", value = "用户管理")
@RestController
@RequestMapping("/pms/user")
public class UserController {


  @Autowired
  private IUserService userService;

  @Autowired
  RedisTemplate redisTemplate;

  @Lazy
  @Autowired
  PasswordEncoder passwordEncoder;

  @ApiOperation(value = "获取用户字典信息", notes = "备注")
  @GetMapping("/dict")
  public ResponseData<Map<String, Object>> getDict() {
    Map<String, Object> dict = new HashMap<>();
    dict.put("gender", CommonUtil.getEnumDictList(GenderEnum.class));
    dict.put("status", CommonUtil.getEnumDictList(UserStatusEnum.class));
    dict.put("excel", CommonUtil.getEnumDictList(UserExcelEnum.class));
    return ResponseData.success(dict);
  }

  @ApiOperation(value = "注册用户", notes = "备注")
  @PostMapping("/register")
  @PreAuthorize("hasRole('admin')")
  public ResponseData<UserVo> registerUser(@RequestBody User user) {
    userService.registerUser(user);

    return ResponseData.success(new UserVo()
            .setCreateTime(user.getCreateTime())
            .setName(user.getName())
            .setStatus(user.getStatus())
            .setUsername(user.getUsername())
            .setId(user.getId())
    );
  }

  @ApiOperation(value = "重设用户名和密码", notes = "备注")
  @PostMapping("/reset-auth")
  @PreAuthorize("hasRole('admin')")
  public ResponseData resetUserNameAndCipher(@RequestBody User user) {
    userService.changeUsernameAndCipher(user.getId(), user.getUsername(), user.getCipher());
    return ResponseData.successSign();
  }

  @ApiOperation(value = "设置用户状态", notes = "备注")
  @PostMapping("/set-status")
  @PreAuthorize("hasRole('admin')")
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#user.id")
  public ResponseData setUserStatus(@RequestBody User user) {
    userService.updateById(new User().setId(user.getId()).setStatus(user.getStatus()));
    return ResponseData.successSign();
  }

  @ApiOperation(value = "修改用户自己个人信息", notes = "备注")
  @PostMapping("/own-update")
  public ResponseData ownUpdate(@RequestBody User user, Principal principal) {
    userService.updateById(new User().setName(user.getName())
            .setBirthday(user.getBirthday())
            .setEmail(user.getEmail())
            .setGender(user.getGender())
            .setMobile(user.getMobile())
            .setDescription(user.getDescription())
            .setId(principal.getName()));
    return ResponseData.successSign();
  }

  @ApiOperation(value = "修改用户信息", notes = "备注")
  @PostMapping("/update")
  @PreAuthorize("hasRole('admin')")
  public ResponseData update(@RequestBody User user) {
    Assert.notBlank(user.getId(), "用户id不能为空");
    userService.updateById(new User().setName(user.getName())
            .setBirthday(user.getBirthday())
            .setEmail(user.getEmail())
            .setGender(user.getGender())
            .setMobile(user.getMobile())
            .setDescription(user.getDescription())
            .setId(user.getId()));
    return ResponseData.successSign();
  }

  private static class QueryParams extends BaseQueryParams {
    public String name;
    public String username;
    public LocalDateTime createTimeAfter;
    public LocalDateTime createTimeBefore;
    public Integer status;
    public Integer gender;
  }

  @ApiOperation(value = "删除用户", notes = "备注")
  @PostMapping("/remove")
  @PreAuthorize("hasRole('admin')")
  public ResponseData remove(@RequestParam String id) {
    userService.deleteUser(id);
    return ResponseData.successSign();
  }


  @ApiOperation(value = "根据条件分页获取用户列表", notes = "备注")
  @PostMapping("/page")
  @PreAuthorize("hasRole('admin')")
  public ResponseData<IPage<UserVo>> getPage(@RequestBody(required = false) QueryParams queryParams) {
    MybatisPlusQueryUtil.PageQueryBuilder pageQueryBuilder = MybatisPlusQueryUtil.pageQueryBuilder(queryParams);

    pageQueryBuilder.configFieldNamespace("u")
            .addMap("name")
            .addMap("username")
            .addMap("createTime")
            .addMap("status")
            .addMap("gender")
            .addMap("createTimeAfter", "createTime")
            .addMap("createTimeBefore", "createTime");

    pageQueryBuilder.addLikeQuery("name")
            .addLikeQuery("username")
            .addEqQuery("status")
            .addEqQuery("gender")
            .addGeQuery("createTimeAfter", "createTime")
            .addLeQuery("createTimeBefore", "createTime");


    Page page = pageQueryBuilder.buildPage();
    QueryWrapper queryWrapper = pageQueryBuilder.buildQueryWrapper();
    //默认不展示超级管理员
    queryWrapper.ne("u.username", Constants.SUPER_ADMIN_NAME);
    return ResponseData.success(userService.getUserVoPage(page, queryWrapper));
  }

  @ApiOperation(value = "获取用户自己的信息", notes = "备注")
  @GetMapping("/own-info")
  public ResponseData<UserModel> getOwnInfo(Principal principal) {
    return ResponseData.success(userService.getUserModelByUserId(principal.getName()));
  }

  @ApiOperation(value = "通过密码认证自己", notes = "备注")
  @PostMapping("/auth")
  public ResponseData auth(Principal principal, @RequestParam("password") String password) {
    User user = userService.getAuthInfoByUserId(principal.getName());
    Assert.isTrue(passwordEncoder.matches(password, user.getCipher()), "密码错误");
    return ResponseData.successSign();
  }

  @ApiOperation(value = "修改自己密码", notes = "备注")
  @PostMapping("/own-change-cipher")
  public ResponseData changeOwnCipher(@RequestBody CipherChanger cipherChanger, Principal principal) {
    userService.changeCipher(principal.getName(), cipherChanger.oldCipher, cipherChanger.newCipher);
    return ResponseData.successSign();
  }

  @ApiOperation(value = "修改用户密码", notes = "备注")
  @PostMapping("/change-cipher")
  @PreAuthorize("hasRole('admin')")
  public ResponseData changeCipher(@RequestBody CipherChanger cipherChanger) {
    userService.changeCipher(cipherChanger.userId, cipherChanger.newCipher);
    return ResponseData.successSign();
  }

  private static class CipherChanger {
    public String userId;
    public String oldCipher;
    public String newCipher;
  }

}
