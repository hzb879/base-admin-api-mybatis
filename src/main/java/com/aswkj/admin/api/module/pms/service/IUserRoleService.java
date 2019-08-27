package com.aswkj.admin.api.module.pms.service;

import com.aswkj.admin.api.module.pms.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
public interface IUserRoleService extends IService<UserRole> {

  void setUserRoles(String userId, List<String> roleIds);
}
