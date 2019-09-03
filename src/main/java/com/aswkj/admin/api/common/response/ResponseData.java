package com.aswkj.admin.api.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "通用API接口返回", description = "Common Api Response")
public class ResponseData<T> implements Serializable {

  @ApiModelProperty(value = "响应状态码")
  private Integer code;
  @ApiModelProperty(value = "响应信息描述")
  private String message;
  @ApiModelProperty(value = "响应数据")
  private T data;

  public static <T> ResponseData<T> success(T data) {
    ResponseMsgEnum success = ResponseMsgEnum.SUCCESS;
    return new ResponseData<T>(success.getCode(), success.getMsg(), data);
  }

  public static <T> ResponseData<T> fail(T data) {
    ResponseMsgEnum fail = ResponseMsgEnum.FAIL;
    return new ResponseData<T>(fail.getCode(), fail.getMsg(), data);
  }

  public static ResponseData<String> failMsg(String msg) {
    return new ResponseData<>(ResponseMsgEnum.FAIL.getCode(), msg, msg);
  }

  public static ResponseData<String> failAlertMsg(String msg) {
    return new ResponseData<>(ResponseMsgEnum.FAIL_ALERT.getCode(), msg, msg);
  }


  public static ResponseData<String> successSign() {
    ResponseMsgEnum success = ResponseMsgEnum.SUCCESS;
    return new ResponseData<>(success.getCode(), success.getMsg(), success.getMsg());
  }

  public static ResponseData<String> failSign() {
    ResponseMsgEnum fail = ResponseMsgEnum.FAIL;
    return new ResponseData<>(fail.getCode(), fail.getMsg(), fail.getMsg());
  }

  public static ResponseData<String> responseMsgEnum(ResponseMsgEnum responseMsgEnum) {
    return new ResponseData<String>(responseMsgEnum.getCode(), responseMsgEnum.getMsg(), responseMsgEnum.getMsg());
  }

  public static <T> ResponseData<T> responseMsgEnumData(ResponseMsgEnum responseMsgEnum, T data) {
    return new ResponseData<T>(responseMsgEnum.getCode(), responseMsgEnum.getMsg(), data);
  }

  public static ResponseData<String> codeMsg(Integer code, String message) {
    return new ResponseData<String>(code, message, message);
  }


}
