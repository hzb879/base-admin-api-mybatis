package ${package.Controller};

import cn.hutool.core.lang.Assert;
import ${cfg.base_package}.common.BaseQueryParams;
import ${cfg.base_package}.common.response.ResponseData;
import ${cfg.base_package}.util.MybatisPlusQueryUtil;
import ${cfg.module_package}.entity.${entity};
import ${cfg.module_package}.service.${table.serviceName};

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(tags = "1.0.0", value = "${table.comment!}管理")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

<#assign my_service="${(table.serviceName?substring(1))?uncap_first}" />
<#assign my_entity_name="${entity?uncap_first}" />
  @Autowired
  private ${table.serviceName} ${my_service};

  private static class QueryParams extends BaseQueryParams {

  }

  @ApiOperation(value = "根据条件分页获取数据", notes = "备注")
  @PostMapping("/page")
  public ResponseData<IPage<${entity}>> getPage(@RequestBody(required = false) QueryParams queryParams) {
    MybatisPlusQueryUtil.PageQueryBuilder pageQueryBuilder = MybatisPlusQueryUtil.pageQueryBuilder(queryParams);


    Page page = pageQueryBuilder.buildPage();
    QueryWrapper queryWrapper = pageQueryBuilder.buildQueryWrapper();

    return ResponseData.success(${my_service}.page(page, queryWrapper));
  }


  @ApiOperation(value = "保存数据", notes = "备注")
  @PostMapping
  public ResponseData<${entity}> save(@RequestBody ${entity} ${my_entity_name}) {
    ${my_entity_name}.setId(null)
              .setCreateTime(null)
              .setUpdateTime(null)
              .setCreateUser(null)
              .setUpdateUser(null);
    ${my_service}.save(${my_entity_name});
    return ResponseData.success(${my_entity_name});
  }


  @ApiOperation(value = "修改数据", notes = "备注")
  @PostMapping("/update")
  public ResponseData update(@RequestBody ${entity} ${my_entity_name}) {
    Assert.notBlank(${my_entity_name}.getId(), "id不能为空！");
    ${my_service}.updateById(new ${entity}()
    .setId(${my_entity_name}.getId()));
    return ResponseData.successSign();
  }

  @ApiOperation(value = "删除数据", notes = "备注")
  @PostMapping("/remove")
  public ResponseData remove(@RequestParam String id) {
    Assert.notBlank(id, "id不能为空！");
    ${my_service}.removeById(id);
    return ResponseData.successSign();
  }

}
</#if>
