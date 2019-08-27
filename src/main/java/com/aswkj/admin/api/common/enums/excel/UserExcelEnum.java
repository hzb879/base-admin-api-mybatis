package com.aswkj.admin.api.common.enums.excel;

import com.aswkj.admin.api.common.enums.StandardEnumInterface;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserExcelEnum implements StandardEnumInterface {

  username("username", "登陆账号", "auto"),
  name("name", "昵称", "auto"),
  roleCnNames("roleCnNames", "角色名称", "auto"),
  status("status", "状态", "auto"),
  gender("gender", "性别", "auto"),
  birthday("birthday", "生日", "auto"),
  mobile("mobile", "手机号码", "auto"),
  email("email", "邮箱", "auto"),
  description("description", "描述", "10"),
  createTime("createTime", "创建时间", "auto"),
  lastLoginTime("lastLoginTime", "最后登陆时间", "auto"),
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
