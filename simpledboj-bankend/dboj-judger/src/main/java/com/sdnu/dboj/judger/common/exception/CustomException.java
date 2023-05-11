package com.sdnu.dboj.judger.common.exception;

import com.sdnu.dboj.judger.common.base.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: WangChen
 * @Date: 2023/4/29 13:34
 * @Version: 1.0
 * @Description:
 */

@Data
public class CustomException extends RuntimeException {

    //状态码
    private Integer code;
    //输出消息
    private String msg;

    public CustomException() {

    }

    public CustomException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CustomException(String msg) {
        this.code = ResultCode.ERROR;
        this.msg = msg;
    }


}
