package com.sdnu.dboj.judger.service;


import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.entity.security.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * <p>
 * 自定义userDetailsService - 认证用户详情
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Service("authenticationService")
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private UcenterUserService userService;

//    @Autowired
//    private PermissionService permissionService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        UcenterUser user = userService.selectByUsername(username);

        // 判断用户是否存在
        if (null == user){
            throw new UsernameNotFoundException("用户名不存在！");
        }

//        List<String> authorities = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser(user);
//        securityUser.setPermissionValueList(authorities);
        securityUser.setPermissionValueList(new ArrayList<>());
        return securityUser;
    }

}
