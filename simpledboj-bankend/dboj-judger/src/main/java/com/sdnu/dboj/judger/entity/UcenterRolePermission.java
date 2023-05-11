package com.sdnu.dboj.judger.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("ucenter_role_permission")
@ApiModel(value = "UcenterRolePermission对象", description = "")
public class UcenterRolePermission {

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty("逻辑删除")
    @TableField("deleted")
    @TableLogic
    private Boolean deleted;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty("角色ID")
    @TableField("role_id")
    private String roleId;

    @ApiModelProperty("权限ID")
    @TableField("permission_id")
    private String permissionId;


}
