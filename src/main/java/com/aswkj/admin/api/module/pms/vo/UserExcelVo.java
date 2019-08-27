package com.aswkj.admin.api.module.pms.vo;

import com.aswkj.admin.api.common.enums.GenderEnum;
import com.aswkj.admin.api.common.enums.UserStatusEnum;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "用户excel视图对象", description = "")
public class UserExcelVo implements Serializable {

  @ApiModelProperty(value = "用户名")
  private String username;

  @ApiModelProperty(value = "用户昵称")
  private String name;

  @ApiModelProperty(value = "状态 -1:逻辑删除 0:禁用 1:启用 2:离职 ")
  private UserStatusEnum status;

  @ApiModelProperty(value = "性别")
  private GenderEnum gender;

  @ApiModelProperty(value = "生日")
  private LocalDate birthday;

  @ApiModelProperty(value = "手机号码")
  private String mobile;

  @ApiModelProperty(value = "邮箱")
  private String email;

  @ApiModelProperty(value = "描述信息")
  private String description;

  @ApiModelProperty(value = "最后登陆时间")
  private LocalDateTime lastLoginTime;

  @ApiModelProperty(value = "创建时间")
  @TableField(fill = FieldFill.INSERT)
  private LocalDateTime createTime;

  @ApiModelProperty(value = "角色中文名称列表，逗号分割")
  private String roleCnNames;

}
