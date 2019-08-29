package com.aswkj.admin.api;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AdminApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(AdminApiApplication.class, args);
  }

}
