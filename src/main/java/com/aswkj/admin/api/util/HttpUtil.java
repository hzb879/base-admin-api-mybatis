package com.aswkj.admin.api.util;

import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HttpUtil {

  private static final String SERVER_IP = getMyServerIp();

  /**
   * 把http请求输入转化为字符串
   *
   * @param request
   * @return
   * @throws Exception
   */
  public static String getHttpInputStreamAsString(HttpServletRequest request) throws Exception {
    try (InputStream inStream = request.getInputStream();
         ByteArrayOutputStream outSteam = new ByteArrayOutputStream();) {
      byte[] buffer = new byte[1024];
      int len = 0;
      while ((len = inStream.read(buffer)) != -1) {
        outSteam.write(buffer, 0, len);
      }
      return outSteam.toString("utf-8");
    }
  }


  public static String getRequestDomain(HttpServletRequest request) {
    StringBuffer url = request.getRequestURL();
    return url.delete(url.length() - request.getRequestURI().length(), url.length())
            .append(request.getSession().getServletContext().getContextPath())
            .append("/").toString();
  }


  /**
   * 获取客户端真实ip
   *
   * @param request
   * @return
   */
  public static String getClientIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    // 如果是多级代理，那么取第一个ip为客户端ip
    if (ip != null && ip.indexOf(",") != -1) {
      ip = ip.substring(0, ip.indexOf(",")).trim();
    }
    return ip;
  }

  /**
   * 获取服务器端ip地址
   *
   * @return
   */
  public static String getServerIpAddress() {
    return SERVER_IP;
  }

  /**
   * 通过paramsMap取出其中的键值对来构造url查询参数 如?a=b&c=d
   *
   * @param params
   * @return
   */
  public static String createQueryParams(MultiValueMap<String, String> params) {
    StringBuilder sb = new StringBuilder("?");
    for (Map.Entry<String, List<String>> entry : params.entrySet()) {
      for (String val : entry.getValue()) {
        if (val == null || val.trim().equals("")) continue;
        sb.append(entry.getKey()).append("=").append(val).append("&");
      }
    }
    return sb.substring(0, sb.length() - 1);
  }

  /**
   * 通过paramsMap取出其中的键值对来构造url查询参数 如?a=b&c=d
   *
   * @param params
   * @return
   */
  public static String createQueryParams(Map<String, String> params) {
    StringBuilder sb = new StringBuilder("?");
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String value = entry.getValue();
      if (value == null || value.trim().equals("")) continue;
      sb.append(entry.getKey()).append("=").append(value).append("&");
    }
    return sb.substring(0, sb.length() - 1);
  }

  /**
   * 获取请求的查询参数串
   *
   * @param request
   * @return
   */
  public static String getQueryString(HttpServletRequest request) {
    String qs = null;
    Map<String, String[]> parameterMap = request.getParameterMap();
    if (Objects.nonNull(parameterMap) && !parameterMap.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (String name : parameterMap.keySet()) {
        for (String value : parameterMap.get(name)) {
          sb.append(name).append("=").append(value).append("&");
        }
      }
      qs = sb.substring(0, sb.length() - 1);
    }
    return qs;
  }

  /**
   * 获取原始访问的url
   *
   * @param request
   * @return
   */
  public static String getPrimitiveRequestUrl(HttpServletRequest request) {
    //获取原始访问路径,登录成功后重定向到该路径
    String primitiveUri = request.getRequestURI();
    String qs = HttpUtil.getQueryString(request);
    if (Objects.nonNull(qs)) {
      primitiveUri += ("?" + qs);
    }
    return primitiveUri;
  }

  public static void responseJson(HttpServletResponse response, Object responseData) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    try (PrintWriter writer = response.getWriter()) {
      writer.write(JsonUtil.createJson(responseData));
    }
  }

  public static boolean isAjaxRequest(HttpServletRequest request) {
    return request.getHeader("X-Requested-With") != null &&
            request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest");
  }

  /**
   * 获取服务器本地ip地址
   *
   * @return
   */
  private static String getMyServerIp() {
    String serverIp = "";
    // 根据网卡取本机配置的IP
    Enumeration<NetworkInterface> allNetInterfaces;  //定义网络接口枚举类
    try {
      allNetInterfaces = NetworkInterface.getNetworkInterfaces();  //获得网络接口
      InetAddress ip = null; //声明一个InetAddress类型ip地址
      while (allNetInterfaces.hasMoreElements()) //遍历所有的网络接口
      {
        NetworkInterface netInterface = allNetInterfaces.nextElement();
        Enumeration<InetAddress> addresses = netInterface.getInetAddresses(); //同样再定义网络地址枚举类
        while (addresses.hasMoreElements()) {
          ip = addresses.nextElement();
          if (ip != null && (ip instanceof Inet4Address)) //InetAddress类包括Inet4Address和Inet6Address
          {
            if (!ip.getHostAddress().equals("127.0.0.1")) {
              serverIp = ip.getHostAddress();
            }
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return serverIp;
  }

}
