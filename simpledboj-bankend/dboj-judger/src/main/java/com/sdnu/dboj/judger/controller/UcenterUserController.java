package com.sdnu.dboj.judger.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.entity.UcenterRole;
import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.entity.UcenterUserRole;
import com.sdnu.dboj.judger.entity.vo.AddUserVo;
import com.sdnu.dboj.judger.entity.vo.UserVo;
import com.sdnu.dboj.judger.service.UcenterRoleService;
import com.sdnu.dboj.judger.service.UcenterUserRoleService;
import com.sdnu.dboj.judger.service.UcenterUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wang Chen
 * @since 2023-04-06 04:04:58
 */
@RestController
@ResponseBody
@RequestMapping("/ucenter")
public class UcenterUserController {

    @Autowired
    private UcenterUserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UcenterUserRoleService userRoleService;
    @Autowired
    private UcenterRoleService roleService;

    @PostMapping("addBatchUser")
    public Result addUser(@RequestBody Map<String, String>[] users) {

        ArrayList<String> errorList = new ArrayList<>();
        int countSuccess = 0;
        int countError = 0;
        for (Map<String, String> user : users) {
            UcenterUser newUser = new UcenterUser();
            String username = user.get("学号/工号");
            if (StringUtils.isEmpty(username)) {
                return Result.error().message("学号/工号不能为空");
            }
            newUser.setUsername(username);
            if (StringUtils.isEmpty(user.get("姓名"))) {
                return Result.error().message("姓名不能为空");
            }
            newUser.setName(user.get("姓名"));
            newUser.setNickname(user.get("姓名"));
            if (StringUtils.isEmpty(user.get("班级"))) {
                return Result.error().message("班级不能为空");
            }
            newUser.setClazz(user.get("班级"));
            if (!StringUtils.isEmpty(user.get("电子邮件"))) {
                newUser.setEmail(user.get("电子邮件"));
            }
            if (!user.containsKey("密码") || StringUtils.isEmpty(user.get("密码"))) {
                newUser.setPassword(passwordEncoder.encode(username.substring(username.length() - 6)));
            } else {
                newUser.setPassword(passwordEncoder.encode(user.get("密码")));
            }
            QueryWrapper<UcenterRole> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.eq("role_name", "学生");
            String roleId = roleService.getOne(roleQueryWrapper).getId();
            try {
                userService.saveOrUpdate(newUser);
                UcenterUserRole userRole = new UcenterUserRole();
                QueryWrapper<UcenterUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("user_id", newUser.getId());
                userRoleQueryWrapper.eq("role_id", roleId);
                UcenterUserRole userRole1 = userRoleService.getOne(userRoleQueryWrapper);
                if (userRole1 == null) {
                    userRole.setUserId(newUser.getId());
                    userRole.setRoleId(roleId);
                    userRoleService.saveOrUpdate(userRole);
                } else {
                    userRole1.setDeleted(false);
                    userRoleService.saveOrUpdate(userRole1);
                }

                countSuccess += 1;
            } catch (Exception e) {
                countError += 1;
                errorList.add(newUser.getName());
            }

        }
        String msg = "成功: " + countSuccess + ", 失败: " + countError + "。\n失败列表：" + errorList;
        return Result.ok().message(msg);
    }

    @PostMapping("deleteBatchUser")
    public Result deleteBatchUser(@RequestBody UserVo[] users) {

        ArrayList<String> errorList = new ArrayList<>();
        int countSuccess = 0;
        int countError = 0;
        for (UserVo user: users) {
            try {

                userService.removeById(user.getId());
                QueryWrapper<UcenterUserRole> userRoleQueryWrapper = new QueryWrapper<>();
                userRoleQueryWrapper.eq("user_id", user.getId());
                userRoleService.remove(userRoleQueryWrapper);
                countSuccess += 1;
            } catch (Exception e) {
                countError += 1;
                errorList.add(user.getName());
                System.out.println(e.getMessage());
            }
        }
        String msg = "成功: " + countSuccess + ", 失败: " + countError + "。\n失败列表：" + errorList;
        return Result.ok().message(msg);
    }

