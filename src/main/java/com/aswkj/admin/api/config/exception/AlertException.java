package com.aswkj.admin.api.config.exception;


/**
 * 此异常让前端alert显示异常详细信息
 */
public class AlertException extends RuntimeException {

  public AlertException() {
  }

  public AlertException(String message) {
    super(message);
  }

  public AlertException(String message, Throwable cause) {
    super(message, cause);
  }
}
