package xyz.qingmumu.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.qingmumu.dao.CommentDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Comment;
import xyz.qingmumu.entity.Result;

import java.util.HashMap;

/**
 * @Date:2023/8/11 0:11
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/user/comments")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class UserCommentController {
    @Autowired
    private CommentDao commentDao;

    /**
     * 获取所有的评论
     *
     * @return
     */
    @GetMapping
    @SaCheckRole("用户")
    public Result getAll(@RequestParam(defaultValue = "1") long current,
                         @RequestParam(defaultValue = "10") long size) {
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询指定字段
        commentLambdaQueryWrapper.select(Comment::getCommentId,Comment::getAvatar,Comment::getPostId,Comment::getUsername,Comment::getContent,Comment::getCreateTime,Comment::getAddress)
                .eq(Comment::getUserId, StpUtil.getLoginIdAsInt())
                .orderBy(true, false, Comment::getCreateTime);
        Page<Comment> commentPage = new Page<>(current, size);
        IPage<Comment> commentIPage = commentDao.selectPage(commentPage, commentLambdaQueryWrapper);
        HashMap<String, Object> userMap = new HashMap<>();
        if (commentIPage != null) {
            userMap.put("data", commentIPage.getRecords());
            userMap.put("pages", commentIPage.getPages());
            userMap.put("total", commentIPage.getTotal());
            return new Result(userMap, Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }
}
