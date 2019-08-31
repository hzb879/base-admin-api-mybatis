package com.aswkj.admin.api.common.controller;


import com.aswkj.admin.api.common.enums.QiniuRegionEnum;
import com.aswkj.admin.api.common.model.LocalStoreModel;
import com.aswkj.admin.api.common.model.QiniuImageStoreModel;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.common.service.LocalFileUploadService;
import com.aswkj.admin.api.common.service.QiniuUploadService;
import com.aswkj.admin.api.config.DateTimeConfig;
import com.aswkj.admin.api.module.pms.service.IAvatarService;
import com.aswkj.admin.api.module.pms.service.IUserService;
import com.aswkj.admin.api.util.SpringUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Api(tags = "1.0.0", value = "文件上传")
@RestController
@RequestMapping(value = "/upload")
@Slf4j
public class UploadController {


  @Autowired
  LocalFileUploadService localFileUploadService;

  @Autowired
  QiniuUploadService qiniuUploadService;

  @Autowired
  IAvatarService avatarService;

  @Autowired
  IUserService userService;

  @Autowired
  BucketManager bucketManager;

  @ApiOperation(value = "七牛上传文件", notes = "备注")
  @PostMapping("/qiniu")
  public ResponseData<QiniuImageStoreModel> uploadQiniu(@RequestParam("file") MultipartFile file,
                                                        @RequestParam(value = "bucket", required = false) String bucket,
                                                        @RequestParam(value = "namespace", required = false) String namespace) {
    return ResponseData.success(qiniuUploadService.uploadMultipartFile(bucket, namespace, file));
  }


  @Data
  private static class BucketModel {
    private String bucket;
    private QiniuRegionEnum region;
  }

  @ApiOperation(value = "添加一个七牛存储空间", notes = "备注")
  @PostMapping("/qiniu/bucket/create")
  public ResponseData<String> createQiniuBucket(@RequestBody BucketModel bucketModel) throws QiniuException {
    Response response = bucketManager.createBucket(bucketModel.getBucket(), bucketModel.getRegion().getDbValue());
    qiniuUploadService.resetBucketSettingMap();
    return ResponseData.success(response.getInfo());
  }

//  @ApiOperation(value = "删除一个七牛存储空间", notes = "备注")
//  @PostMapping("/qiniu/bucket/remove")
//  public ResponseData<String> removeQiniuBucket(@RequestParam String bucket) throws QiniuException {
//    Response response = bucketManager.delete(bucket, null);
//    qiniuUploadService.resetBucketSettingMap();
//    return ResponseData.success(response.getInfo());
//  }


  @ApiOperation(value = "本地上传用户自己头像", notes = "备注")
  @PostMapping("/local/own-avatar")
  public ResponseData<LocalStoreModel> uploadOwnAvatarLocal(@RequestParam("file") MultipartFile file, Principal principal) {

    LocalStoreModel localStoreModel = localFileUploadService.upload(file);
    userService.saveAvatarAndUpdateUserAvatarByUserId(principal.getName(), localStoreModel);
    return ResponseData.success(localStoreModel);
  }


  @ApiOperation(value = "本地上传用户自己头像", notes = "备注")
  @GetMapping("/test")
  public ResponseData test() throws QiniuException {
    qiniuUploadService.resetBucketSettingMap();
    System.out.println(SpringUtil.getBean(DateTimeConfig.class));
    return ResponseData.success("111");
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