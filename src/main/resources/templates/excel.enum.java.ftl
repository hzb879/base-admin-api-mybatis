package ${cfg.module_package}.enums.excel;

import ${cfg.base_package}.common.enums.StandardEnumInterface;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ${entity}ExcelEnum implements StandardEnumInterface {

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.propertyName != "id" && field.propertyName != "createUser" && field.propertyName != "updateUser" && field.propertyName != "updateTime">
  ${(field.name)?upper_case}("${field.propertyName}", "${(field.comment)!''}", "auto"),
    </#if>
</#list>
<#------------  END 字段循环遍历  ---------->
  ;

  private String dbValue;

  @JsonValue
  private String displayValue;

  private String width;

  ${entity}ExcelEnum(String dbValue, String displayValue, String width) {
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