    @PostMapping("deleteUser")
    public Result deleteUser(@RequestBody UserVo user) {
        boolean flag = userService.removeById(user.getId());
        QueryWrapper<UcenterUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        userRoleService.remove(userRoleQueryWrapper);
        return flag ? Result.ok(): Result.error();
    }

    @PostMapping("addUser")
    public Result addUser(@RequestBody AddUserVo userVo) {
        UcenterUser ucenterUser = new UcenterUser();
        BeanUtils.copyProperties(userVo, ucenterUser);
        if (StringUtils.isEmpty(ucenterUser.getUsername())) {
            return Result.error().message("学号/工号不能为空");
        }
        if (StringUtils.isEmpty(ucenterUser.getName())) {
            return Result.error().message("姓名不能为空");
        }
        if (StringUtils.isEmpty(ucenterUser.getClazz())) {
            return Result.error().message("班级不能为空");
        }
        if (StringUtils.isEmpty(ucenterUser.getNickname())) {
            ucenterUser.setNickname(ucenterUser.getName());
        }
        String username = ucenterUser.getUsername();
        if (StringUtils.isEmpty(ucenterUser.getPassword())) {
            if (StringUtils.isEmpty(ucenterUser.getId())) {
                ucenterUser.setPassword(passwordEncoder.encode(username.substring(username.length() - 6)));
            }
        } else {
            ucenterUser.setPassword(passwordEncoder.encode(ucenterUser.getPassword()));
        }
        userService.saveOrUpdate(ucenterUser);
        List<String> roles = userVo.getRoles();
        boolean flag ;
        QueryWrapper<UcenterUserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", ucenterUser.getId());
        List<UcenterUserRole> userRoleList = userRoleService.list(userRoleQueryWrapper);
        for (UcenterUserRole userRole1 : userRoleList) {
            flag = false;
            for (String roleId : roles) {
                if (StringUtils.equals(userRole1.getRoleId(), roleId)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                // 角色被删除
                userRoleService.removeById(userRole1);
            }


        }
        for (String roleId : roles) {
            QueryWrapper<UcenterUserRole> userRoleQueryWrapper1 = new QueryWrapper<>();
            userRoleQueryWrapper1.eq("user_id", ucenterUser.getId());
            userRoleQueryWrapper1.eq("role_id", roleId);
            UcenterUserRole userRole2 = userRoleService.getOne(userRoleQueryWrapper1);
            if (userRole2 == null) {
                UcenterUserRole userRole = new UcenterUserRole();
                userRole.setUserId(ucenterUser.getId());
                userRole.setRoleId(roleId);
                userRoleService.saveOrUpdate(userRole);
            }
        }
        return Result.ok();
    }

    @PostMapping("page/{page}/{offset}")
    public Result pageUser(@PathVariable("page") Integer page,
                           @PathVariable("offset") Integer offset,
                           @RequestBody Map<String, String> param) {
        Page<UcenterUser> userPage = new Page<>(page, offset);
        QueryWrapper<UcenterUser> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(param.get("name"))) {
            queryWrapper.like("name", param.get("name"));
        }
        if (!StringUtils.isEmpty(param.get("username"))) {
            queryWrapper.like("username", param.get("username"));
        }
        if (!StringUtils.isEmpty(param.get("email"))) {
            queryWrapper.like("email", param.get("email"));
        }
        queryWrapper.orderByAsc("username");
        userService.page(userPage, queryWrapper);
        List<UcenterUser> records = userPage.getRecords();
        long total = userPage.getTotal();
        List<UserVo> userVoList = new ArrayList<>();
        for (UcenterUser record : records) {
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(record, userVo);
            ArrayList<String> roles = new ArrayList<>();
            QueryWrapper<UcenterUserRole> userRoleQueryWrapper = new QueryWrapper<>();
            userRoleQueryWrapper.eq("user_id", record.getId());
            List<UcenterUserRole> userRoleList = userRoleService.list(userRoleQueryWrapper);
            for (UcenterUserRole userRole : userRoleList) {
                UcenterRole role = roleService.getById(userRole.getRoleId());
                if (role != null) {
                    roles.add(role.getRoleName());
                }
            }
            userVo.setRoles(roles);
            userVoList.add(userVo);
        }
        return Result.ok().data("list", userVoList).data("total", total);
    }

}

