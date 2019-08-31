package com.aswkj.admin.api.config.exception;

import com.aswkj.admin.api.common.enums.ResponseMsgEnum;
import com.aswkj.admin.api.common.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @Value("${custom.upload.maxFileSize}")
  private String maxFileSize;


  /**
   * 捕捉业务自定义异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(CustomException.class)
  public ResponseData<String> customException(CustomException e) {
    return ResponseData.failMsg(e.getMessage());
  }


  /**
   * 捕捉前端alert异常
   *
   * @param e
   * @return
   */
  @ExceptionHandler(AlertException.class)
  public ResponseData<String> alertException(AlertException e) {
    return ResponseData.failAlertMsg(e.getMessage());
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
    String message = e.getMessage();
    if (message.contains("io.undertow.server.handlers.form.MultiPartParserDefinition$FileTooLargeException")) {
      //文件过大
      return ResponseData.failMsg(String.format("文件过大，文件最大为%s", maxFileSize));
    }
    e.printStackTrace();
    return ResponseData.failMsg(message);
  }


}

