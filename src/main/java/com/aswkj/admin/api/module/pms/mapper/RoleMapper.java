package com.aswkj.admin.api.module.pms.mapper;

import com.aswkj.admin.api.module.pms.entity.Role;
import com.aswkj.admin.api.module.pms.vo.RoleVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
public interface RoleMapper extends BaseMapper<Role> {

  @Select("select id,name,cn_name from role")
  List<RoleVo> selectVoList();
}
