package com.aswkj.admin.api.config;

import com.aswkj.admin.api.common.enums.QiniuRegionEnum;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "custom.upload.qiniu")
public class QiniuUploadConfig {

  private String accessKey;

  private String secretKey;

  @Data
  public static class RegionManager {
    private BucketManager bucketManager;
    private UploadManager uploadManager;
  }


  @Bean
  public Map<String, RegionManager> qiniuRegionManagerMap(Auth auth) {
    Map<String, RegionManager> regionManagerMap = new HashMap<>();


    Map<String, com.qiniu.storage.Configuration> configMap = new HashMap<>();
    configMap.put(QiniuRegionEnum.huadong.getDbValue(), new com.qiniu.storage.Configuration(Region.huadong()));
    configMap.put(QiniuRegionEnum.huabei.getDbValue(), new com.qiniu.storage.Configuration(Region.huabei()));
    configMap.put(QiniuRegionEnum.huanan.getDbValue(), new com.qiniu.storage.Configuration(Region.huanan()));
    configMap.put(QiniuRegionEnum.beimei.getDbValue(), new com.qiniu.storage.Configuration(Region.beimei()));
    configMap.put(QiniuRegionEnum.xinjiapo.getDbValue(), new com.qiniu.storage.Configuration(Region.xinjiapo()));

    for (String region : configMap.keySet()) {
      RegionManager regionManager = new RegionManager();
      com.qiniu.storage.Configuration config = configMap.get(region);
      regionManager.setUploadManager(new UploadManager(config));
      regionManager.setBucketManager(new BucketManager(auth, config));
      regionManagerMap.put(region, regionManager);
    }
    return regionManagerMap;
  }


  /**
   * 认证信息实例
   */
  @Bean
  public Auth auth() {
    return Auth.create(accessKey, secretKey);
  }


  /**
   * 构建七牛空间管理实例
   */
  @Bean
  public BucketManager bucketManager() throws QiniuException {
    BucketManager bucketManager = new BucketManager(auth(), new com.qiniu.storage.Configuration(Region.autoRegion()));
    return bucketManager;
  }

}
