package com.susu.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.susu.dao.UserDao;
import com.susu.entity.Code;
import com.susu.entity.Result;
import com.susu.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Date:2023/8/2 22:59
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/user/user")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class UserController {

    @Autowired
    private UserDao userDao;

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole("用户")
    public Result editInfo(@RequestBody User user) {
        //排除掉权限字段
        User newUserInfo = new User(user.getAvatar(), user.getEmail(), user.getUsername(), user.getPassword());
        userDao.update(newUserInfo, new UpdateWrapper<User>().lambda().eq(User::getId, user.getId()));
        return new Result(null, Code.UPDATE_OK, "修改信息成功！");
    }

    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
