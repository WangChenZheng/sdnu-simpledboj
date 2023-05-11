package com.sdnu.dboj.judger.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: WangChen
 * @Date: 2023/5/2 20:12
 * @Version: 1.0
 * @Description:
 */

@Data
public class UserVo {

    private String id;

    private String username;

    private String name;

    private String nickname;

    private String email;

    private String clazz;

    private List<String> roles;
}
