package com.aswkj.admin.api.module.pms.service;

import com.aswkj.admin.api.module.pms.entity.Role;
import com.aswkj.admin.api.module.pms.vo.RoleVo;
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

public interface IRoleService extends IService<Role> {
  void saveRole(Role role);
  void updateRole(Role role);
  void deleteRole(String id);
  List<RoleVo> selectVoList();
}
