package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.susu.entity.Code;
import com.susu.entity.Result;
import com.susu.entity.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date:2023/8/19 21:34
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/server")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class ServerController {

    /**
     * 服务器运行信息 cpu jvm Mem sys sysfile
     *
     * @return
     * @throws Exception
     */
    @GetMapping
    @SaCheckRole("管理员")
    public Result getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        return new Result(server, Code.GET_OK, "success");
    }


}
