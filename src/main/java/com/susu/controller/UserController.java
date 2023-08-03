package com.susu.controller;

import cn.dev33.satoken.annotation.SaIgnore;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Result Login(@RequestBody Map<String, Object> map) {
        Integer code=null;
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername,map.get("username")).eq(User::getPassword,map.get("password"));
        List<User> users = userDao.selectList(wrapper);
        // 第一步：比对前端提交的账号名称、密码
        if(users!=null) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(users.get(0));
            code= Code.GET_OK;
            return new  Result(null,code,"登录成功");
        }
        code= Code.GET_ERR;
        return new  Result(null,code,"登录成功");
    }

    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
