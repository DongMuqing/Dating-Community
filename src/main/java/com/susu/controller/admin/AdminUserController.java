package com.susu.controller.admin;

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

import java.util.HashMap;

/**
 * @Date:2023/8/2 22:59
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/admin/user")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AdminUserController {

    @Autowired
    private UserDao userDao;
    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    @PostMapping("/getInfo/{id}")
    public Result getInfoById( @PathVariable("id") Integer id) {
        int loginId= StpUtil.getLoginIdAsInt();
        if (loginId==id) {
            User user = userDao.selectById(id);
            if (user != null) {
                HashMap<String, Object> userInfo = new HashMap<>();
                userInfo.put("username", user.getUsername());
                userInfo.put("avatar", user.getAvatar());
                userInfo.put("logintime", user.getLoginTime());
                userInfo.put("role", user.getRole());
                return new Result(userInfo, Code.GET_OK, "查询成功!");
            } else {
                return new Result(null, Code.GET_ERR, "查询失败!");
            }
        } else {
            return new Result(null, Code.NO_PERMISSION, "权限不够,请联系管理员！");
        }
    }

    /**
     * 修改用户信息 可进行提权
     * @param user
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole("管理员")
    public Result edit(@RequestBody User user){
        userDao.update(user,new UpdateWrapper<User>().lambda().eq(User::getId,user.getId()));
        return new Result(null, Code.UPDATE_OK,"修改用户信息成功！");
    }
    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
