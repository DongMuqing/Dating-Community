package com.susu.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.damian.User;
import com.susu.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @Date:2023/8/2 22:59
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/user")
@CrossOrigin
@Slf4j

public class UserController {
    @Autowired
    private UserDao userDao;

    @PostMapping
    @SaIgnore
    public Result Login(@RequestBody User user) {
        Integer code=null;
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword());
        List<User> users = userDao.selectList(wrapper);
        // 第一步：比对前端提交的账号名称、密码
        if(!users.isEmpty()) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(users.get(0));
            // 第2步，获取 Token  相关参数
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            code= Code.GET_OK;
            return new  Result(tokenInfo,code,"登录成功");
        }
        code= Code.GET_ERR;
        return new  Result(null,code,"登录失败");
    }

    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
