package com.sdnu.dboj.judger.filter;


import com.sdnu.dboj.judger.common.base.ResponseUtil;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author: WangChen
 * @Date: 2023/4/20 10:27
 * @Version: 1.0
 * @Description:
 */

public class TokenAuthenticationFilter extends BasicAuthenticationFilter {
    @Resource
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
//        logger.info("================="+req.getRequestURI());
//        if(req.getRequestURI().indexOf("admin") == -1) {
//            chain.doFilter(req, res);
//            return;
//        }

        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = getAuthentication(req);
        } catch (Exception e) {
            ResponseUtil.out(res, Result.error());
        }

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            ResponseUtil.out(res, Result.error());
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // token置于header里
        String token = request.getHeader("Token");
        if (token != null && !"".equals(token.trim())) {
            String username = JwtUtils.getIdByJwtToken(token);

//            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
//            for(String permissionValue : permissionValueList) {
//                if(StringUtils.isEmpty(permissionValue)) {
//                    continue;
//                }
//                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permissionValue);
//                authorities.add(authority);
//            }

            if (!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, token, authorities);
            }
            return null;
        }
        return null;
    }
}