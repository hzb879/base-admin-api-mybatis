package com.aswkj.admin.api.config.exception;

/**
 * 服务（业务）异常如“ 账号或密码错误 ”，该异常只做INFO级别的日志记录 @see WebMvcConfigurer
 */
public class CustomException extends RuntimeException {

  private static final long serialVersionUID = -2531223836758388000L;

  public CustomException() {
  }

  public CustomException(String message) {
    super(message);
  }

  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }
}
