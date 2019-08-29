package com.aswkj.admin.api.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "QiniuStoreModel对象", description = "")
public class QiniuImageStoreModel implements Serializable {

  @ApiModelProperty(value = "存储空间标识")
  private String bucket;

  @ApiModelProperty(value = "存储空间bucket下的文件唯一标识")
  private String key;

  @ApiModelProperty(value = "文件hash值")
  private String hash;

  @ApiModelProperty(value = "图片宽度")
  private String width;

  @ApiModelProperty(value = "图片高度")
  private String height;

  @ApiModelProperty(value = "图片大小")
  private String fileSize;

  @ApiModelProperty(value = "文件原始名称")
  private String filename;

  @ApiModelProperty(value = "资源类型，例如JPG图片的资源类型为image/jpg")
  private String mimeType;

  @ApiModelProperty(value = "访问链接")
  private String src;

  @ApiModelProperty(value = "访问域名")
  private String domain;


}


