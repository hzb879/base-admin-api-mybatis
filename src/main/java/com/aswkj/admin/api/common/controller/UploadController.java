package com.aswkj.admin.api.common.controller;


import com.aswkj.admin.api.common.model.LocalStoreModel;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.common.service.LocalFileUploadService;
import com.aswkj.admin.api.module.pms.service.IAvatarService;
import com.aswkj.admin.api.module.pms.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Api(tags = "1.0.0", value = "文件上传")
@RestController
@RequestMapping(value = "/upload")
public class UploadController {


  @Autowired
  LocalFileUploadService localFileUploadService;

  @Autowired
  IAvatarService avatarService;

  @Autowired
  IUserService userService;

  @ApiOperation(value = "七牛上传文件", notes = "备注")
  @PostMapping("/qiniu")
  public ResponseData uploadQiniu() {
    return null;
  }

  @ApiOperation(value = "本地上传用户自己头像", notes = "备注")
  @PostMapping("/local/own-avatar")
  public ResponseData<LocalStoreModel> uploadOwnAvatarLocal(@RequestParam("file") MultipartFile file, Principal principal) {

    LocalStoreModel localStoreModel = localFileUploadService.upload(file);
    userService.saveAvatarAndUpdateUserAvatarByUserId(principal.getName(), localStoreModel);
    return ResponseData.success(localStoreModel);
  }


  @ApiOperation(value = "本地上传用户头像", notes = "备注")
  @PostMapping("/local/avatar")
  public ResponseData<LocalStoreModel> uploadAvatarLocal(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("userId") String userId) {
    LocalStoreModel localStoreModel = localFileUploadService.upload(file);
    userService.saveAvatarAndUpdateUserAvatarByUserId(userId, localStoreModel);
    return ResponseData.success(localStoreModel);
  }


}