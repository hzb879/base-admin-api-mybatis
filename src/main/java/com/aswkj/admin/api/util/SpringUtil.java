package com.aswkj.admin.api.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.aswkj.admin.api.config.exception.CustomException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
public class SpringUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;
  private static final String TMP_DIR = System.getProperty("java.io.tmpdir");


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringUtil.applicationContext = applicationContext;
  }

  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }

  public static <T> T getBean(String name, Class<T> calzz) {
    return applicationContext.getBean(name, calzz);
  }

  public static Object getBean(String name) {
    return applicationContext.getBean(name);
  }

  public static String getProperty(String key) {
    return applicationContext.getBean(Environment.class).getProperty(key);
  }

  public static File transferMultipartFileToLocalTmpFile(MultipartFile file, String tmpDir, String dirKey) throws IOException {
    if (Objects.isNull(file) || file.isEmpty()) {
      throw new CustomException("文件内容为空");
    }
    if (Objects.isNull(tmpDir)) {
      tmpDir = TMP_DIR;
    }
    if (Objects.isNull(dirKey)) {
      dirKey = IdUtil.fastSimpleUUID();
    }
    String fileName = file.getOriginalFilename();
    File localFile = FileUtil.file(tmpDir, dirKey, fileName);
    localFile.getParentFile().mkdir();
    file.transferTo(localFile);
    return localFile;
  }


  public static File transferMultipartFileToLocalTmpFile(MultipartFile file, String tmpDir) throws IOException {
    return transferMultipartFileToLocalTmpFile(file, tmpDir, null);
  }

  public static File transferMultipartFileToLocalTmpFile(MultipartFile file) throws IOException {
    return transferMultipartFileToLocalTmpFile(file, null);
  }

}
