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
@TableName("ucenter_user")
@ApiModel(value = "UcenterUser对象", description = "")
public class UcenterUser {

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

    @ApiModelProperty("学号")
    @TableField("username")
    private String username;

    @ApiModelProperty("学生名")
    @TableField("name")
    private String name;

    @ApiModelProperty("昵称")
    @TableField("nickname")
    private String nickname;

    @ApiModelProperty("电子邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty("班级")
    @TableField("clazz")
    private String clazz;

    @ApiModelProperty("密码")
    @TableField("password")
    private String password;

    @ApiModelProperty("版本")
    @TableField(value = "version", fill = FieldFill.INSERT)
    @Version
    private Integer version;

}
