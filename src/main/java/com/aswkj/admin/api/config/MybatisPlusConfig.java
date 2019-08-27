package com.aswkj.admin.api.config;

import com.aswkj.admin.api.module.pms.model.UserDetailsModel;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Configuration
@MapperScan("com.aswkj.admin.api.module.*.mapper")
@EnableTransactionManagement
public class MybatisPlusConfig {


  /**
   * 表字段自动填充配置
   * @return
   */
  @Bean
  public MetaObjectHandler CommonFieldHandler() {
    return new MetaObjectHandler() {
      @Override
      public void insertFill(MetaObject metaObject) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authentication)) {
          UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();
          String id = userDetails.getId();
          this.setFieldValByName("createUser", id, metaObject);
          this.setFieldValByName("updateUser", id, metaObject);
        }
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName("createTime", now, metaObject);
        this.setFieldValByName("updateTime", now, metaObject);
      }

      @Override
      public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authentication)) {
          UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();
          this.setFieldValByName("updateUser", userDetails.getId(), metaObject);
        }
      }
    };

  }

  /**
   * SQL执行效率插件
   */
  @Bean
  @Profile({"dev","test"})// 设置 dev test 环境开启
  public PerformanceInterceptor performanceInterceptor() {
    PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
    return performanceInterceptor;
  }


  /**
   * 分页插件
   */
  @Bean
  public PaginationInterceptor paginationInterceptor() {
    PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
    // paginationInterceptor.setLimit(你的最大单页限制数量，默认 500 条，小于 0 如 -1 不受限制);
//    paginationInterceptor.setLimit(500);
    return paginationInterceptor;
  }


}

