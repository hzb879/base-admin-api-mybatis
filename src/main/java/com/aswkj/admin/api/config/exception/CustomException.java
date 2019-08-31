package com.aswkj.admin.api.config.exception;

/**
 * 业务异常
 */
public class CustomException extends RuntimeException {

  public CustomException() {
  }

  public CustomException(String message) {
    super(message);
  }

  public CustomException(String message, Throwable cause) {
    super(message, cause);
  }
}
