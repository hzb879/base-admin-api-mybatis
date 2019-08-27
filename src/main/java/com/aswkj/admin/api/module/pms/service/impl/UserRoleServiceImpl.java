package com.aswkj.admin.api.module.pms.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.aswkj.admin.api.common.constant.CacheNameConstant;
import com.aswkj.admin.api.common.constant.Constants;
import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.entity.UserRole;
import com.aswkj.admin.api.module.pms.mapper.UserRoleMapper;
import com.aswkj.admin.api.module.pms.service.IUserRoleService;
import com.aswkj.admin.api.module.pms.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

  @Autowired
  private IUserService userService;

  @Override
  @Transactional
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#userId")
  public void setUserRoles(String userId, List<String> roleIds) {
    Assert.notBlank(userId, "用户id不能为空");
    User dbUser = userService.lambdaQuery().select(User::getUsername).eq(User::getId, userId).one();
    Assert.isFalse(Constants.SUPER_ADMIN_NAME.equals(dbUser.getUsername()), "不能设置超级管理用户角色！");
    this.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
    if (CollectionUtil.isNotEmpty(roleIds)) {
      List<UserRole> userRoles = roleIds.
              stream()
              .map(roleId -> new UserRole().setUserId(userId).setRoleId(roleId))
              .collect(Collectors.toList());
      this.saveBatch(userRoles);
    }
  }
}
