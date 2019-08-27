package com.aswkj.admin.api.module.pms.entity;

import com.aswkj.admin.api.common.enums.MediaStoreTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author hzb
 * @since 2019-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="Avatar对象", description="")
public class Avatar implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id主键")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "存储类型，本地：local,七牛：qiniu")
    private MediaStoreTypeEnum storeType;

    @ApiModelProperty(value = "(七牛)存储空间标识")
    private String bucket;

    @ApiModelProperty(value = "(七牛)存储空间bucket下的文件唯一标识")
    private String key;

    @ApiModelProperty(value = "文件原始名称")
    private String filename;

    @ApiModelProperty(value = "访问链接")
    private String src;

    @ApiModelProperty(value = "访问域名")
    private String domain;

    @ApiModelProperty(value = "本地访问url_path前缀")
    private String localUrlNamespace;

    @ApiModelProperty(value = "本地存储根目录")
    private String localRootDirectory;

    @ApiModelProperty(value = "本地存储根目录下的文件全路径")
    private String localFileKey;

    @ApiModelProperty(value = "记录创建者")
    @TableField(fill = FieldFill.INSERT)
    private String createUser;

    @ApiModelProperty(value = "记录最后更新者")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
