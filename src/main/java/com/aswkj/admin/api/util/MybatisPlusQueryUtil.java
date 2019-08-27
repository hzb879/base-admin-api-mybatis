package com.aswkj.admin.api.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.common.BaseQueryParams;
import com.aswkj.admin.api.common.constant.Constants;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;

public class MybatisPlusQueryUtil {

  public static PageQueryBuilder pageQueryBuilder(BaseQueryParams baseQueryParams) {
    return new PageQueryBuilder(baseQueryParams);
  }

  public static class PageQueryBuilder<T> {
    private BaseQueryParams baseQueryParams;
    private QueryWrapper<T> queryWrapper;
    private Map<String, String> fieldNamespaceMap;


    private PageQueryBuilder(BaseQueryParams baseQueryParams) {
      this.baseQueryParams = baseQueryParams;
      this.queryWrapper = new QueryWrapper<>();
      this.fieldNamespaceMap = new HashMap<>();
    }


    public class FieldNamespace {
      private String nameSpace;

      public FieldNamespace(String nameSpace) {
        this.nameSpace = nameSpace;
      }

      public FieldNamespace addMap(String fieldName) {
        fieldNamespaceMap.put(fieldName, nameSpace + "." + fieldName);
        return this;
      }

      public FieldNamespace addMap(String fieldName, String columnName) {
        fieldNamespaceMap.put(fieldName, nameSpace + "." + columnName);
        return this;
      }
    }

    public FieldNamespace configFieldNamespace(String nameSpace) {
      return new FieldNamespace(nameSpace);
    }

    public Page buildPage() {
      if (Objects.isNull(baseQueryParams)) {
        return new Page(1, Constants.DEFAULT_PAGE_SIZE);
      }
      List<OrderItem> items = getOrderItems();
      Page page = new Page(Objects.isNull(baseQueryParams.pageNum) ? 1 : baseQueryParams.pageNum,
              Objects.isNull(baseQueryParams.pageSize) ? Constants.DEFAULT_PAGE_SIZE : baseQueryParams.pageSize);
      if (CollUtil.isNotEmpty(items)) {
        page.setOrders(items);
      }
      return page;
    }

    private List<OrderItem> getOrderItems() {
      List<OrderItem> items = null;
      if (!Objects.isNull(baseQueryParams) && CollUtil.isNotEmpty(baseQueryParams.orderItems)) {
        items = new ArrayList<>();
        for (BaseQueryParams.OrderItem orderItem : baseQueryParams.orderItems) {
          String column = orderItem.column;
          if (StrUtil.isNotBlank(column)) {
            column = StrUtil.toUnderlineCase(getColumnName(column, column));
            if (orderItem.direction != null) {
              switch (orderItem.direction) {
                case "desc":
                  items.add(OrderItem.desc(column));
                  break;
                default:
                  items.add(OrderItem.asc(column));
                  break;
              }
            } else {
              items.add(OrderItem.asc(column));
            }
          }
        }
      }
      return items;
    }

    public QueryWrapper buildQueryWrapper() {
      return this.queryWrapper;
    }

    private static class Pair {
      private String columnName;
      private Object fieldValue;

      public Pair(String columnName, Object fieldValue) {
        this.columnName = columnName;
        this.fieldValue = fieldValue;
      }

      public String getColumnName() {
        return columnName;
      }

      public Object getFieldValue() {
        return fieldValue;
      }
    }

    private void queryHandle(String fieldName, String columnName, java.util.function.Consumer<Pair> queryCondition) {
      if (Objects.nonNull(baseQueryParams)) {
        Object fieldValue = BeanUtil.getFieldValue(baseQueryParams, fieldName);
        if (Collection.class.isInstance(fieldValue) && !((Collection) fieldValue).isEmpty()
                || Objects.nonNull(fieldValue) && StrUtil.isNotBlank(fieldValue.toString())) {
          columnName = getColumnName(fieldName, columnName);
          queryCondition.accept(new Pair(StrUtil.toUnderlineCase(columnName), fieldValue));
        }
      }
    }

    private String getColumnName(String fieldName, String columnName) {
      return fieldNamespaceMap.containsKey(fieldName) ? fieldNamespaceMap.get(fieldName) : columnName;
    }


    public PageQueryBuilder addEqQuery(String fieldName) {
      return addEqQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addEqQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.eq(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }

    public PageQueryBuilder addNotEqQuery(String fieldName) {
      return addNotEqQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addNotEqQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.ne(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }

    public PageQueryBuilder addGeQuery(String fieldName) {
      return addGeQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addGeQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.ge(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }

    public PageQueryBuilder addLeQuery(String fieldName) {
      return addLeQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addLeQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.le(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }

    public PageQueryBuilder addLtQuery(String fieldName) {
      return addLtQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addLtQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.lt(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }

    public PageQueryBuilder addGtQuery(String fieldName) {
      return addGtQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addGtQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.gt(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }


    public PageQueryBuilder addLikeQuery(String fieldName) {
      return addLikeQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addLikeQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.like(pair.getColumnName(), pair.getFieldValue()));
      return this;
    }


    public PageQueryBuilder addInQuery(String fieldName) {
      return addInQuery(fieldName, fieldName);
    }

    public PageQueryBuilder addInQuery(String fieldName, String columnName) {
      queryHandle(fieldName,
              columnName,
              pair -> queryWrapper.in(pair.getColumnName(), (Collection<?>) pair.getFieldValue()));
      return this;
    }

    public PageQueryBuilder addKeyWorldQuery(String fieldName, String... columnNames) {
      if (Objects.nonNull(baseQueryParams)) {
        Object fieldValue = BeanUtil.getFieldValue(baseQueryParams, fieldName);
        if (Objects.nonNull(fieldValue) && StrUtil.isNotBlank(fieldValue.toString())) {
          this.queryWrapper.and(q -> {
            Arrays.stream(columnNames)
                    .map(columnName -> getColumnName(columnName, columnName))
                    .forEach(column -> q.like(StrUtil.toUnderlineCase(column), fieldValue).or());
            return q;
          });
        }
      }
      return this;
    }

    public PageQueryBuilder activeListOrderQuery() {
      List<OrderItem> items = getOrderItems();
      if (CollUtil.isNotEmpty(items)) {
        for (OrderItem item : items) {
          if (item.isAsc()) {
            this.queryWrapper.orderByAsc(item.getColumn());
          } else {
            this.queryWrapper.orderByDesc(item.getColumn());
          }
        }
      }
      return this;
    }

  }
}
