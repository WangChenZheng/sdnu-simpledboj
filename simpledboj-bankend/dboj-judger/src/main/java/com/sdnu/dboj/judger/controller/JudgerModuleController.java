package com.sdnu.dboj.judger.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.config.PathConfig;
import com.sdnu.dboj.judger.entity.JudgerModule;
import com.sdnu.dboj.judger.entity.JudgerModuleFile;
import com.sdnu.dboj.judger.entity.JudgerRecord;
import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.entity.vo.ChapterVo;
import com.sdnu.dboj.judger.entity.vo.ModuleFileVo;
import com.sdnu.dboj.judger.service.*;
import com.sdnu.dboj.judger.utils.JudgeQueue;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author: WangChen
 * @Date: 2023/3/13 19:18
 * @Version: 1.0
 * @Description:
 */

@CrossOrigin
@Controller
@RequestMapping("/module")
@ResponseBody
public class JudgerModuleController {

    @Autowired
    private ExecService execService;
    @Autowired
    private FileService fileService;
    @Autowired
    private JudgerModuleService moduleService;
    @Autowired
    private JudgerModuleFileService moduleFileService;
    @Autowired
    private JudgerRecordService recordService;
    @Autowired
    private UcenterUserService userService;

    @GetMapping("chapterTree")
    public Result chapterTree() {
        List<ChapterVo> moduleTree = moduleService.moduleTree();
        return Result.ok().data("list", moduleTree);
    }

    @PostMapping("deleteChapter")
    public Result deleteChapter(@RequestBody JudgerModule module) {
        String moduleId = module.getId();
        if (StringUtils.isEmpty(moduleId)) {
            return Result.error().message("章节不能为空");
        }
        QueryWrapper<JudgerModule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", moduleId);
        moduleService.remove(queryWrapper);
        moduleService.removeById(moduleId);
        return Result.ok();
    }

    @PostMapping("deleteBatchChapter")
    public Result deleteBatchChapter(@RequestBody Map<String, String>[] modules) {

        ArrayList<String> errorList = new ArrayList<>();
        int countSuccess = 0;
        int countError = 0;
        for (Map<String, String> module: modules) {
            try {
                QueryWrapper<JudgerModule> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("parent_id", module.get("id"));
                moduleService.remove(queryWrapper);
                moduleService.removeById(module.get("id"));
                countSuccess += 1;
            } catch (Exception e) {
                countError += 1;
                errorList.add(module.get("moduleName"));
                System.out.println(e.getMessage());
            }
        }
        String msg = "成功: " + countSuccess + ", 失败: " + countError + "。\n失败列表：" + errorList;
        return Result.ok().message(msg);
    }

    @GetMapping("getDocument/{id}")
    public Result getDocument(@PathVariable("id") String id) {
        JudgerModule module = moduleService.getById(id);
        return Result.ok().data("doc", module.getDoc());
    }

    @GetMapping("getModule/{id}")
    public Result getModule(@PathVariable("id") String id) {
        JudgerModule module = moduleService.getById(id);
        return Result.ok().data("module", module);
    }

    @PostMapping("addModule")
    public Result add(@RequestBody JudgerModule judgerModule) {
        if (StringUtils.isEmpty(judgerModule.getModuleName())) {
            return Result.error().message("章节名不为空");
        }
        if (StringUtils.isEmpty(judgerModule.getDetails())) {
            return Result.error().message("章节描述不为空");
        }
        if (StringUtils.isEmpty(judgerModule.getModulePath())) {
            return Result.error().message("模块所在路径不为空");
        }
//        judgerModule.setCaseNum(0);
//        judgerModule.setSubmitNum(0);
//        judgerModule.setAcceptNum(0);
        boolean flag = moduleService.saveOrUpdate(judgerModule);
        return flag? Result.ok(): Result.error();
    }

    @PostMapping("/judge/{moduleId}")
    public Result moduleJudge(@PathVariable("moduleId") String moduleId,
                              HttpServletRequest request) {
        String token = request.getHeader("Token");
        String username = JwtUtils.getIdByJwtToken(token);
        QueryWrapper<UcenterUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UcenterUser user = userService.getOne(queryWrapper);
//        System.out.println("judge module id: " + moduleId);
        JudgerRecord record = new JudgerRecord();
        record.setUserId(user.getId());
        record.setModuleId(moduleId);
        record.setStatus(0);
        recordService.saveOrUpdate(record);
        JudgeQueue.add(moduleId, record);
        return Result.ok();
    }

    @PostMapping("addModuleFile")
    public Result addModuleFile(@RequestBody JudgerModuleFile judgerModuleFile) {
        if (StringUtils.isEmpty(judgerModuleFile.getModuleId())) {
            return Result.error().message("模块不能为空");
        }
        if (StringUtils.isEmpty(judgerModuleFile.getFilename())) {
            return Result.error().message("文件名不能为空");
        }
        boolean flag = moduleFileService.saveOrUpdate(judgerModuleFile);
        return flag? Result.ok(): Result.error();
    }

    @GetMapping("getModuleFile/{moduleId}")
    public Result getModuleFile(@PathVariable("moduleId") String moduleId) {
        List<ModuleFileVo> moduleFileVoList = moduleFileService.getModuleFileList(moduleId);
        return Result.ok().data("list", moduleFileVoList);
    }

    @GetMapping("downloadModule/{moduleId}")
    public Result downloadModule(@PathVariable("moduleId") String moduleId) throws IOException {
        JudgerModule module = moduleService.getById(moduleId);
        if (module == null) {
            return Result.error().message("请选择模块");
        }
        String base64String = fileService.getModuleZip(module);
        return Result.ok().data("base64", base64String);
    }
}
