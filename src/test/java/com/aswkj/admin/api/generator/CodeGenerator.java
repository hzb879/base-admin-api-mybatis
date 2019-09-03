package com.aswkj.admin.api.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeGenerator {

  public static void main(String[] args) {
    // 代码生成器
    generateCode("cms",
            new String[]{
                    "product"
            });

  }

  public static void generateCode(String moduleName, String[] tableNames) {
    // 代码生成器
    AutoGenerator mpg = new AutoGenerator();

    String projectPath = System.getProperty("user.dir") + "/aswkj-api";
    String basePackage = "com.aswkj.admin.api.module";
    String author = "hzb";
    //全局配置
    mpg.setGlobalConfig(getGlobalConfig(projectPath, author));
    // 数据源配置
    mpg.setDataSource(getDataSourceConfig());
    //包配置
    mpg.setPackageInfo(getPackageConfig(moduleName, basePackage));
    //自定义模版配置
    mpg.setCfg(getInjectionConfig(projectPath, moduleName, basePackage));

    // 配置模板
    TemplateConfig templateConfig = new TemplateConfig();
    templateConfig.setXml(null);
    templateConfig.setController(null);
    mpg.setTemplate(templateConfig);

    // 策略配置
    mpg.setStrategy(getStrategyConfig(moduleName, tableNames));

    mpg.setTemplateEngine(new FreemarkerTemplateEngine());
    mpg.execute();
  }

  private static DataSourceConfig getDataSourceConfig() {
    DataSourceConfig dsc = new DataSourceConfig();
    dsc.setUrl("jdbc:mysql://localhost:3306/aswkj?useUnicode=true&characterEncoding=utf-8&useSSL=false");
    // dsc.setSchemaName("public");
    dsc.setDriverName("com.mysql.cj.jdbc.Driver");
    dsc.setUsername("root");
    dsc.setPassword("password");
    return dsc;
  }


  private static StrategyConfig getStrategyConfig(String moduleName, String[] tableNames) {
    StrategyConfig strategy = new StrategyConfig();
    strategy.setNaming(NamingStrategy.underline_to_camel);
    strategy.setColumnNaming(NamingStrategy.underline_to_camel);

//    strategy.setSuperEntityClass("com.aswkj.admin.api.common.BaseEntity");
//     公共父类
//    strategy.setSuperControllerClass("com.aswkj.adminapi.common.BaseController");
//     写于父类中的公共字段
//    strategy.setSuperEntityColumns("id");

    List<TableFill> tableFills = new ArrayList<>();
    tableFills.add(new TableFill("create_time", FieldFill.INSERT));
    tableFills.add(new TableFill("create_user", FieldFill.INSERT));
    tableFills.add(new TableFill("update_time", FieldFill.INSERT_UPDATE));
    tableFills.add(new TableFill("update_user", FieldFill.INSERT_UPDATE));
    strategy.setTableFillList(tableFills);

    strategy.setEntityLombokModel(true);
    strategy.setRestControllerStyle(true);

    strategy.setInclude(tableNames);
    strategy.setControllerMappingHyphenStyle(true);
    strategy.setTablePrefix(moduleName + "_");
    return strategy;
  }

  private static InjectionConfig getInjectionConfig(String projectPath, String moduleName, String basePackage) {
    String modulePackage = basePackage + "." + moduleName;

    // 自定义配置
    InjectionConfig cfg = new InjectionConfig() {
      @Override
      public void initMap() {
        // 自定义属性注入 通过${cfg.*} 获取
        Map<String, Object> map = new HashMap<>();
        map.put("base_package", basePackage.replaceAll("\\.module", ""));
        map.put("module_package", modulePackage);
        this.setMap(map);

      }
    };

    // 自定义输出配置
    List<FileOutConfig> focList = new ArrayList<>();

    // 自定义mapper.xml模版
    String xmlMapperTemplatePath = "/templates/mapper.xml.ftl";
    focList.add(new FileOutConfig(xmlMapperTemplatePath) {
      @Override
      public String outputFile(TableInfo tableInfo) {
        return projectPath + "/src/main/resources/mapper/" + moduleName
                + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
      }
    });

    // 自定义controller代码模板
    String ControllerTemplatePath = "/templates/controller.java.ftl";
    focList.add(new FileOutConfig(ControllerTemplatePath) {
      @Override
      public String outputFile(TableInfo tableInfo) {
        String entityFile = projectPath
                + "/src/main/java/"
                + modulePackage.replaceAll("\\.", "/")
                + "/controller/" + tableInfo.getControllerName()
                + ".java";
        return entityFile;
      }
    });


    // 自定义vo代码模板
    String VoTemplatePath = "/templates/vo.java.ftl";
    focList.add(new FileOutConfig(VoTemplatePath) {
      @Override
      public String outputFile(TableInfo tableInfo) {
        String entityFile = projectPath
                + "/src/main/java/"
                + modulePackage.replaceAll("\\.", "/")
                + "/vo/" + tableInfo.getEntityName()
                + "Vo.java";
        return entityFile;
      }
    });

    // 自定义ExcelEnum代码模板
    String ExcelEnumTemplatePath = "/templates/excel.enum.java.ftl";
    focList.add(new FileOutConfig(ExcelEnumTemplatePath) {
      @Override
      public String outputFile(TableInfo tableInfo) {
        String entityFile = projectPath
                + "/src/main/java/"
                + modulePackage.replaceAll("\\.", "/")
                + "/enums/excel/" + tableInfo.getEntityName()
                + "ExcelEnum.java";
        return entityFile;
      }
    });

    cfg.setFileOutConfigList(focList);
    return cfg;
  }


  private static PackageConfig getPackageConfig(String moduleName, String basePackage) {
    // 包配置
    PackageConfig pc = new PackageConfig();
    pc.setModuleName(moduleName);
    pc.setParent(basePackage);
    return pc;
  }

  private static GlobalConfig getGlobalConfig(String projectPath, String author) {
    GlobalConfig gc = new GlobalConfig();
    gc.setOutputDir(projectPath + "/src/main/java");
    gc.setBaseColumnList(true);
    gc.setBaseResultMap(true);
    gc.setFileOverride(false);
    gc.setAuthor(author);
    gc.setOpen(false);
    gc.setSwagger2(true);
    gc.setIdType(IdType.ID_WORKER_STR);
    return gc;
  }


}