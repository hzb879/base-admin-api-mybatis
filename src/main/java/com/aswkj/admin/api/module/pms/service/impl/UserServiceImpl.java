package com.aswkj.admin.api.module.pms.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import com.aswkj.admin.api.common.constant.CacheNameConstant;
import com.aswkj.admin.api.common.constant.Constants;
import com.aswkj.admin.api.common.enums.MediaStoreTypeEnum;
import com.aswkj.admin.api.common.enums.UserStatusEnum;
import com.aswkj.admin.api.common.model.LocalStoreModel;
import com.aswkj.admin.api.common.service.LocalFileUploadService;
import com.aswkj.admin.api.config.exception.CustomException;
import com.aswkj.admin.api.module.pms.entity.Avatar;
import com.aswkj.admin.api.module.pms.entity.User;
import com.aswkj.admin.api.module.pms.entity.UserRole;
import com.aswkj.admin.api.module.pms.mapper.UserMapper;
import com.aswkj.admin.api.module.pms.model.UserDetailsModel;
import com.aswkj.admin.api.module.pms.model.UserMediaModel;
import com.aswkj.admin.api.module.pms.model.UserModel;
import com.aswkj.admin.api.module.pms.service.IAvatarService;
import com.aswkj.admin.api.module.pms.service.IUserRoleService;
import com.aswkj.admin.api.module.pms.service.IUserService;
import com.aswkj.admin.api.module.pms.vo.UserExcelVo;
import com.aswkj.admin.api.module.pms.vo.UserVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserMapper userMapper;

  @Autowired
  IAvatarService avatarService;

  @Autowired
  LocalFileUploadService localFileUploadService;

  @Autowired
  IUserRoleService userRoleService;

  @Override
  public void registerUser(User user) {

    validateUsernameAndCipher(user.getUsername(), user.getCipher());
    ensureUsernameNotExist(user.getUsername());

    user.setCipher(passwordEncoder.encode(user.getCipher()));
    LocalDateTime now = LocalDateTime.now();
    user.setLastCipherUpdateTime(now);
    user.setLastLogoutTime(now);
    user.setStatus(UserStatusEnum.ENABLE);
    user.setName(Constants.DEFAULT_USER_NAME);
    this.save(user);
  }


  @Override
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#userId")
  public void changeUsernameAndCipher(String userId, String newUserName, String newCipher) {
    User dbUser = this.lambdaQuery().select(User::getUsername).eq(User::getId, userId).one();
    Assert.isFalse(Constants.SUPER_ADMIN_NAME.equals(dbUser.getUsername()), "不能修改超级管理员用户名或者密码！");
    validateUsernameAndCipher(newUserName, newCipher);
    if (!dbUser.getUsername().equals(newUserName)) {
      ensureUsernameNotExist(newUserName);
    }
    this.updateById(new User().setId(userId)
            .setUsername(newUserName)
            .setCipher(passwordEncoder.encode(newCipher))
            .setLastCipherUpdateTime(LocalDateTime.now())
    );
  }


  private void ensureUsernameNotExist(String username) {
    int count = this.count(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    Assert.isTrue(count == 0, "用户名 {} 已存在", username);
  }

  private void validateUsernameAndCipher(String username, String cipher) {
    Assert.notBlank(username, "用户名不能为空");
    Assert.notBlank(cipher, "密码不能为空");
    Assert.isTrue(Validator.isMactchRegex("^[a-zA-Z]{1}\\w{0,31}$",
            username),
            "用户名必须以字母开头，且是1-32位长度的字符，数字与下划线_的组合");
  }

  @Override
  public User getAuthInfoByUsername(String username) {
    return userMapper.getAuthInfoByUsername(username);
  }

  @Override
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#userId")
  public void changeCipher(String userId, String oldCipher, String newCipher) {

    User user = userMapper.getAuthInfoByUserId(userId);

    Assert.notNull(user, "用户不存在");

    if (!passwordEncoder.matches(oldCipher, user.getCipher())) {
      throw new CustomException("原密码错误");
    }

    this.updateById(new User()
            .setCipher(passwordEncoder.encode(newCipher))
            .setLastCipherUpdateTime(LocalDateTime.now())
            .setId(userId));
  }

  @Override
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#userId")
  public void changeCipher(String userId, String newCipher) {
    this.updateById(new User()
            .setCipher(passwordEncoder.encode(newCipher))
            .setLastCipherUpdateTime(LocalDateTime.now())
            .setId(userId));
  }

//  @Override
//  public UserDetailsModel getUserDetailsModelByUsername(String username) {
//    return userMapper.getUserDetailsModelByUsername(username);
//  }

  @Override
  @Cacheable(cacheNames = CacheNameConstant.USER_AUTH, key = "#userId")
  public UserDetailsModel getUserDetailsModelByUserId(String userId) {
    return userMapper.getUserDetailsModelByUserId(userId);
  }

  @Override
  public UserModel getUserModelByUserId(String userId) {
    return userMapper.getUserModelByUserId(userId);
  }

//  @Override
//  public UserModel getUserModelByUsername(String username) {
//    return userMapper.getUserModelByUsername(username);
//  }

//  @Override
//  @Transactional
//  public void saveAvatarAndUpdateUserAvatarByUsername(String username, LocalStoreModel localStoreModel) {
//    UserMediaModel userMediaModel = userMapper.getUserAvatarByUsername(username);
//    Avatar avatar = handleAvatar(localStoreModel, userMediaModel);
//    //更新用户头像id
//    this.lambdaUpdate()
//            .eq(User::getUsername, username)
//            .update(new User().setAvatarId(avatar.getId()));
//  }

  @Override
  @Transactional
  public void saveAvatarAndUpdateUserAvatarByUserId(String userId, LocalStoreModel localStoreModel) {
    UserMediaModel userMediaModel = userMapper.getUserAvatarByUserId(userId);
    Avatar avatar = handleAvatar(localStoreModel, userMediaModel);
    //更新用户头像id
    this.updateById(new User().setAvatarId(avatar.getId()).setId(userId));
  }

  private Avatar handleAvatar(LocalStoreModel localStoreModel, UserMediaModel userMediaModel) {
    Avatar avatar = userMediaModel.getAvatar();
    deleteAvatarIfExist(avatar);

    //保存头像
    Avatar dbAvatar = new Avatar();
    dbAvatar.setStoreType(MediaStoreTypeEnum.LOCAL);
    BeanUtil.copyProperties(localStoreModel, dbAvatar);
    avatarService.save(dbAvatar);
    return dbAvatar;
  }

  private void deleteAvatarIfExist(Avatar avatar) {
    if (Objects.nonNull(avatar) && Objects.nonNull(avatar.getId())) {
      //删除原先上传的旧头像
      if (Objects.nonNull(avatar.getStoreType())) {
        switch (avatar.getStoreType()) {
          case LOCAL:
            LocalStoreModel deleteModel = new LocalStoreModel();
            BeanUtil.copyProperties(avatar, deleteModel);
            localFileUploadService.deleteFile(deleteModel);
            break;
          case QINIU:
            break;
          default:
            break;
        }
      }
      avatarService.removeById(avatar.getId());
    }
  }


  @Override
  public IPage<UserVo> getUserVoPage(Page page, Wrapper wrapper) {
    page.setSearchCount(false);
    page.setTotal(userMapper.getUserVoCount(wrapper));
    return userMapper.getUserVoPage(page, wrapper);
  }

  @Override
  public List<UserExcelVo> getUserExcelVoList(Wrapper wrapper) {
    return userMapper.getUserExcelVoList(wrapper);
  }

  @Override
  @Transactional
  @CacheEvict(cacheNames = CacheNameConstant.USER_AUTH, key = "#id")
  public void deleteUser(String id) {
    UserMediaModel userMediaModel = userMapper.getUserAvatarByUserId(id);
    Assert.isFalse(Constants.SUPER_ADMIN_NAME.equals(userMediaModel.getUsername()),
            "不能删除超级管理员！");
    userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, id));
    deleteAvatarIfExist(userMediaModel.getAvatar());
    userMapper.deleteById(id);
  }


}
