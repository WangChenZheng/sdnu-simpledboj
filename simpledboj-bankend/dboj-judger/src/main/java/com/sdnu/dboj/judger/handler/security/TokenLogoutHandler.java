package com.sdnu.dboj.judger.handler.security;


import com.sdnu.dboj.judger.common.base.ResponseUtil;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 登出业务逻辑类
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
public class TokenLogoutHandler implements LogoutHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("token");
        if (token != null) {
            //清空当前用户缓存中的权限数据
//            String id = JwtUtils.getIdByJwtToken(token);
//            redisTemplate.delete(id);
        }
        ResponseUtil.out(response, Result.ok());
    }

}