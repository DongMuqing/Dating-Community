package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.damian.*;
import com.susu.dao.CommentDao;
import com.susu.dao.DynamicDao;
import com.susu.util.AliOSSUtils;
import com.susu.util.IpInfo;
import com.susu.util.UUIDUsernameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Date:2023/8/11 0:11
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/comments")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class CommentController {
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private DynamicDao dynamicDao;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @GetMapping
    @SaIgnore
    public Result getAll() {
        List<Comment> comments = commentDao.selectList(null);
        Integer code = comments != null ? Code.GET_OK : Code.GET_ERR;
        String msg = comments != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(comments, code, msg);
    }

    @PostMapping("/add")
    @SaIgnore
    public Result add(@RequestBody Comment comment, HttpServletRequest request) throws Exception {
        // (3, 1, 'qing', '哈哈哈', '2022-02-03 00:00:00','上海');
        //commentid 动态id 用户名 评论 时间 地址
        //未登录 就随机一个游客名字
        String username = "";
        Object loginIdByToken = StpUtil.getLoginIdByToken(StpUtil.getTokenValue());
        if (loginIdByToken != null) {
            String loginId = (String) StpUtil.getTokenInfo().loginId;
            // 使用正则表达式匹配username的部分
            String usernamePattern = "username=([^,\\s]+)";
            Pattern pattern = Pattern.compile(usernamePattern);
            Matcher matcher = pattern.matcher(loginId);
            if (matcher.find()) {
                username = matcher.group(1);
            }
        } else {
            username = "游客" + UUIDUsernameUtil.generateRandomUsername();
        }
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        //随机一个头像
        List<String> userpics = aliOSSUtils.ListRequest("Userpics");
        int index = (int) (Math.random()* userpics.size());
        String avatar=userpics.get(index);
        //插入评论对象
        Comment comments = new Comment(comment.getPostId(),avatar, username, comment.getContent(), comment.getCreateTime(), addressInfo);
        int flag = commentDao.insert(comments);
        //获取对应动态的评论并返回 用于更新界面评论数据
        QueryWrapper<Comment> wrapper=new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getPostId,comments.getPostId()).orderBy(true, false, Comment::getCreateTime);
        List<Comment> comments1 = commentDao.selectList(wrapper);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "发送成功" : "发送失败，请重试！";
        return new Result(comments1, code, msg);
    }
}
