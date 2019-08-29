package com.aswkj.admin.api.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext = null;

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


}
