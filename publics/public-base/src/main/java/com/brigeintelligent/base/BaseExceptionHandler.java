package com.brigeintelligent.base;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName BaseExceptionHandler
 * @Description 统一异常处理类
 * @Author Sugweet Chen
 * @Date 2019/5/3 14:08
 * @Version 1.0
 **/
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(Exception.class)
    public BaseResponse error(Exception e) {
        return new BaseResponse(false, BaseCode.FAILED, e.getMessage());
    }
}
