package com.aswkj.admin.api.config.exception;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class UserStatusException extends RuntimeException {

  public UserStatusException() {
  }

  public UserStatusException(String message) {
    super(message);
  }

  public UserStatusException(String message, Throwable cause) {
    super(message, cause);
  }
}
