package com.aswkj.admin.api.common.enums;

/**
 * 响应信息枚举
 * @author hzb
 *
 */
public enum ResponseMsgEnum {
	
	SUCCESS(0, "SUCCESS"),
	FAIL(1, "FAIL"),


	USERNAME__PASSWORD_ERROR(40000, "用户名或密码错误"),
	UN_AUTHORIZED(40001, "未认证"),
	INVALID_TOKEN(40002, "非法token"),
	EXPIRED_TOKEN(40003, "token超时"),
	ACCESS_FORBIDDEN(40004, "无权限");

	private Integer code;
	private String msg;
	
	ResponseMsgEnum(Integer code, String msg) {
		this.code=code;
		this.msg=msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
