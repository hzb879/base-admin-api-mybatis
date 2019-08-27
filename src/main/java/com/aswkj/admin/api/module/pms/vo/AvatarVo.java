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
@ApiModel(value="Avatar视图对象", description="")
public class AvatarVo implements Serializable {

  @ApiModelProperty(value = "id主键")
  private String id;

  @ApiModelProperty(value = "访问链接")
  private String src;


}
