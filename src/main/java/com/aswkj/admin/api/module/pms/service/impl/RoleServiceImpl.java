package com.aswkj.admin.api.module.pms.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.common.constant.Constants;
import com.aswkj.admin.api.module.pms.entity.Role;
import com.aswkj.admin.api.module.pms.entity.UserRole;
import com.aswkj.admin.api.module.pms.mapper.RoleMapper;
import com.aswkj.admin.api.module.pms.service.IRoleService;
import com.aswkj.admin.api.module.pms.service.IUserRoleService;
import com.aswkj.admin.api.module.pms.vo.RoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

  @Autowired
  RoleMapper roleMapper;

  @Autowired
  IUserRoleService userRoleService;

  @Override
  public void saveRole(Role role) {
    validateRole(role);
    ensureRoleNameNotExist(role);
    ensureRoleCnNameNotExist(role);
    this.save(role);
  }

  @Override
  public void updateRole(Role role) {
    Assert.notBlank(role.getId(), "角色id不能为空！");
    validateRole(role);
    Role dbRole = this.lambdaQuery().select(Role::getName, Role::getCnName).eq(Role::getId, role.getId()).one();
    //不能修改admin的英文名称
    Assert.isFalse(Constants.SUPER_ADMIN_NAME.equals(dbRole.getName())
                    && !dbRole.getName().equals(role.getName()),
            "不能修改超级管理员的英文名称 admin !");

    if (!role.getName().equals(dbRole.getName())) {
      ensureRoleNameNotExist(role);
    }
    if (!role.getCnName().equals(dbRole.getCnName())) {
      ensureRoleCnNameNotExist(role);
    }
    this.updateById(new Role().setId(role.getId())
            .setName(role.getName())
            .setCnName(role.getCnName())
            .setDescription(role.getDescription())
    );

  }

  @Override
  @Transactional
  public void deleteRole(String id) {
    Role role = this.lambdaQuery().select(Role::getName).eq(Role::getId, id).one();
    Assert.isFalse(Constants.SUPER_ADMIN_NAME.equals(role.getName()), "不能删除超级管理员角色!");
    userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, id));
    this.removeById(id);
  }

  @Override
  public List<RoleVo> selectVoList() {
    return roleMapper.selectVoList();
  }

  private void ensureRoleCnNameNotExist(Role role) {
    Integer cnNameCount = this.lambdaQuery().eq(Role::getCnName, role.getCnName()).count();
    Assert.isTrue(cnNameCount == 0, "{}已经被使用", role.getCnName());
  }

  private void ensureRoleNameNotExist(Role role) {
    Integer nameCount = this.lambdaQuery().eq(Role::getName, role.getName()).count();
    Assert.isTrue(nameCount == 0, "{}已经被使用", role.getName());
  }

  private void validateRole(Role role) {
    Assert.notBlank(role.getName(), "角色英文名不能为空!");
    Assert.notBlank(role.getCnName(), "角色中文名不能为空!");
    Assert.isTrue(Validator.isChinese(role.getCnName()), "角色中文名含有非中文字符");
    Assert.isTrue(
            Validator.isLowerCase(
                    StrUtil.removeAll(role.getName(), "_")),
            "角色英文名只能包含英文小写字符和下划线_");
  }
}
