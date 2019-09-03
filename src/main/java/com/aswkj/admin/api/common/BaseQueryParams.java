package com.aswkj.admin.api.common;

import java.util.List;

public class BaseQueryParams {
  public Integer pageNum;
  public Integer pageSize;
  public List<OrderItem> orderItems;

  public static class OrderItem {
    public String column;
    public String direction;
  }

}
