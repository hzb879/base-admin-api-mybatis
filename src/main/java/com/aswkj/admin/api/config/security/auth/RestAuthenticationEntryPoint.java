package com.aswkj.admin.api.config.security.auth;

/**
 * Created by fan.jin on 2016-11-12.
 */

import com.aswkj.admin.api.common.response.ResponseMsgEnum;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.util.HttpUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by fan.jin on 2016-11-07.
 */
//@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request,
                       HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    // This is invoked when user tries to access a secured REST resource without supplying any credentials
    // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to

    ResponseData data = null;
    if (UsernameNotFoundException.class.isInstance(authException)
            || BadCredentialsException.class.isInstance(authException)) {
      data = ResponseData.responseMsgEnumData(ResponseMsgEnum.USERNAME__PASSWORD_ERROR, authException.getMessage());
    } else {
      data = ResponseData.responseMsgEnumData(ResponseMsgEnum.UN_AUTHORIZED, authException.getMessage());
    }

    HttpUtil.responseJson(response, data);
  }

}

