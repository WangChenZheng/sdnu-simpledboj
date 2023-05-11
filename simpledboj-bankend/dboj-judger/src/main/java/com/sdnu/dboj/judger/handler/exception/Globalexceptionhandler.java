package com.sdnu.dboj.judger.handler.exception;



import com.sdnu.dboj.judger.common.base.Result;
import com.sdnu.dboj.judger.common.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class Globalexceptionhandler {

    //指定异常执行方法
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e){
        e.printStackTrace();
        return Result.error().message("方法执行ArithmeticException异常！");
    }

    //自定义的异常处理
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e){
//        log.error(e.getMessage());
//        e.printStackTrace();
        return Result.error().code(e.getCode()).message(e.getMsg());
    }

}
