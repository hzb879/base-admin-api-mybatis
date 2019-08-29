package com.aswkj.admin.api.common.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.common.constant.CacheManagerConstant;
import com.aswkj.admin.api.common.model.QiniuBucketInfoModel;
import com.aswkj.admin.api.common.model.QiniuImageStoreModel;
import com.aswkj.admin.api.common.service.QiniuUploadService;
import com.aswkj.admin.api.config.QiniuUploadConfig;
import com.aswkj.admin.api.config.exception.CustomException;
import com.aswkj.admin.api.util.JsonUtil;
import com.aswkj.admin.api.util.SpringUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BucketInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class QiniuUploadServiceImpl implements QiniuUploadService, InitializingBean, ApplicationRunner {

  private StringMap putPolicy;

  @Autowired
  private Auth auth;

  @Value("${spring.servlet.multipart.location}")
  private String fileTempPath;

  @Value("${custom.upload.qiniu.bucket}")
  private String default_bucket;

  @Autowired
  private BucketManager bucketManager;

  private Map<String, BucketSetting> bucketSettingMap;

  @Data
  public static class BucketSetting {
    private String domain;
    private BucketManager bucketManager;
    private UploadManager uploadManager;
  }


  @Autowired
  @Qualifier("qiniuRegionManagerMap")
  Map<String, QiniuUploadConfig.RegionManager> regionManagerMap;

  @Override
  public QiniuImageStoreModel uploadFile(File file) throws QiniuException {
    return uploadFile(null, file);
  }

  @Override
  public QiniuImageStoreModel uploadFile(File file, String namespace) throws QiniuException {
    return uploadFile(null, file, namespace);
  }

  @Override
  public QiniuImageStoreModel uploadFile(String bucket, File file) throws QiniuException {
    return uploadFile(bucket, file, null);
  }

  @Override
  public QiniuImageStoreModel uploadFile(String bucket, File file, String namespace) throws QiniuException {
    return uploadFile(bucket, namespace, IdUtil.fastSimpleUUID(), file);
  }

  @Override
  public QiniuImageStoreModel uploadFile(String bucket, String key, File file) throws QiniuException {
    return uploadFile(bucket, null, key, file);
  }

  @Override
  public QiniuImageStoreModel uploadFile(String bucket, String namespace, String key, File file) throws QiniuException {
    key = getUploadKey(namespace, key);
    UploadManager uploadManager = getUploadManager(bucket);
    Response response = uploadManager.put(file, key, getUploadToken(bucket));
    int retry = 0;
    while (response.needRetry() && retry++ < 3) {
      response = uploadManager.put(file, key, getUploadToken(bucket));
    }
    return getQiniuImageStoreModel(response);
  }

  @Override
  public QiniuImageStoreModel uploadBytes(byte[] data) throws QiniuException {
    return this.uploadBytes(null, data);

  }

  @Override
  public QiniuImageStoreModel uploadBytes(String bucket, byte[] data) throws QiniuException {
    return uploadBytes(bucket, null, data);
  }

  @Override
  public QiniuImageStoreModel uploadBytes(String bucket, String namespace, byte[] data) throws QiniuException {
    String key = IdUtil.fastSimpleUUID();
    key = getUploadKey(namespace, key);
    UploadManager uploadManager = getUploadManager(bucket);
    Response response = uploadManager.put(data, key, getUploadToken(bucket));
    int retry = 0;
    while (response.needRetry() && retry < 3) {
      response = uploadManager.put(data, key, getUploadToken(bucket));
      retry++;
    }
    return getQiniuImageStoreModel(response);
  }

  @Override
  public QiniuImageStoreModel uploadBytes(byte[] data, String namespace) throws QiniuException {
    return uploadBytes(null, namespace, data);
  }

  @Override
  public QiniuImageStoreModel uploadMultipartFile(String bucket, MultipartFile file) {
    return uploadMultipartFile(bucket, null, file);
  }

  @Override
  public QiniuImageStoreModel uploadMultipartFile(String bucket, String namespace, MultipartFile file) {
    if (file.isEmpty()) {
      throw new CustomException("文件内容为空");
    }
    String fileName = file.getOriginalFilename();
    String key = IdUtil.fastSimpleUUID();
    File localFile = FileUtil.file(fileTempPath, key, fileName);
    localFile.getParentFile().mkdir();
    try {
      file.transferTo(localFile);
      return this.uploadFile(bucket, namespace, key, localFile);
    } catch (IOException e) {
      e.printStackTrace();
      throw new CustomException("文件上传失败");
    } finally {
      if (Objects.nonNull(localFile)) {
        FileUtil.del(localFile.getParentFile());
      }
    }
  }

  @Override
  public QiniuImageStoreModel uploadMultipartFile(MultipartFile file, String namespace) {
    return this.uploadMultipartFile(null, namespace, file);
  }

  @Override
  public QiniuImageStoreModel uploadMultipartFile(MultipartFile file) {
    return this.uploadMultipartFile(null, file);
  }

  @Override
  @Cacheable(cacheNames = "qiniu", key = "'bucket_info'", cacheManager = CacheManagerConstant.REDIS_30_MINUTES)
  public Map<String, QiniuBucketInfoModel> getQiniuBucketInfoModelMap() throws QiniuException {

    Map<String, QiniuBucketInfoModel> map = new HashMap<>();
    for (String bucket : bucketManager.buckets()) {
      QiniuBucketInfoModel info = new QiniuBucketInfoModel();
      BucketInfo bucketInfo = bucketManager.getBucketInfo(bucket);
      info.setRegion(bucketInfo.getRegion());
      info.setDomains(Arrays.asList(bucketManager.domainList(bucket)));
      map.put(bucket, info);
    }

    System.out.println(map);

    return map;
  }

  @Override
  @CacheEvict(cacheNames = "qiniu", key = "'bucket_info'", cacheManager = CacheManagerConstant.REDIS_30_MINUTES)
  public void clearQiniuBucketInfoModelMapCache() {
  }

  @Override
  public synchronized void resetBucketSettingMap() throws QiniuException {
    QiniuUploadService _this = getSelf();
    _this.clearQiniuBucketInfoModelMapCache();
    Map<String, BucketSetting> newBucketSettingMap = createBucketSettingMap(_this);
    this.bucketSettingMap = newBucketSettingMap;
  }

  private Map<String, BucketSetting> createBucketSettingMap(QiniuUploadService _this) throws QiniuException {
    Map<String, BucketSetting> newBucketSettingMap = new HashMap<>();
    Map<String, QiniuBucketInfoModel> qiniuBucketInfoModelMap = _this.getQiniuBucketInfoModelMap();
    for (String bucket : qiniuBucketInfoModelMap.keySet()) {
      BucketSetting setting = new BucketSetting();
      QiniuBucketInfoModel qiniuBucketInfoModel = qiniuBucketInfoModelMap.get(bucket);
      String region = qiniuBucketInfoModel.getRegion();
      setting.setDomain(StrUtil.addPrefixIfNot(qiniuBucketInfoModel.getDomains().get(0), "http://"));
      setting.setUploadManager(regionManagerMap.get(region).getUploadManager());
      setting.setBucketManager(regionManagerMap.get(region).getBucketManager());
      newBucketSettingMap.put(bucket, setting);
    }
    return newBucketSettingMap;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    //创建文件临时存储目录
    FileUtil.mkdir(fileTempPath);
    //上传默认策略设置
    defaultPutPolicyFill();
  }

  /**
   * 初始化工作
   * 由于 afterPropertiesSet 方法 获取不到 QiniuUploadService 代理对象，
   * 故在此方法里进行数据初始化
   *
   * @param args
   * @throws Exception
   */
  @Override
  public void run(ApplicationArguments args) throws Exception {
//    动态使用七牛API获取并保存每个bucket的相应信息
    this.bucketSettingMap = createBucketSettingMap(getSelf());
  }

  private void defaultPutPolicyFill() {
    this.putPolicy = new StringMap();
    QiniuImageStoreModel qiniuImageStoreModel = new QiniuImageStoreModel()
            .setBucket("$(bucket)")
            .setKey("$(key)")
            .setHash("$(etag)")
            .setWidth("$(imageInfo.width)")
            .setHeight("$(imageInfo.height)")
            .setMimeType("$(mimeType)")
            .setFilename("$(fname)")
            .setFileSize("$(fsize)");
    String returnBody = JsonUtil.createJsonStr(BeanUtil.beanToMap(qiniuImageStoreModel, false, true));
    putPolicy.put("returnBody", returnBody);
  }

  private UploadManager getUploadManager(String bucket) throws QiniuException {
    if (StrUtil.isBlank(bucket)) {
      bucket = default_bucket;
    }
    BucketSetting bucketSetting = bucketSettingMap.get(bucket);
    if (Objects.isNull(bucketSetting)) {
      //获取最新信息并刷新缓存,以确保最新添加的bucket有效
      this.resetBucketSettingMap();
      bucketSetting = bucketSettingMap.get(bucket);
      if (Objects.isNull(bucketSetting)) {
        //如果还是空，则说明此bucket无效！
        throw new CustomException(String.format("%s 空间名不存在！", bucket));
      }
    }

    return bucketSetting.getUploadManager();
  }

  private String getUploadKey(String namespace, String key) {
    if (StrUtil.isNotBlank(namespace)) {
      namespace = StrUtil.removePrefix(namespace, "/");
      namespace = StrUtil.addSuffixIfNot(namespace, "/");
      key = namespace + key;
    }
    return key;
  }

  private QiniuImageStoreModel getQiniuImageStoreModel(Response response) throws QiniuException {
    QiniuImageStoreModel qiniuImageStoreModel = JsonUtil.parseToObj(response.bodyString(), QiniuImageStoreModel.class);
    String domain = bucketSettingMap.get(qiniuImageStoreModel.getBucket()).getDomain();
    qiniuImageStoreModel.setDomain(domain);
    qiniuImageStoreModel.setSrc(StrUtil.appendIfMissing(domain, "/") + qiniuImageStoreModel.getKey());
    return qiniuImageStoreModel;
  }

  /**
   * 获取上传凭证
   *
   * @param bucket
   * @return 上传凭证
   */
  private String getUploadToken(String bucket) {
    if (StrUtil.isBlank(bucket)) {
      bucket = default_bucket;
    }
    return this.auth.uploadToken(bucket, null, 3600, putPolicy);
  }


  private String getUploadToken() {
    return getUploadToken(null);
  }

  private QiniuUploadService getSelf() {
    return SpringUtil.getBean(QiniuUploadService.class);
  }
}
