package com.aswkj.admin.api.module.pms.model;

import com.aswkj.admin.api.module.pms.entity.Avatar;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author hzb
 * @since 2019-08-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "UserMediaModel对象", description = "")
public class UserMediaModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "id主键")
  private String id;

  @ApiModelProperty(value = "用户名")
  private String username;

  private Avatar avatar;

}
