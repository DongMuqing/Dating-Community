package com.susu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.util.SaResult;
import com.susu.damian.Code;
import com.susu.damian.Result;
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
    public Result handlerException(Exception e) {
        Integer code = Code.SYSTEM_ERR;
        String msg = "请先登录！";
        return new Result(null, code, msg);
    }
}

