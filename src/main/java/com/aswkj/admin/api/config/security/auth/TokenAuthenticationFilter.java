package com.aswkj.admin.api.config.security.auth;

import cn.hutool.core.util.StrUtil;
import com.aswkj.admin.api.common.enums.ResponseMsgEnum;
import com.aswkj.admin.api.common.response.ResponseData;
import com.aswkj.admin.api.config.exception.UserStatusException;
import com.aswkj.admin.api.config.security.helper.TokenHelper;
import com.aswkj.admin.api.module.pms.model.UserDetailsModel;
import com.aswkj.admin.api.util.HttpUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private TokenHelper tokenHelper;

  private UserDetailsService userDetailsService;

  private Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

  public TokenAuthenticationFilter(TokenHelper tokenHelper, UserDetailsService userDetailsService) {
    this.tokenHelper = tokenHelper;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void doFilterInternal(
          HttpServletRequest request,
          HttpServletResponse response,
          FilterChain chain
  ) throws IOException, ServletException {

    try {
      String authToken = tokenHelper.getToken(request);
      if (StrUtil.isNotBlank(authToken)) {
        Claims claims = tokenHelper.getAllClaimsFromToken(authToken);
        String userId = claims.getSubject();
        if (Objects.nonNull(userId)) {
          UserDetailsModel userDetailsModel = (UserDetailsModel) userDetailsService.loadUserByUsername(userId);
          if (Objects.nonNull(userDetailsModel)) {
            validateToken(claims, userDetailsModel);
            //create authentication
            TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetailsModel);
            authentication.setToken(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
          }
        }
      }
    } catch (ExpiredJwtException ej) {
      HttpUtil.responseJson(response, ResponseData.responseMsgEnum(ResponseMsgEnum.EXPIRED_TOKEN));
      return;
    } catch (JwtException je) {
      HttpUtil.responseJson(response, ResponseData.responseMsgEnum(ResponseMsgEnum.INVALID_TOKEN));
      return;
    } catch (UserStatusException ue) {
      HttpUtil.responseJson(response, ResponseData.failMsg(ue.getMessage()));
      return;
    } catch (Exception e) {
      logger.error("error: ", e);
      throw e;
    }
    chain.doFilter(request, response);
  }

  private void validateToken(Claims claims, UserDetailsModel userDetailsModel) {
    tokenHelper.validateUserStatus(userDetailsModel.getStatus());
    LocalDateTime issuedAt = LocalDateTime.ofInstant(claims.getIssuedAt().toInstant(), ZoneId.systemDefault());
    LocalDateTime lastCipherUpdateTime = userDetailsModel.getLastCipherUpdateTime();
    LocalDateTime lastLogoutTime = userDetailsModel.getLastLogoutTime();

    notBeforeValidate(issuedAt, lastCipherUpdateTime);
    notBeforeValidate(issuedAt, lastLogoutTime);

  }

  private void notBeforeValidate(LocalDateTime issuedAt, LocalDateTime lastTime) {
    if (Objects.nonNull(lastTime)) {
      if (issuedAt.isBefore(lastTime)) {
        throw new JwtException("token失效");
      }
    }
  }

}