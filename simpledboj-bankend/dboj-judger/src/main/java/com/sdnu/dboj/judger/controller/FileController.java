package com.sdnu.dboj.judger.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.config.PathConfig;
import com.sdnu.dboj.judger.entity.JudgerModule;
import com.sdnu.dboj.judger.entity.JudgerModuleFile;
import com.sdnu.dboj.judger.entity.JudgerRecord;
import com.sdnu.dboj.judger.entity.UcenterUser;
import com.sdnu.dboj.judger.service.*;
import com.sdnu.dboj.judger.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * @Author: WangChen
 * @Date: 2023/4/2 10:10
 * @Version: 1.0
 * @Description:
 */

@CrossOrigin
@Controller
@RequestMapping("/file")
@ResponseBody
public class FileController {

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

    @PostMapping("/uploadChunk")
    public Result uploadChunk(@RequestParam("file") MultipartFile file,
                              @RequestParam("filename") String filename,
                              @RequestParam("chunk") Integer chunk,
                              @RequestParam("chunks") Integer chunks) {
        String userId = "201911010223";
        String uploadPath = Paths.get(PathConfig.USER_SOURCE_PATH, userId).toString();
        try {
            fileService.saveChunk(file, filename, uploadPath, chunk);
            if (chunk == chunks - 1) {
                fileService.mergeChunks(filename, uploadPath);
            }
            return Result.ok();
        } catch (IOException e) {
            return Result.error();
        }
    }

    @PostMapping("/uploadFile")
    public Result uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("fileId") String fileId,
                             HttpServletRequest request) {
        String token = request.getHeader("Token");
        String username = JwtUtils.getIdByJwtToken(token);
        QueryWrapper<UcenterUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UcenterUser user = userService.getOne(queryWrapper);
        JudgerModuleFile moduleFile = moduleFileService.getById(fileId);
        JudgerModule module = moduleService.getById(moduleFile.getModuleId());
        String modulePath = module.getModulePath();
        String uploadPath = Paths.get(PathConfig.USER_SOURCE_PATH, user.getId(), modulePath).toString();
        try {
            fileService.saveFile(file, uploadPath);
            return Result.ok();
        } catch (IOException e) {
            return Result.error();
        }
    }

}
