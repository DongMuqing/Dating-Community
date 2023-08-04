package com.susu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Date:2023/8/4 15:15
 * @Created by Muqing
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler(NotLoginException.class)
    public SaResult handlerException(Exception e) {

        return SaResult.error(e.getMessage());
    }
}

