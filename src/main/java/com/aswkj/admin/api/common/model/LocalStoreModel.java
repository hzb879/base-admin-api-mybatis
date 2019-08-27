package com.aswkj.admin.api.common.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "LocalUploadModel对象", description = "")
public class LocalStoreModel implements Serializable {

  @ApiModelProperty(value = "文件原始名称")
  private String filename;

  @ApiModelProperty(value = "访问链接")
  private String src;

  @ApiModelProperty(value = "访问域名")
  private String domain;

  @JsonIgnore
  @ApiModelProperty(value = "本地访问url_path前缀")
  private String localUrlNamespace;

  @JsonIgnore
  @ApiModelProperty(value = "本地存储根目录")
  private String localRootDirectory;

  @JsonIgnore
  @ApiModelProperty(value = "本地存储根目录下的文件全路径")
  private String localFileKey;


}


