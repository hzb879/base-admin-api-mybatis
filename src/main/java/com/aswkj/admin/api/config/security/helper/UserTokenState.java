package com.aswkj.admin.api.config.security.helper;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserTokenState {
    private String token;
    private Integer expires_in;

}