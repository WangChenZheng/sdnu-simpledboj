package com.sdnu.dboj.judger.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.common.exception.CustomException;
import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.mapper.UcenterUserMapper;
import com.sdnu.dboj.judger.service.UcenterUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Wang Chen
 * @since 2023-04-06 04:04:58
 */
@Service
public class UcenterUserServiceImpl extends ServiceImpl<UcenterUserMapper, UcenterUser> implements UcenterUserService {

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public String loginByPwd(String username, String password) {
        QueryWrapper<UcenterUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UcenterUser user = baseMapper.selectOne(queryWrapper);
        if (null == user || !passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException("用户名或密码错误");
        }
        String token = JwtUtils.getJwtToken(user.getUsername());
        return token;
    }

    @Override
    public UcenterUser selectByUsername(String username) {
        QueryWrapper<UcenterUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UcenterUser ucenterUser = baseMapper.selectOne(queryWrapper);
        return ucenterUser;
    }
}
