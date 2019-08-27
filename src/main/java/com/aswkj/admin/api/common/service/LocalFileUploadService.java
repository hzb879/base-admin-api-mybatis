package com.aswkj.admin.api.common.service;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.common.model.LocalStoreModel;
import com.aswkj.admin.api.config.exception.CustomException;
import com.aswkj.admin.api.module.pms.service.IAvatarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
public class LocalFileUploadService implements InitializingBean {

  @Value("${custom.upload.local.root-directory}")
  String rootDirectory;

  @Value("${custom.upload.local.url-namespace}")
  String urlNamespace;

  @Value("${custom.domain}")
  String domain;

  String concatUrlNamespace;

  @Autowired
  IAvatarService avatarService;

  private DateTimeFormatter year = DateTimeFormatter.ofPattern("yyyy");
  private DateTimeFormatter month = DateTimeFormatter.ofPattern("MM");
  private DateTimeFormatter day = DateTimeFormatter.ofPattern("dd");


  public LocalStoreModel upload(MultipartFile file) {
    if (file.isEmpty()) {
      throw new CustomException("文件内容为空");
    }
    String originalFilename = file.getOriginalFilename();

    String fileType = StrUtil.subAfter(originalFilename, ".", true);
    String localFileKey = generateFileKeyByDate(fileType);
    File destination = FileUtil.file(rootDirectory, localFileKey);
    FileUtil.mkParentDirs(destination);
    try {
      file.transferTo(destination);
    } catch (IOException e) {
      log.error("【文件上传至本地】失败，绝对路径：{}", destination.getAbsolutePath());
      throw new CustomException("本地文件上传失败");
    }

    String requestDomain = StrUtil.removeSuffix(domain, "/");

    LocalStoreModel localStoreModel = new LocalStoreModel().setDomain(requestDomain)
            .setFilename(originalFilename)
            .setLocalFileKey(localFileKey)
            .setLocalRootDirectory(rootDirectory)
            .setLocalUrlNamespace(urlNamespace)
            .setSrc(requestDomain + concatUrlNamespace + localFileKey);

    return localStoreModel;
  }


  public void deleteFile(LocalStoreModel localStoreModel) {
    File file = FileUtil.file(localStoreModel.getLocalRootDirectory(), localStoreModel.getLocalFileKey());
    if (Objects.nonNull(file) && file.exists()) {
      FileUtil.del(file);
    }
  }


  /**
   * 根据日期生成唯一子文件
   *
   * @param fileType
   * @return
   */
  private String generateFileKeyByDate(String fileType) {
    LocalDate now = LocalDate.now();
    return ArrayUtil.join(new String[]{
            now.format(year), now.format(month), now.format(day),
            IdUtil.fastSimpleUUID() + "." + fileType}, "/");

  }


  @Override
  public void afterPropertiesSet() throws Exception {
    concatUrlNamespace = StrUtil.addPrefixIfNot(urlNamespace, "/");
    concatUrlNamespace = StrUtil.addSuffixIfNot(concatUrlNamespace, "/");
  }

}
