package com.aswkj.admin.api.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 媒体资源： 如，图片， 视频的存储类型
 *
 * @author hzb
 */
public enum MediaStoreTypeEnum implements StandardEnumInterface {

  QINIU("qiniu", "七牛"),
  LOCAL("local", "本地");


  @EnumValue
  private String dbValue;

  @JsonValue
  private String displayValue;

  MediaStoreTypeEnum(String dbValue, String displayValue) {
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
