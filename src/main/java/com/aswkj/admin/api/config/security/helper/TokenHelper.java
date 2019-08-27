package com.aswkj.admin.api.config.security.helper;

import com.aswkj.admin.api.common.enums.UserStatusEnum;
import com.aswkj.admin.api.config.exception.UserStatusException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
public class TokenHelper {

  @Value("${jwt.secret}")
  public String SECRET;

  @Value("${jwt.expires_in}")
  private int EXPIRES_IN;

  @Value("${jwt.header}")
  private String AUTH_HEADER;

  @Autowired
  TimeProvider timeProvider;

  private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

  public String generateToken(String username) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(timeProvider.now())
            .setExpiration(generateExpirationDate())
            .signWith(SIGNATURE_ALGORITHM, SECRET)
            .compact();
  }

  public Claims getAllClaimsFromToken(String token) {
    return Jwts.parser()
            .setSigningKey(SECRET)
            .parseClaimsJws(token)
            .getBody();
  }

  private Date generateExpirationDate() {
    return new Date(timeProvider.now().getTime() + EXPIRES_IN * 1000);
  }


  public int getExpiredIn() {
    return EXPIRES_IN;
  }


  public String getToken(HttpServletRequest request) {
    /**
     *  Getting the token from Authentication header
     *  e.g Bearer your_token
     */
//    String authHeader = getAuthHeaderFromHeader(request);
//    if (authHeader != null && authHeader.startsWith("Bearer ")) {
//      return authHeader.substring(7);
//    }

    return getAuthHeaderFromHeader(request);
  }


  public String getAuthHeaderFromHeader(HttpServletRequest request) {
    return request.getHeader(AUTH_HEADER);
  }


  public void validateUserStatus(UserStatusEnum status) {
    if (!UserStatusEnum.ENABLE.equals(status)) {
      throw new UserStatusException("您已被禁止登陆！");
    }
  }

}