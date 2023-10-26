package xyz.qingmumu.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.qingmumu.dao.UserDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import xyz.qingmumu.entity.User;
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
    @SaCheckRole("管理员")
    public Result getInfoById(@PathVariable("id") Integer id) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getId, id)
                .select(User::getUsername, User::getRole, User::getEmail, User::getLoginTime, User::getAvatar);
        User user = userDao.selectOne(userLambdaQueryWrapper);
        return new Result(user, Code.GET_OK, "查询成功!");
    }


    /**
     * 修改用户信息 进行提权
     * @param user
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole("管理员")
    public Result edit(@RequestBody User user) {
        userDao.update(user, new UpdateWrapper<User>().lambda().eq(User::getId, user.getId()));

        return new Result(null, Code.UPDATE_OK, "修改用户信息成功！");
    }

    /**
     * 根据id删除用户
     * @param id 用户id
     * @return
     */
    @DeleteMapping("/delete")
    @SaCheckRole("管理员")
    public Result delete(@RequestParam("id") Integer id) {
        userDao.deleteById(id);
        return new Result(null, Code.UPDATE_OK, "删除成功！");
    }

    /**
     * 所有用户分页信息
     *
     * @return
     */
    @PostMapping("/userInfo")
    @SaCheckRole("管理员")
    public Result userInfo(@RequestParam(defaultValue = "1") long current,
                           @RequestParam(defaultValue = "10") long size) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询指定字段
        userLambdaQueryWrapper.select(User::getId, User::getAvatar, User::getEmail, User::getUsername, User::getLoginTime, User::getRole)
                .orderBy(true, false, User::getLoginTime);
        Page<User> userPage = new Page<>(current, size);
        IPage<User> userIPage = userDao.selectPage(userPage, userLambdaQueryWrapper);
        HashMap<String, Object> userMap = new HashMap<>();
        if (userIPage != null) {
            userMap.put("data", userIPage.getRecords());
            userMap.put("pages", userIPage.getPages());
            userMap.put("total", userIPage.getTotal());
            return new Result(userMap, Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }


    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
