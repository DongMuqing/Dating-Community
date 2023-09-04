package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.susu.entity.Code;
import com.susu.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date:2023/8/3 21:26
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/token")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class TokenController {

    @PostMapping
    public Result getLoginIdByToken(){
        // 获取指定 token 对应的账号id，如果未登录，则返回 null
        Object loginIdByToken = StpUtil.getLoginIdByToken(StpUtil.getTokenValue());
        Integer code = loginIdByToken != null ? Code.GET_OK : Code.GET_ERR;
        String msg = loginIdByToken != null? "已登录" : "请先登录！";
        return new Result(null, code, msg);
    }
}
