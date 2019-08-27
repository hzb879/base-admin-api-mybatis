package com.aswkj.admin.api.config.security.auth;

import com.aswkj.admin.api.module.pms.model.UserDetailsModel;
import com.aswkj.admin.api.module.pms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Autowired
  IUserService userService;

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    UserDetailsModel userDetailsModel = userService.getUserDetailsModelByUserId(s);
    if(Objects.isNull(userDetailsModel)) {
      throw new UsernameNotFoundException("用户不存在");
    }
    return userDetailsModel;

  }
}
