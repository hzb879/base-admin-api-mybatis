package com.aswkj.admin.api.config;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("${custom.upload.local.root-directory}")
  String rootDirectory;

  @Value("${custom.upload.local.url-namespace}")
  String urlNamespace;

  /**
   * 跨域支持
   *
   * @param registry
   */
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**");
  }


  /**
   * 本地上传路径映射
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    urlNamespace = StrUtil.addPrefixIfNot(urlNamespace, "/");
    urlNamespace = StrUtil.addSuffixIfNot(urlNamespace, "/");
    rootDirectory = StrUtil.addSuffixIfNot(rootDirectory, "/");
    //本地上传文件路径映射
    registry.addResourceHandler(urlNamespace + "**").addResourceLocations("file://" + rootDirectory);

  }
}
