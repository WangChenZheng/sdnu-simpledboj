package com.sdnu.dboj.judger.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author Wang Chen
 * @since 2023-03-15 08:44:03
 */
@Getter
@Setter
@TableName("judger_test_procedure")
@ApiModel(value = "JudgerTestProcedure对象", description = "")
public class JudgerTestProcedure {

    @ApiModelProperty("uuid")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("版本")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Integer version;

    @ApiModelProperty("测试程序名")
    @TableField("filename")
    private String filename;

    @ApiModelProperty("测试程序路径")
    @TableField("path")
    private String path;

    @ApiModelProperty("模块ID")
    @TableField("module_id")
    private String moduleId;

    @ApiModelProperty("测试程序生成文件路径")
    @TableField("user_file_path")
    private String userFilePath;

    @ApiModelProperty("结果文件路径")
    @TableField("result_path")
    private String resultPath;


}
