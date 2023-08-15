package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.damian.Code;
import com.susu.damian.Comment;
import com.susu.damian.Result;
import com.susu.dao.CommentDao;
import com.susu.dao.PostDao;
import com.susu.util.AliOSSUtils;
import com.susu.util.IpInfo;
import com.susu.util.StringUtil;
import com.susu.util.UUIDUsernameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private PostDao postDao;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @GetMapping
    public Result getAll() {
        List<Comment> comments = commentDao.selectList(null);
        Integer code = comments != null ? Code.GET_OK : Code.GET_ERR;
        String msg = comments != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(comments, code, msg);
    }

    /**
     * 评论的添加
     * @param comment
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    @SaIgnore
    public Result add(@RequestBody Comment comment, HttpServletRequest request) throws Exception {
        // (3, 1, 'qing', '哈哈哈', '2022-02-03 00:00:00','上海');
        //commentid 动态id 用户名 评论 时间 地址
        //未登录 就随机一个游客名字
        String username = "";
        String avatar = "";
        Object loginIdByToken = StpUtil.getLoginIdByToken(StpUtil.getTokenValue());
        if (loginIdByToken != null) {
            String loginId = (String) StpUtil.getTokenInfo().loginId;
            username = StringUtil.extractValue(loginId, "username=");
            avatar = StringUtil.extractValue(loginId, "avatar=");
        } else {
            //未登录游客
            username = "游客" + UUIDUsernameUtil.generateRandomUsername();
            //随机一个头像
            List<String> userpics = aliOSSUtils.ListRequest("Userpics");
            int index = (int) (Math.random() * userpics.size());
            avatar = userpics.get(index);
        }
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        //插入评论对象
        Comment comments = new Comment(comment.getPostId(), avatar, username, comment.getContent(), comment.getCreateTime(), addressInfo);
        int flag = commentDao.insert(comments);
        //获取对应动态的评论并返回 用于更新界面评论数据
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getPostId, comments.getPostId()).orderBy(true, false, Comment::getCreateTime);
        List<Comment> comments1 = commentDao.selectList(wrapper);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "发送成功" : "发送失败，请重试！";
        return new Result(comments1, code, msg);
    }

    @DeleteMapping
    public Result delete(@RequestBody Integer id){
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.lambda().eq(Comment::getCommentId, id);
        int delete = commentDao.delete(query);
        List<Comment> comments = commentDao.selectList(null);
        Integer code = delete != 0? Code.DELETE_OK: Code.DELETE_ERR;
        String msg = delete != 0 ? "删除成功" : "删除失败，请重试！";
        return new Result(comments,code,msg);
    }
}
