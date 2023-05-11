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
 * @since 2023-04-06 04:31:45
 */
@Getter
@Setter
@TableName("ucenter_role")
@ApiModel(value = "UcenterRole对象", description = "")
public class UcenterRole {

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
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("角色名")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty("角色")
    @TableField("name")
    private String name;


}
