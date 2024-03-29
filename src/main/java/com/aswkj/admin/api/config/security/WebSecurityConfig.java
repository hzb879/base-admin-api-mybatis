package com.aswkj.admin.api.config.security;


import com.aswkj.admin.api.config.security.auth.JwtUserDetailsService;
import com.aswkj.admin.api.config.security.auth.TokenAuthenticationFilter;
import com.aswkj.admin.api.config.security.helper.TokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Autowired
  private JwtUserDetailsService jwtUserDetailsService;

//  @Autowired
//  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Autowired
  private TokenHelper tokenHelper;

  // Custom the ROLE_ prefix
  @Bean
  public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(jwtUserDetailsService)
            .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            // by default uses a Bean by the name of corsConfigurationSource
            .cors().and()
            // we don't need CSRF because our token is invulnerable
            .csrf().disable()
            // don't create session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//            .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

            .authorizeRequests()
            .anyRequest().authenticated().and()
            .addFilterBefore(new TokenAuthenticationFilter(tokenHelper, jwtUserDetailsService), BasicAuthenticationFilter.class);

    // disable page caching
    http.headers().cacheControl();

  }

  /**
   * 配置静态资源不拦截
   *
   * @param web
   * @throws Exception
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
//    web.ignoring().antMatchers("/**");

    //TokenAuthenticationFilter will ignore the below paths
    web.ignoring().antMatchers(
            HttpMethod.GET,
            "/images/**",
            "/*.html",
            "/favicon.ico",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js",
            "/**/*.map"
    ).antMatchers("/auth/login");

    // Allow swagger to be accessed without authentication and other public resource
    web.ignoring().antMatchers(
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/configuration/**",
            "/webjars/**",
            "/v3/swagger*/**",
            "/public",
            "/error");

  }


}
