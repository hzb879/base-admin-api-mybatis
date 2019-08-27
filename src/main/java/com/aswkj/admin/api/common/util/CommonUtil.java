package com.aswkj.admin.api.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;

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


}
