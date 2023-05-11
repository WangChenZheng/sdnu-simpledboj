package com.sdnu.dboj.judger.entity.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: WangChen
 * @Date: 2023/5/9 13:42
 * @Version: 1.0
 * @Description:
 */

@Data
public class TestProcedureVo {

    private String id;

    private String filename;

    private String path;

    private String moduleId;

    private String moduleName;

    private String userFilePath;

    private String resultPath;

    private String testFile;

    private String resultFile;
}
