package com.aswkj.admin.api.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 用户状态信息
 */
public enum UserStatusEnum implements StandardEnumInterface {
  //逻辑删除
  DELETE(-1, "逻辑删除"),
  //禁用
  DISABLE(0, "禁用"),
  //启用
  ENABLE(1, "在职"),
  //离职
  DIMISSION(2, "离职");

  @EnumValue
  private Integer dbValue;

  @JsonValue
  private String displayValue;

  UserStatusEnum(Integer dbValue, String displayValue) {
    this.dbValue = dbValue;
    this.displayValue = displayValue;
  }

//  @JsonCreator
//  public static UserStatusEnum getItem(Integer status) {
//    for (UserStatusEnum item : values()) {
//      if (item.getDbValue().equals(status)) {
//        return item;
//      }
//    }
//    return null;
//  }

  public Integer getDbValue() {
    return dbValue;
  }

  public void setDbValue(Integer dbValue) {
    this.dbValue = dbValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }

  public void setDisplayValue(String displayValue) {
    this.displayValue = displayValue;
  }


}
