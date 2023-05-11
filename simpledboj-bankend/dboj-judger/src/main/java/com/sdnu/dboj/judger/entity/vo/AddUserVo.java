package com.sdnu.dboj.judger.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: WangChen
 * @Date: 2023/5/9 23:52
 * @Version: 1.0
 * @Description:
 */

@Data
public class AddUserVo {

    private String id;

    private String username;

    private String name;

    private String nickname;

    private String email;

    private String clazz;

    private String password;

    private List<String> roles;
}
