package com.sdnu.dboj.judger.entity.vo;

import lombok.Data;

/**
 * @Author: WangChen
 * @Date: 2023/5/9 22:49
 * @Version: 1.0
 * @Description:
 */

@Data
public class RecordVo {

    private String id;

    private String userId;

    private String moduleId;

    private String moduleName;

    private Integer status;

    private String result;

    private String errorcase;

    private Long costtime;

}
