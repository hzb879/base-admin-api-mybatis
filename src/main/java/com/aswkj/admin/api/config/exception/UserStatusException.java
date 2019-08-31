package com.aswkj.admin.api.config.exception;


/**
 * 用户状态异常， 不合法则不允许登陆
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
