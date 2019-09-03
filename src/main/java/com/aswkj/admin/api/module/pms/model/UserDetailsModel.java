package com.aswkj.admin.api.module.pms.model;

import com.aswkj.admin.api.module.pms.enums.UserStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetailsModel implements UserDetails, Serializable {

  private String id;

  private String username;
  private String cipher;
  private UserStatusEnum status;

  private LocalDateTime lastCipherUpdateTime;

  private LocalDateTime lastLogoutTime;

  private List<String> roles;

  @Override
  @JsonIgnore
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

  @Override
  @JsonIgnore
  public String getPassword() {
    return this.cipher;
  }

  @Override
  @JsonIgnore
  public String getUsername() {
    return this.id;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return true;
  }

}
