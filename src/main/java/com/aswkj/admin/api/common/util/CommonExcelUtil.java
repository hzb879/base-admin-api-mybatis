package com.aswkj.admin.api.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.*;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.exceptions.POIException;
import com.aswkj.admin.api.common.enums.StandardEnumInterface;
import com.aswkj.admin.api.config.exception.AlertException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CommonExcelUtil {


  private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
  private static final DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT);
  private static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
  private static final char[] COLUMN_CHAR = {
          'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
          'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
          'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
          'Y', 'Z'
  };

  public static void exportExcel(HttpServletResponse response,
                                 Class<? extends Enum> fieldNamesEnumClazz,
                                 List<String> fieldNames,
                                 List<? extends Object> data,
                                 String fileName) throws IOException {
    Assert.notEmpty(data, "没有数据可导出！");
    Assert.notEmpty(fieldNames, "请至少设置一个导出属性！");

    try (ExcelWriter writer = ExcelUtil.getWriter();
         ServletOutputStream out = response.getOutputStream()) {


      List<String> header = CollUtil.newArrayList();
      List<String> widths = CollUtil.newArrayList();
      List<Function<Object, String>> fieldValueFormatters = new ArrayList<>();

      Class dataClazz = data.get(0).getClass();

      for (String fieldName : fieldNames) {
        Enum column = EnumUtil.likeValueOf(fieldNamesEnumClazz, fieldName);
        header.add(BeanUtil.getFieldValue(column, "displayValue").toString());
        widths.add(BeanUtil.getFieldValue(column, "width").toString());
        fieldValueFormatters.add(getFieldValueFormatter(dataClazz, fieldName));
      }

      writer.writeRow(header);

      List<List<String>> rows = new ArrayList<>();
      for (Object o : data) {
        List<String> row = new ArrayList<>();
        for (int i = 0; i < fieldNames.size(); i++) {
          Object fieldValue = BeanUtil.getFieldValue(o, fieldNames.get(i));
          if (Objects.nonNull(fieldValue)) {
            row.add(fieldValueFormatters.get(i).apply(fieldValue));
          }
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

  private static Function<Object, String> getFieldValueFormatter(Class dataClazz, String fieldName) {
    Field field = ReflectUtil.getField(dataClazz, fieldName);
    Class fieldType = field.getType();
    if (LocalDateTime.class.isAssignableFrom(fieldType)) {
      return fieldValue -> DEFAULT_DATE_TIME_FORMATTER.format((TemporalAccessor) fieldValue);
    } else if (LocalDate.class.isAssignableFrom(fieldType)) {
      return fieldValue -> DEFAULT_DATE_FORMATTER.format((TemporalAccessor) fieldValue);
    } else if (LocalTime.class.isAssignableFrom(fieldType)) {
      return fieldValue -> DEFAULT_TIME_FORMATTER.format((TemporalAccessor) fieldValue);
    } else if (Date.class.isAssignableFrom(fieldType)) {
      return fieldValue -> new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).format((Date) fieldValue);
    } else if (Collection.class.isAssignableFrom(fieldType)) {
      return fieldValue -> CollUtil.join((Collection) fieldValue, ",");
    } else if (StandardEnumInterface.class.isAssignableFrom(fieldType)) {
      return fieldValue -> BeanUtil.getFieldValue(fieldValue, "displayValue").toString();
    } else {
      return fieldValue -> fieldValue.toString();
    }
  }


  public static <T> List<T> importExcel(InputStream in, Class<T> resultClazz, Class fieldNamesEnumClazz) {

    List<T> resultList = new ArrayList<>();

    Map<String, BiConsumer<Object, Object>> fillFieldValueHandlerMap = new HashMap<>();

    List<String> headerList = EnumUtil.getFieldValues(fieldNamesEnumClazz, "displayValue");
    HashSet<String> headerSet = CollUtil.newHashSet(headerList);

    try {
      ExcelUtil.readBySax(in, -1, (sheetIndex, rowIndex, rowList) -> {
        if (rowIndex == 0) { //标题处理
          CommonUtil.isFalseAlertAssert(ObjectUtil.isEmpty(rowList), "第{}页,第1行不能为空，且必须为标题！", getShowNumber(sheetIndex));
          for (int i = 0; i < rowList.size(); i++) {
            Object headerColumn = rowList.get(i);
            if (Objects.nonNull(headerColumn)) {
              String headerStr = headerColumn.toString();
              if (headerSet.contains(headerStr)) {
                Object fieldName = CommonUtil.getEnumFieldValue(fieldNamesEnumClazz, headerStr, "dbValue");
                BiConsumer<Object, Object> fillFieldValueHandler = getFillFieldValueHandler(resultClazz, fieldName.toString());
                if (Objects.nonNull(fillFieldValueHandler)) {
                  fillFieldValueHandlerMap.put(getKey(sheetIndex, i), fillFieldValueHandler);
                }
              }
            }
          }
          CommonUtil.isFalseAlertAssert(fillFieldValueHandlerMap.isEmpty(),
                  "第{}页,第{}行必须为标题, 且至少包含 {} 这些标题中的一个",
                  getShowNumber(sheetIndex), getShowNumber(rowIndex), CollUtil.join(headerList, ","));
          return;
        }

        if (Objects.isNull(rowList) || rowList.stream().allMatch(o -> Objects.isNull(o) || StrUtil.isBlank(o.toString()))) {
          //跳过空行
          return;
        }

        T t = ReflectUtil.newInstance(resultClazz);
        boolean atLeastOnePropertySet = false;
        for (int columnIndex = 0; columnIndex < rowList.size(); columnIndex++) {
          String key = getKey(sheetIndex, columnIndex);
          if (!fillFieldValueHandlerMap.containsKey(key)) {
            continue;
          }

          Object cell = rowList.get(columnIndex);
          if (Objects.nonNull(cell) && StrUtil.isNotBlank(cell.toString())) {
            try {
              atLeastOnePropertySet = true;
              fillFieldValueHandlerMap.get(key).accept(t, cell);
            } catch (Exception e) {
              CommonUtil.isTrueAlertAssert(false,
                      "第{}页,第{}行，第{}({})列 的 {} 格式错误，请修改至正确才能导入成功！",
                      getShowNumber(sheetIndex),
                      getShowNumber(rowIndex),
                      getShowNumber(columnIndex),
                      columnIndex < COLUMN_CHAR.length ? COLUMN_CHAR[columnIndex] : "",
                      cell);
            }
          }
        }
        if (atLeastOnePropertySet) {
          resultList.add(t);
        }
      });
    } catch (POIException e) {
      //由于readBySax的rowHandler里抛出的异常被捕获了并包装成了POIException，故在此重新抛出原本想要抛出的异常
      throw new AlertException(Objects.nonNull(e.getCause()) ? e.getCause().getMessage() : e.getMessage());
    } catch (Exception e) {
      throw e;
    }

    return resultList;
  }

  private static int getShowNumber(int index) {
    return index + 1;
  }

  private static String getKey(int sheetIndex, int columnIndex) {
    return sheetIndex + "-" + columnIndex;
  }

  private static <T> BiConsumer<Object, Object> getFillFieldValueHandler(Class<T> objClazz, String fieldName) {
    Field field = ReflectUtil.getField(objClazz, fieldName);
    if (Objects.isNull(field)) {
      return null;
    }
    Class fieldType = field.getType();
    if (LocalDateTime.class.isAssignableFrom(fieldType)) {
      return (obj, fieldValue) -> ReflectUtil.setFieldValue(obj, field, LocalDateTime.parse(fieldValue.toString(), DEFAULT_DATE_TIME_FORMATTER));
    } else if (LocalDate.class.isAssignableFrom(fieldType)) {
      return (obj, fieldValue) -> ReflectUtil.setFieldValue(obj, field, LocalDate.parse(fieldValue.toString(), DEFAULT_DATE_FORMATTER));
    } else if (LocalTime.class.isAssignableFrom(fieldType)) {
      return (obj, fieldValue) -> ReflectUtil.setFieldValue(obj, field, LocalTime.parse(fieldValue.toString(), DEFAULT_TIME_FORMATTER));
    } else if (Date.class.isAssignableFrom(fieldType)) {
      return (obj, fieldValue) -> {
        try {
          ReflectUtil.setFieldValue(obj, field, new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT).parse(fieldValue.toString()));
        } catch (ParseException e) {
          throw new AlertException(e.getMessage());
        }
      };
    } else if (StandardEnumInterface.class.isAssignableFrom(fieldType)) {
      return (obj, fieldValue) -> {
        Enum value = EnumUtil.likeValueOf(fieldType, fieldValue);
        CommonUtil.isFalseAlertAssert(Objects.isNull(value), "");
        ReflectUtil.setFieldValue(obj, field, value);
      };
    } else {
      return (obj, fieldValue) -> ReflectUtil.setFieldValue(obj, field, fieldValue);
    }
  }


}
