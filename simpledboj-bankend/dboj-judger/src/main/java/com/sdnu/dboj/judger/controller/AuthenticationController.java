package com.sdnu.dboj.judger.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.entity.UcenterRole;
import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.entity.UcenterUserRole;
import com.sdnu.dboj.judger.entity.vo.UserVo;
import com.sdnu.dboj.judger.service.UcenterRoleService;
import com.sdnu.dboj.judger.service.UcenterUserRoleService;
import com.sdnu.dboj.judger.service.UcenterUserService;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @Author: WangChen
 * @Date: 2023/4/21 18:42
 * @Version: 1.0
 * @Description:
 */

@CrossOrigin
@Controller
@ResponseBody
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UcenterUserService userService;
    @Autowired
    private UcenterUserRoleService userRoleService;
    @Autowired
    private UcenterRoleService roleService;

//    @PostMapping("loginByPwd")
//    public Result loginByPwd(@RequestBody Map<String, String> user){
//        String username = user.get("username");
//        String password = user.get("password");
//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            return Result.error().message("用户名或密码不能为空");
//        }
//        String token = userService.loginByPwd(username, password);
//        return Result.ok().data("token",token);
//    }

    @RequestMapping("getUserInfo")
    public Result getUserInfo(@RequestParam("token") String token) {
        String username = JwtUtils.getIdByJwtToken(token);
        QueryWrapper<UcenterUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UcenterUser originUser = userService.getOne(queryWrapper);
        UserVo user = new UserVo();
        BeanUtils.copyProperties(originUser, user);
        ArrayList<String> roles = new ArrayList<>();
        QueryWrapper<UcenterUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", originUser.getId());
        List<UcenterUserRole> userRoleList = userRoleService.list(userRoleQueryWrapper);
        for (UcenterUserRole userRole : userRoleList) {
            UcenterRole role = roleService.getById(userRole.getRoleId());
            roles.add(role.getName());
        }
        user.setRoles(roles);
        if (null == user) {
            return Result.error().message("发生错误，请联系管理员");
        }
        return Result.ok().data("user", user);
    }

}
