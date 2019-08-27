package com.aswkj.admin.api.generator;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.module.pms.entity.User;

import java.util.Collection;

public class MybatisCustomMapperSqlGenerator {

  public static void main(String[] args) {
    printCustomMapperSql(User.class, "u", "user");
  }


  private static void printCustomMapperSql(Class clazz, String dotPrefix, String underlinePrefix) {

    BeanDesc beanDesc = BeanUtil.getBeanDesc(clazz);
    Collection<BeanDesc.PropDesc> props = beanDesc.getProps();

    StringBuilder sb1 = new StringBuilder();
    StringBuilder sb2 = new StringBuilder();

    sb1.append("<!-- 自定义查询列 -->").append("\n");
    sb1.append("<sql id=\"CustomSelectColumn\">").append("\n");


    sb2.append("<!-- 自定义查询映射结果 -->").append("\n");
    sb2.append(StrUtil.format("<resultMap id=\"CustomBaseResultMap\" type=\"{}\">", ClassUtil.getClassName(clazz, false))).append("\n");
    sb2.append(StrUtil.format("<id column=\"{}_id\" property=\"id\"/>", underlinePrefix)).append("\n");

    for (BeanDesc.PropDesc prop : props) {

      String fieldName = prop.getFieldName();
      String underlineCase = StrUtil.toUnderlineCase(fieldName);
      sb1.append(StrUtil.format("{}.{} AS {}_{},", dotPrefix, underlineCase, underlinePrefix, underlineCase)).append("\n");
      if (!("id").equals(fieldName)) {
        sb2.append(StrUtil.format("<result column=\"{}_{}\" property=\"{}\"/>", underlinePrefix, underlineCase, fieldName)).append("\n");
      }
    }

    sb1.append("</sql>").append("\n");
    sb2.append("</resultMap>").append("\n");

    System.out.println(sb1.toString());
    System.out.println(sb2.toString());

  }
}
