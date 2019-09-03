package com.aswkj.admin.api.module.pms.model;

import com.aswkj.admin.api.module.pms.enums.GenderEnum;
import com.aswkj.admin.api.module.pms.enums.UserStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
@ApiModel(value="User对象", description="")
public class UserModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id主键")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String name;

    @ApiModelProperty(value = "状态 -1:逻辑删除 0:禁用 1:启用 2:离职 ")
    private UserStatusEnum status;

    @ApiModelProperty(value = "头像id")
    private String avatarId;

    @ApiModelProperty(value = "头像链接")
    private String avatar;

    @ApiModelProperty(value = "性别")
    private GenderEnum gender;

    @ApiModelProperty(value = "生日")
    private LocalDate birthday;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "角色列表")
    private List<String> roles = new ArrayList<>();

    @ApiModelProperty(value = "角色中文名称列表")
    private List<String> roleCnNames = new ArrayList<>();

}
