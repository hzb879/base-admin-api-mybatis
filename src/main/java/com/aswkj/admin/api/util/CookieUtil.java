package com.aswkj.admin.api.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class CookieUtil {
	
	/**
	 * 设置cookie
	 * @param response
	 * @param key
	 * @param value
	 * @param expiry 失效时间,单位:秒
	 */
	public static void setCookie(HttpServletResponse response, String key, String value, int expiry) {
		try {
			Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
			cookie.setMaxAge(expiry);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置不允许js获取的cookie
	 * @param response
	 * @param key
	 * @param value
	 * @param expiry 失效时间,单位:秒
	 */
	public static void setHttpOnlyCookie(HttpServletResponse response, String key, String value, int expiry) {
		try {
			Cookie cookie = new Cookie(key, URLEncoder.encode(value, "UTF-8"));
			cookie.setMaxAge(expiry);
			cookie.setPath("/");
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie cookie = getCookieObject(request, name);
		return cookie != null ? cookie.getValue() : null;
	}
	
	public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name){
		Cookie cookie = getCookieObject(request, name);
		if(cookie!=null){
			cookie.setMaxAge(0);
	        	cookie.setPath("/");
	        	response.addCookie(cookie);
		}
    }
	
	private static Cookie getCookieObject(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}
	
}
