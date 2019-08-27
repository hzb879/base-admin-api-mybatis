package com.aswkj.admin.api.module.pms.service;

import com.aswkj.admin.api.common.model.LocalStoreModel;
import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.model.UserDetailsModel;
import com.aswkj.admin.api.module.pms.model.UserModel;
import com.aswkj.admin.api.module.pms.vo.UserExcelVo;
import com.aswkj.admin.api.module.pms.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
public interface IUserService extends IService<User> {

  void registerUser(User user);

  User getAuthInfoByUsername(String username);

  void changeCipher(String userId, String oldCipher, String newCipher);

  void changeCipher(String userId, String newCipher);

  void changeUsernameAndCipher(String userId, String newUserName, String newCipher);

//  /**
//   * 获取带有角色信息的userDetails,用于认证授权
//   *
//   * @param username
//   * @return
//   */
//  UserDetailsModel getUserDetailsModelByUsername(String username);

  /**
   * 获取带有角色信息的userDetails,用于认证授权
   *
   * @param userId
   * @return
   */
  UserDetailsModel getUserDetailsModelByUserId(String userId);

  /**
   * 根据用户id获取用户详细信息，带有角色信息
   *
   * @param userId
   * @return
   */
  UserModel getUserModelByUserId(String userId);

  /**
   * 保存并修改头像信息
   *
   * @param username        用户名
   * @param localStoreModel
   */
//  void saveAvatarAndUpdateUserAvatarByUsername(String username, LocalStoreModel localStoreModel);

  /**
   * 保存并修改头像信息
   *
   * @param userId          用户id
   * @param localStoreModel
   */
  void saveAvatarAndUpdateUserAvatarByUserId(String userId, LocalStoreModel localStoreModel);

  IPage<UserVo> getUserVoPage(Page page, Wrapper wrapper);

  List<UserExcelVo> getUserExcelVoList(Wrapper wrapper);

  void deleteUser(String id);


}
