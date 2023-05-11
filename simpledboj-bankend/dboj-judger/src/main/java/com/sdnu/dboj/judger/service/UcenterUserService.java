package com.sdnu.dboj.judger.service;

import com.sdnu.dboj.judger.entity.UcenterUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Wang Chen
 * @since 2023-04-06 04:04:58
 */
public interface UcenterUserService extends IService<UcenterUser> {

    String loginByPwd(String username, String password);

    UcenterUser selectByUsername(String username);
}
