package com.sdnu.dboj.judger.config;

import com.alibaba.fastjson.JSONObject;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.filter.TokenAuthenticationFilter;
import com.sdnu.dboj.judger.filter.UserAuthenticationFilter;
import com.sdnu.dboj.judger.handler.security.TokenLogoutHandler;
import com.sdnu.dboj.judger.handler.security.UnauthorizedEntryPoint;
import com.sdnu.dboj.judger.service.AuthenticationService;
import com.sdnu.dboj.judger.utils.JwtUtils;
import com.sdnu.dboj.judger.utils.Md5PasswordEncoder;
import org.apache.el.parser.Token;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: WangChen
 * @Date: 2023/4/29 1:03
 * @Version: 1.0
 * @Description:
 */

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * {@link UserDetailsService} 实现类
     */
    @Resource
    private AuthenticationService authenticationService;


    @Override
    public void configure(WebSecurity web) throws Exception {
        // 静态资源放行
        web.ignoring().antMatchers(
                "/css/**", "/js/**", "/index.html", "/img/**", "/webjars/**",
                "/fonts/**", "/favicon.ico", "/JsonLogin.html", "/api/**",
                "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**"
        );
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/**").hasRole("admin")
//                .antMatchers("/judge/**").authenticated()
                .anyRequest().permitAll()
                .and().logout().logoutUrl("/auth/logout").addLogoutHandler(new TokenLogoutHandler())
                .and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().cors()
                .and()
                .addFilter(new UserAuthenticationFilter(authenticationManager()))
                .addFilter(new TokenAuthenticationFilter(authenticationManager()));


    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 添加自定义的 AuthenticationProvider
        auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
//        configuration.addAllowedHeader("*");
//        configuration.setExposedHeaders(Arrays.asList("token"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("*");
        cors.setAllowCredentials(true);
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
//        cors.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }

    /**
     * 密码加密解密
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
