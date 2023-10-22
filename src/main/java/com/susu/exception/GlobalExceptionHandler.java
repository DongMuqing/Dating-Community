package com.susu.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.util.SaResult;
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
    // 未登录
    @ExceptionHandler(NotLoginException.class)
    public Result handlerException() {
        return new Result(null, Code.SYSTEM_ERR,"请先登录！");
    }
    //权限不够
    @ExceptionHandler(NotRoleException.class)
    public Result notRoleException() {
        return new Result(null,Code.NO_PERMISSION,"权限不够,请联系管理员！");
    }
    @ExceptionHandler(MissingServletRequestPartException.class)
    public Result missingServletRequestPartException() {
        return new Result(null, Code.SYSTEM_ERR,"请选择文件！");
    }
    @ExceptionHandler(NullPointerException.class)
    public Result nullPointerException() {
        return new Result(null,Code.SYSTEM_UNKNOW_ERR,"未知错误,管理员正在修复!");
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public Result illegalArgumentException() {
        return new Result(null,Code.SAVE_ERR,"请正确选择上传文件!");
    }
}

