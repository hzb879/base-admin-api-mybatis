package com.aswkj.admin.api.module.pms.enums.excel;

import com.aswkj.admin.api.common.enums.StandardEnumInterface;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserExcelEnum implements StandardEnumInterface {

  USERNAME("username", "登陆账号", "auto"),
  NAME("name", "昵称", "auto"),
  ROLE_CN_NAMES("roleCnNames", "角色名称", "auto"),
  STATUS("status", "状态", "auto"),
  GENDER("gender", "性别", "auto"),
  BIRTHDAY("birthday", "生日", "auto"),
  MOBILE("mobile", "手机号码", "auto"),
  EMAIL("email", "邮箱", "auto"),
  DESCRIPTION("description", "描述", "10"),
  CREATE_TIME("createTime", "创建时间", "auto"),
  LAST_LOGIN_TIME("lastLoginTime", "最后登陆时间", "auto"),
  ;


  private String dbValue;

  @JsonValue
  private String displayValue;

  private String width;

  UserExcelEnum(String dbValue, String displayValue, String width) {
    this.dbValue = dbValue;
    this.displayValue = displayValue;
    this.width = width;
  }

  public String getDbValue() {
    return dbValue;
  }

  public void setDbValue(String dbValue) {
    this.dbValue = dbValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }

  public void setDisplayValue(String displayValue) {
    this.displayValue = displayValue;
  }

  public String getWidth() {
    return width;
  }

  public void setWidth(String width) {
    this.width = width;
  }

}
