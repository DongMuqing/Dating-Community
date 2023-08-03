package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.susu.damian.Code;
import com.susu.damian.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public Result getLoginIdByToken(@RequestParam("token") String token){
        // 获取指定 token 对应的账号id，如果未登录，则返回 null
        Object loginIdByToken = StpUtil.getLoginIdByToken(token);
        Integer code = loginIdByToken != null ? Code.GET_OK : Code.GET_ERR;
        String msg = loginIdByToken != null? "已登录" : "请先登录！";
        return new Result(null, code, msg);
    }
}
