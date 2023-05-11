package com.sdnu.dboj.judger.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.config.PathConfig;
import com.sdnu.dboj.judger.entity.JudgerModule;
import com.sdnu.dboj.judger.entity.JudgerTestProcedure;
import com.sdnu.dboj.judger.entity.vo.TestProcedureVo;
import com.sdnu.dboj.judger.entity.vo.UserVo;
import com.sdnu.dboj.judger.service.FileService;
import com.sdnu.dboj.judger.service.JudgerModuleService;
import com.sdnu.dboj.judger.service.JudgerTestProcedureService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wang Chen
 * @since 2023-03-15 08:44:03
 */
@RestController
@RequestMapping("/module/test")
public class JudgerTestProcedureController {

    @Autowired
    private JudgerTestProcedureService testProcedureService;
    @Autowired
    private FileService fileService;
    @Autowired
    private JudgerModuleService moduleService;

    @PostMapping("deleteBatchTestProcedure")
    public Result deleteBatchTestProcedure(@RequestBody Map<String, String>[] testProcedures) {

        ArrayList<String> errorList = new ArrayList<>();
        int countSuccess = 0;
        int countError = 0;
        for (Map<String, String> testProcedure: testProcedures) {
            try {
                testProcedureService.removeById(testProcedure.get("id"));
                countSuccess += 1;
            } catch (Exception e) {
                countError += 1;
                errorList.add(testProcedure.get("filename"));
                System.out.println(e.getMessage());
            }
        }
        String msg = "成功: " + countSuccess + ", 失败: " + countError + "。\n失败列表：" + errorList;
        return Result.ok().message(msg);
    }

    @PostMapping("deleteTestProcedure")
    public Result deleteUser(@RequestBody TestProcedureVo testProcedureVo) {
        boolean flag = testProcedureService.removeById(testProcedureVo.getId());
        return flag ? Result.ok(): Result.error();
    }

    @PostMapping("pageCase/{page}/{offset}")
    public Result pageCase(@PathVariable("page") Integer page,
                           @PathVariable("offset") Integer offset,
                           @RequestBody Map<String, String> map) {
        Page<JudgerTestProcedure> testProcedurePage = new Page<>(page, offset);
        QueryWrapper<JudgerTestProcedure> testProcedureQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(map.get("moduleId"))) {
            testProcedureQueryWrapper.eq("module_id", map.get("moduleId"));
        }
        testProcedureQueryWrapper.orderByAsc("module_id");
        testProcedureService.page(testProcedurePage, testProcedureQueryWrapper);
        List<JudgerTestProcedure> records = testProcedurePage.getRecords();
        List<TestProcedureVo> testProcedureVoList = new ArrayList<>();
        for (JudgerTestProcedure record : records) {
            TestProcedureVo testProcedureVo = new TestProcedureVo();
            BeanUtils.copyProperties(record, testProcedureVo);
            String moduleName = moduleService.getById(record.getModuleId()).getModuleName();
            testProcedureVo.setModuleName(moduleName);
            testProcedureVoList.add(testProcedureVo);
        }
        long total = testProcedurePage.getTotal();
        return Result.ok().data("list", testProcedureVoList).data("total", total);
    }

    @PostMapping("/addCase")
    public Result add(@RequestBody TestProcedureVo testProcedureVo) {
        if (StringUtils.isEmpty(testProcedureVo.getFilename())) {
            return Result.error().message("文件名不为空");
        }
        if (StringUtils.isEmpty(testProcedureVo.getModuleId())) {
            return Result.error().message("章节不为空");
        }
        if (StringUtils.isEmpty(testProcedureVo.getResultPath())) {
            return Result.error().message("结果文件不为空");
        }
        if (StringUtils.isEmpty(testProcedureVo.getTestFile())) {
            return Result.error().message("测试文件不为空");
        }
        if (StringUtils.isEmpty(testProcedureVo.getResultFile())) {
            return Result.error().message("测试结果文件不为空");
        }
        String modulePath = moduleService.getById(testProcedureVo.getModuleId()).getModulePath();
        fileService.saveFile(testProcedureVo.getTestFile(), Paths.get(PathConfig.TEST_PATH,
                modulePath,
                testProcedureVo.getPath(),
                testProcedureVo.getFilename()).toString());
        fileService.saveFile(testProcedureVo.getResultFile(), Paths.get(PathConfig.REAL_RESULT_PATH,
                modulePath,
                testProcedureVo.getResultPath()).toString());
        JudgerTestProcedure testProcedure = new JudgerTestProcedure();
        BeanUtils.copyProperties(testProcedureVo, testProcedure);
        boolean flag = testProcedureService.saveOrUpdate(testProcedure);

        return flag? Result.ok(): Result.error();
    }

}

