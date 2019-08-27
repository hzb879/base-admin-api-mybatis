package com.aswkj.admin.api.module.pms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Role视图对象", description="")
public class RoleVo implements Serializable {

  @ApiModelProperty(value = "id主键")
  private String id;

  @ApiModelProperty(value = "英文名称")
  private String name;

  @ApiModelProperty(value = "中文名称")
  private String cnName;


}
