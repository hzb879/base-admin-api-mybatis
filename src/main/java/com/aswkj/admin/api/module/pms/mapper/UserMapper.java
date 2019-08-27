package com.aswkj.admin.api.module.pms.mapper;

import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.model.UserDetailsModel;
import com.aswkj.admin.api.module.pms.model.UserMediaModel;
import com.aswkj.admin.api.module.pms.model.UserModel;
import com.aswkj.admin.api.module.pms.vo.UserExcelVo;
import com.aswkj.admin.api.module.pms.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
public interface UserMapper extends BaseMapper<User> {

  UserDetailsModel getUserDetailsModelByUserId(String userId);

  UserMediaModel getUserAvatarByUserId(String userId);

  IPage<UserVo> getUserVoPage(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

  List<UserExcelVo> getUserExcelVoList(@Param(Constants.WRAPPER) Wrapper wrapper);

  long getUserVoCount(@Param(Constants.WRAPPER) Wrapper wrapper);

  @Select("select id, username, cipher, status from user where username = #{username}")
  User getAuthInfoByUsername(String username);

  @Select("select id, username, cipher from user where id = #{userId}")
  User getAuthInfoByUserId(String userId);

  UserModel getUserModelByUserId(String userId);

}
