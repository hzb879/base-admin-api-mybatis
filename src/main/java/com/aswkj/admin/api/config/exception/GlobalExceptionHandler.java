package com.aswkj.admin.api.config.exception;

import com.aswkj.admin.api.common.enums.ResponseMsgEnum;
import com.aswkj.admin.api.common.response.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 捕捉自定义异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(CustomException.class)
  public ResponseData<String> customException(CustomException e) {
    return ResponseData.failMsg(e.getMessage());
  }


  /**
   * 断言异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseData<String> illegalArgumentException(IllegalArgumentException e) {
    return ResponseData.failMsg(e.getMessage());
  }


  /**
   * 用户状态异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(UserStatusException.class)
  public ResponseData<String> userStatusException(UserStatusException e) {
    return ResponseData.failMsg(e.getMessage());
  }

  /**
   * 用户名或密码错误异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(AuthenticationException.class)
  public ResponseData<String> authenticationException(AuthenticationException e) {
    return ResponseData.responseMsgEnum(ResponseMsgEnum.USERNAME__PASSWORD_ERROR);
  }

  /**
   * 捕捉访问权限不足异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ResponseData<String> accessDeniedException(AccessDeniedException e) {
    return ResponseData.responseMsgEnum(ResponseMsgEnum.ACCESS_FORBIDDEN);
  }

  /**
   * 捕捉其他所有异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseData<String> globalException(Exception e) {
    e.printStackTrace();
    return ResponseData.failMsg(e.getMessage());
  }


}

