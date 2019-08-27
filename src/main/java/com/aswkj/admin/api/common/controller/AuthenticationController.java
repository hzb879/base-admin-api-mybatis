package com.aswkj.admin.api.common.controller;


import cn.hutool.core.lang.Assert;
import com.aswkj.admin.api.common.constant.CacheNameConstant;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.config.exception.CustomException;
import com.aswkj.admin.api.config.security.auth.JwtAuthenticationRequest;
import com.aswkj.admin.api.config.security.helper.TokenHelper;
import com.aswkj.admin.api.config.security.helper.UserTokenState;
import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;

@Api(tags = "1.0.0", value = "认证管理")
@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

  @Autowired
  TokenHelper tokenHelper;

  @Lazy
  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  IUserService userService;


  @ApiOperation(value = "登陆获取access_token", notes = "备注")
  @PostMapping("/login")
  public ResponseData<UserTokenState> createAuthenticationToken(
          @RequestBody JwtAuthenticationRequest authenticationRequest) {

    Stream.of(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            .forEach(v -> Assert.notBlank(v, "用户名或密码不能为空"));

    User user = userService.getAuthInfoByUsername(authenticationRequest.getUsername());

    if (Objects.isNull(user) || !passwordEncoder.matches(authenticationRequest.getPassword(), user.getCipher())) {
      throw new CustomException("用户名或密码错误");
    }

    tokenHelper.validateUserStatus(user.getStatus());

    //修改最后登陆时间
    userService.lambdaUpdate()
            .set(User::getLastLoginTime, LocalDateTime.now())
            .eq(User::getId, user.getId())
            .update();
    return ResponseData.success(new UserTokenState(tokenHelper.generateToken(user.getId()), tokenHelper.getExpiredIn()));
  }

  @ApiOperation(value = "登出操作，使得token失效", notes = "备注")
  @PostMapping(value = "/logout")
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#principal.name")
  public ResponseData logout(Principal principal) {
    userService.lambdaUpdate()
            .set(User::getLastLogoutTime, LocalDateTime.now())
            .eq(User::getId, principal.getName())
            .update();
    return ResponseData.successSign();
  }


  @ApiOperation(value = "刷新access_token", notes = "备注")
  @PostMapping("/refresh")
  public ResponseData<UserTokenState> refreshAuthenticationToken(Principal principal) {
    return ResponseData.success(new UserTokenState(
            tokenHelper.generateToken(
                    principal.getName()),
            tokenHelper.getExpiredIn()));
  }

}