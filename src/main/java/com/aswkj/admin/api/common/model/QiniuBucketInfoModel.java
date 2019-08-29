package com.aswkj.admin.api.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "QiniuConfigModel对象", description = "")
public class QiniuBucketInfoModel implements Serializable {

  @ApiModelProperty(value = "空间所属区域")
  private String region;

  @ApiModelProperty(value = "访问域名")
  private List<String> domains;


}


