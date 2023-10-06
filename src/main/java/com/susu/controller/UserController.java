package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.dao.UserDao;
import com.susu.entity.Code;
import com.susu.entity.Comment;
import com.susu.entity.Result;
import com.susu.entity.User;
import com.susu.service.EmailService;
import com.susu.util.VerCodeGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    @Autowired
    private EmailService emailService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping
    public Result Login(@RequestBody User user) {
        Integer code = null;
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword());
        User loginUser= userDao.selectOne(wrapper);
        // 第一步：比对前端提交的账号名称、密码
        if (!loginUser.equals("")) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(loginUser.getId());
            // 第2步，获取 Token  相关参数
            SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
            return new Result(tokenInfo, Code.GET_OK, "登录成功");
        }
        code = Code.GET_ERR;
        return new Result(null, code, "登录失败");
    }

    /**
     * 用户的注册
     * @param email 邮箱
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @return
     */
    @PostMapping("register")
    @SaIgnore
    public Result register(@RequestParam("email") String email,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("code") String code) {
        boolean isEmail = Validator.isEmail(email);
        if (isEmail) {
            // 从Redis中检索相应用户的验证码
            String storedCode = stringRedisTemplate.opsForValue().get("Code" + email);
            // 检查验证码是否匹配
            if (storedCode != null && storedCode.equals(code)) {
                // 验证通过，执行注册逻辑，例如将用户信息保存到数据库
                User user = new User(email, username, password);
                int insert = userDao.insert(user);
                return new Result(null, Code.GET_OK, "注册成功！");
            } else {
                // 验证失败，返回相应的错误消息或状态码
                return new Result(null, Code.GET_ERR, "验证码不正确！");
            }
        } else {
            return new Result(null, Code.GET_ERR, "请输入合法邮箱！");
        }
    }

    /**
     * 验证码的发送
     *验证码过期时间为10分钟
     * @param email 邮箱地址 存入redis中 在注册操作中验证
     * @return
     * @throws MessagingException
     */
    @PostMapping("code")
    @SaIgnore
    public Result sendCode(@RequestParam("email") String email,
                           @RequestParam("username")String username) throws MessagingException {
        boolean isEmail = Validator.isEmail(email);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(User::getUsername,username);
        User isUser = userDao.selectOne(wrapper);
        if (isEmail) {
            //用户名不存在则可以发送验证码注册
            if (isUser==null) {
                String storedCode = stringRedisTemplate.opsForValue().get("Code" + email);
                if (storedCode==null) {
                    String verCode = VerCodeGenerateUtil.getVerCode();
        //        注意set方法的参数,需要指定过期时间的单位
                    stringRedisTemplate.opsForValue().set("Code" + email, verCode, 10, TimeUnit.MINUTES);
                    emailService.sendVerificationCode(email, verCode);
                    return new Result(null, Code.GET_OK, "验证码以发送,请查收！");
                } else {
                    return new Result(null, Code.GET_ERR, "请勿重复发送验证码！");
                }
            } else {
                return new Result(null, Code.GET_ERR, "用户名以存在！");
            }
        } else {
            return new Result(null, Code.GET_ERR, "请输入合法邮箱！");
        }
    }

    @PostMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
}
