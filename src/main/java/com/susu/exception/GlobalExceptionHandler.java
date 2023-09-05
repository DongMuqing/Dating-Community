package com.susu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.susu.entity.Code;
import com.susu.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

/**
 * @Date:2023/8/4 15:15
 * @Created by Muqing
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 全局异常拦截
    @ExceptionHandler(NotLoginException.class)
    public Result handlerException() {
        Integer code = Code.SYSTEM_ERR;
        String msg = "请先登录！";
        return new Result(null, code, msg);
    }
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Result missingServletRequestPartException() {
        Integer code = Code.SYSTEM_ERR;
        String msg = "请选择文件！";
        return new Result(null, code, msg);
    }
}

