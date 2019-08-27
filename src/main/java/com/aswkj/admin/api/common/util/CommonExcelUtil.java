package com.aswkj.admin.api.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.aswkj.admin.api.common.enums.StandardEnumInterface;
import com.aswkj.admin.api.common.enums.excel.UserExcelEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class CommonExcelUtil {


  public static void main(String[] args) {
    UserExcelEnum birthday = EnumUtil.likeValueOf(UserExcelEnum.class, "createTime");

    System.out.println(birthday);
  }

  private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
  private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

  private static String getStrValue(Object value) {
    if (Objects.nonNull(value)) {
      if (LocalDateTime.class.isInstance(value)) {
        return DEFAULT_DATE_TIME_FORMATTER.format((TemporalAccessor) value);
      } else if (LocalDate.class.isInstance(value)) {
        return DEFAULT_DATE_FORMATTER.format((TemporalAccessor) value);
      } else if (LocalTime.class.isInstance(value)) {
        return DEFAULT_TIME_FORMATTER.format((TemporalAccessor) value);
      } else if (Date.class.isInstance(value)) {
        return new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).format((Date) value);
      } else if (Collection.class.isInstance(value)) {
        return CollUtil.join((Collection) value, ",");
      } else if (StandardEnumInterface.class.isInstance(value)) {
        return BeanUtil.getFieldValue(value, "displayValue").toString();
      } else {
        return value.toString();
      }
    } else {
      return "";
    }
  }

  public static void exportExcel(HttpServletResponse response, Class<? extends Enum> fieldNamesEnumClazz, List<String> fieldNames, List<? extends Object> data, String fileName) throws IOException {
    Assert.notEmpty(data, "没有数据可导出！");
    Assert.notEmpty(fieldNames, "请至少设置一个导出属性！");

    try (ExcelWriter writer = ExcelUtil.getWriter();
         ServletOutputStream out = response.getOutputStream()) {


      List<String> header = CollUtil.newArrayList();
      List<String> widths = CollUtil.newArrayList();

      for (String fieldName : fieldNames) {
        Enum column = EnumUtil.likeValueOf(fieldNamesEnumClazz, fieldName);
        header.add(BeanUtil.getFieldValue(column, "displayValue").toString());
        widths.add(BeanUtil.getFieldValue(column, "width").toString());
      }

      writer.writeRow(header);

      List<List<String>> rows = new ArrayList<>();
      for (Object o : data) {
        List<String> row = new ArrayList<>();
        for (String fieldName : fieldNames) {
          row.add(getStrValue(BeanUtil.getFieldValue(o, fieldName)));
        }
        rows.add(row);
      }
      writer.write(rows);
      //设置列宽,注意需最后设置！
      for (int i = 0; i < fieldNames.size(); i++) {
        String width = widths.get(i);
        if ("auto".equals(width)) {
          writer.autoSizeColumn(i);
        } else {
          writer.setColumnWidth(i, Integer.parseInt(width));
        }
      }

      response.setContentType("application/vnd.ms-excel;charset=utf-8");
      //设置文件名
      String today = DEFAULT_DATE_FORMATTER.format(LocalDate.now());
      String outFilename = null;
      if (StrUtil.isNotBlank(fileName)) {
        outFilename = today + "-" + fileName;
      } else {
        outFilename = today;
      }
      outFilename = URLUtil.encode(outFilename, "utf-8");
      response.setHeader("Content-Disposition", StrUtil.format("attachment;filename={}.xls", outFilename));
      response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
      writer.flush(out, true);
    }

  }

}
