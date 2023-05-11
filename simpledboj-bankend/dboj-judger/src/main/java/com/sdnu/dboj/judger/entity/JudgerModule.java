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
@TableName("judger_module")
@ApiModel(value = "JudgerModule对象", description = "")
public class JudgerModule {

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

    @ApiModelProperty("父ID")
    @TableField("parent_id")
    private String parentId;

    @ApiModelProperty("模块名字")
    @TableField("module_name")
    private String moduleName;

    @ApiModelProperty("描述")
    @TableField("details")
    private String details;

    @ApiModelProperty("文档")
    @TableField("doc")
    private String doc;

    @ApiModelProperty("模块路径")
    @TableField("module_path")
    private String modulePath;

    @ApiModelProperty("排序")
    @TableField("sort")
    private int sort;

    @ApiModelProperty("测试案例个数")
    @TableField("case_num")
    private int caseNum;

    @ApiModelProperty("提交人数")
    @TableField("submit_num")
    private int submitNum;

    @ApiModelProperty("成功人数")
    @TableField("accept_num")
    private int acceptNum;


}
