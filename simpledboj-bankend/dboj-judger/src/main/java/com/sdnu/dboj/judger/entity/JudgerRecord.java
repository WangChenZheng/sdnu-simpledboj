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
 * @since 2023-04-06 04:04:58
 */
@Getter
@Setter
@TableName("judger_record")
@ApiModel(value = "JudgerRecord对象", description = "")
public class JudgerRecord {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "deleted", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT)
    private LocalDateTime updateTime;

    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty("模块ID")
    @TableField("module_id")
    private String moduleId;

    @ApiModelProperty("状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("执行结果")
    @TableField("result")
    private String result;

    @ApiModelProperty("错误案例")
    @TableField("errorcase")
    private String errorcase;

    @ApiModelProperty("耗费时间")
    @TableField("costtime")
    private Long costtime;


}
