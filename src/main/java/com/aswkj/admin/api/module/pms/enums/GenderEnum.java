package com.aswkj.admin.api.module.pms.enums;

import com.aswkj.admin.api.common.enums.StandardEnumInterface;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 性别枚举
 *
 * @author hzb
 */
public enum GenderEnum implements StandardEnumInterface {

  MALE(0, "男"),
  FEMALE(1, "女");


  @EnumValue
  private Integer dbValue;

  @JsonValue
  private String displayValue;

  GenderEnum(Integer dbValue, String displayValue) {
    this.dbValue = dbValue;
    this.displayValue = displayValue;
  }

//  @JsonCreator
//  public static GenderEnum getItem(Integer gender) {
//    for (GenderEnum item : values()) {
//      if (item.getDbValue().equals(gender)) {
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
