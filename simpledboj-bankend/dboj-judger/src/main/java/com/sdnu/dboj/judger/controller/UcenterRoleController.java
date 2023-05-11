package com.sdnu.dboj.judger.controller;


import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.entity.UcenterRole;
import com.sdnu.dboj.judger.service.UcenterRoleService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @since 2023-04-06 04:31:45
 */
@RestController
@RequestMapping("/ucenter/role")
public class UcenterRoleController {

    @Autowired
    private UcenterRoleService roleService;

    @GetMapping("listRole")
    public Result listRole() {
        List<UcenterRole> list = roleService.list();
        return Result.ok().data("list", list);
    }

    @PostMapping("addRole")
    public Result addRole(@RequestBody UcenterRole role) {
        boolean flag = roleService.saveOrUpdate(role);
        return flag? Result.ok(): Result.error();
    }

    @PostMapping("deleteRole")
    public Result deleteRole(@RequestBody UcenterRole role) {
        boolean flag = roleService.removeById(role.getId());
        return flag? Result.ok(): Result.error();
    }

    @PostMapping("deleteBatchRole")
    public Result deleteBatchRole(@RequestBody UcenterRole[] roles) {

        ArrayList<String> errorList = new ArrayList<>();
        int countSuccess = 0;
        int countError = 0;
        for (UcenterRole role: roles) {
            try {
                roleService.removeById(role.getId());
                countSuccess += 1;
            } catch (Exception e) {
                countError += 1;
                errorList.add(role.getRoleName());
                System.out.println(e.getMessage());
            }
        }
        String msg = "成功: " + countSuccess + ", 失败: " + countError + "。\n失败列表：" + errorList;
        return Result.ok().message(msg);
    }

}

