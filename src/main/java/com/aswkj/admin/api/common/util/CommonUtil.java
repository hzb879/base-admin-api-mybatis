package com.aswkj.admin.api.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.config.exception.AlertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonUtil {

  public static List<Map<String, Object>> getEnumDictList(Class<? extends Enum> clazz) {
    List<Map<String, Object>> result = new ArrayList<>();
    for (Enum v : clazz.getEnumConstants()) {
      result.add(
              MapUtil.<String, Object>builder()
                      .put("k", BeanUtil.getFieldValue(v, "dbValue"))
                      .put("v", BeanUtil.getFieldValue(v, "displayValue")).
                      build());
    }
    return result;
  }

  public static Object getEnumFieldValue(Class enumClazz, String likeValue, String fieldName) {
    Enum obj = EnumUtil.likeValueOf(enumClazz, likeValue);
    return BeanUtil.getFieldValue(obj, fieldName);
  }


  public static void isTrueAlertAssert(boolean expression, String errMsg, Object... params) {
    if (!expression) {
      throw new AlertException(StrUtil.format(errMsg, params));
    }
  }


  public static void isFalseAlertAssert(boolean expression, String errMsg, Object... params) {
    if (expression) {
      throw new AlertException(StrUtil.format(errMsg, params));
    }
  }
  
}
