package com.aswkj.admin.api.common.controller.excel;

import com.aswkj.admin.api.common.constant.Constants;
import com.aswkj.admin.api.common.enums.excel.UserExcelEnum;
import com.aswkj.admin.api.common.util.CommonExcelUtil;
import com.aswkj.admin.api.module.pms.service.IUserService;
import com.aswkj.admin.api.module.pms.vo.UserExcelVo;
import com.aswkj.admin.api.util.MybatisPlusQueryUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "1.0.0", value = "excel文件处理")
@RestController
@RequestMapping(value = "/excel")
public class ExcelController {


  @Autowired
  IUserService userService;

  private static class UserParams extends BaseExcelParams {
    public String name;
    public String createTimeAfter;
    public String createTimeBefore;
    public Integer status;
    public Integer gender;
  }


  @ApiOperation(value = "下载用户excel列表", notes = "备注")
  @PostMapping("/user/download")
  public void userDownload(@RequestBody UserParams userParams, HttpServletResponse response) throws IOException {
    MybatisPlusQueryUtil.PageQueryBuilder pageQueryBuilder = MybatisPlusQueryUtil.pageQueryBuilder(userParams);

    pageQueryBuilder.configFieldNamespace("u")
            .addMap("name")
            .addMap("createTime")
            .addMap("status")
            .addMap("gender")
            .addMap("createTimeAfter", "createTime")
            .addMap("createTimeBefore", "createTime");

    pageQueryBuilder.addLikeQuery("name")
            .addEqQuery("status")
            .addEqQuery("gender")
            .addGeQuery("createTimeAfter", "createTime")
            .addLeQuery("createTimeBefore", "createTime")
            .activeListOrderQuery();


    QueryWrapper queryWrapper = pageQueryBuilder.buildQueryWrapper();
    //默认不展示超级管理员
    queryWrapper.ne("u.username", Constants.SUPER_ADMIN_NAME);
    //解决需要排序的sql语法问题
    queryWrapper.groupBy("u.id");
    List<UserExcelVo> userExcelVoList = userService.getUserExcelVoList(queryWrapper);

    CommonExcelUtil.exportExcel(response, UserExcelEnum.class, userParams.fieldNames, userExcelVoList, "用户");


  }
}
