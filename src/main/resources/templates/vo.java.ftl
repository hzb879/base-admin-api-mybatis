package ${cfg.module_package}.vo;

import java.time.LocalDateTime;
import java.io.Serializable;
<#if swagger2>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if entityLombokModel>
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
</#if>

/**
 * <p>
 * ${table.comment!}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
</#if>
<#if swagger2>
@ApiModel(value="${entity}视图对象", description="${table.comment!}")
</#if>
public class ${entity}Vo implements Serializable {

<#if entitySerialVersionUID>
  private static final long serialVersionUID = 1L;
</#if>

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if field.propertyName != "createUser" && field.propertyName != "updateUser" && field.propertyName != "updateTime">
    <#if field.comment!?length gt 0>
        <#if swagger2>
  @ApiModelProperty(value = "${field.comment}")
        <#else>
  /**
   * ${field.comment}
   */
        </#if>
    </#if>
  private ${field.propertyType} ${field.propertyName};

</#if>
</#list>
<#------------  END 字段循环遍历  ---------->
<#if !entityLombokModel>
    <#list table.fields as field>
        <#if field.propertyType == "boolean">
            <#assign getprefix="is"/>
        <#else>
            <#assign getprefix="get"/>
        </#if>
  public ${field.propertyType} ${getprefix}${field.capitalName}() {
    return ${field.propertyName};
  }

    <#if entityBuilderModel>
  public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    <#else>
  public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    </#if>
    this.${field.propertyName} = ${field.propertyName};
        <#if entityBuilderModel>
    return this;
        </#if>
  }
    </#list>
</#if>

<#if !entityLombokModel>
  @Override
  public String toString() {
    return "${entity}{" +
    <#list table.fields as field>
        <#if field_index==0>
        "${field.propertyName}=" + ${field.propertyName} +
        <#else>
        ", ${field.propertyName}=" + ${field.propertyName} +
        </#if>
    </#list>
    "}";
  }
</#if>
}
