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
 * @since 2023-04-02 10:24:52
 */
@Getter
@Setter
@TableName("judger_module_file")
@ApiModel(value = "JudgerModuleFile对象", description = "")
public class JudgerModuleFile {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "deleted", fill = FieldFill.INSERT_UPDATE)
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("版本")
    @TableField(value = "version", fill = FieldFill.INSERT_UPDATE)
    @Version
    private Integer version;

    @ApiModelProperty("模块ID")
    @TableField("module_id")
    private String moduleId;

    @ApiModelProperty("文件名")
    @TableField("filename")
    private String filename;

    @ApiModelProperty("描述")
    @TableField("details")
    private String details;

    @ApiModelProperty("文件路径")
    @TableField("filepath")
    private String filepath;


}
