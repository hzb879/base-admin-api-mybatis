package com.aswkj.admin.api.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 七牛存储区域枚举
 *
 * @author hzb
 */
public enum QiniuRegionEnum implements StandardEnumInterface {

  huadong("z0", "华东"),
  huabei("z1", "华北"),
  huanan("z2", "华南"),
  beimei("na0", "北美"),
  xinjiapo("as0", "东南亚");


  private String dbValue;

  @JsonValue
  private String displayValue;

  QiniuRegionEnum(String dbValue, String displayValue) {
    this.dbValue = dbValue;
    this.displayValue = displayValue;
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
}
