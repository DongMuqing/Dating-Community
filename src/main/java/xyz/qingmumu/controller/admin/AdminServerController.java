package xyz.qingmumu.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import xyz.qingmumu.entity.Server;
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
@RequestMapping("api/{version}/admin/server")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AdminServerController {

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
