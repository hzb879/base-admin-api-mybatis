package com.aswkj.admin.api.config;

import com.aswkj.admin.api.common.constant.CacheManagerConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.net.URISyntaxException;

@Configuration
public class EhcacheConfig {

  /**
   * ehcache 转jcache 的xml配置
   *
   * @return
   * @throws URISyntaxException
   */
  @Bean(CacheManagerConstant.EHCACHE_LOCAL)
  CacheManager ehcache() throws URISyntaxException {
    CachingProvider cachingProvider = Caching.getCachingProvider();
    javax.cache.CacheManager manager = cachingProvider.getCacheManager(
            getClass().getResource("/config/ehcache.xml").toURI(),
            getClass().getClassLoader());
    return new JCacheCacheManager(manager);
  }
}
