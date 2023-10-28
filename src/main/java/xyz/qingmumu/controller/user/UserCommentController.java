package xyz.qingmumu.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.jna.platform.unix.LibC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.qingmumu.dao.CommentDao;
import xyz.qingmumu.dao.PostDao;
import xyz.qingmumu.dao.UserDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Comment;
import xyz.qingmumu.entity.Post;
import xyz.qingmumu.entity.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    private PostDao postDao;
    @Autowired
    private UserDao userDao;
    /**
     * 获取所有的评论
     *
     * @return
     */
    @GetMapping
    @SaCheckRole("用户")
    public Result getAll(@RequestParam(defaultValue = "1") long current,
                         @RequestParam(defaultValue = "10") long size) {
        //1:查当前用户的动态id
        List<Post> posts = postDao.selectList(new QueryWrapper<Post>().lambda().eq(Post::getUserId, StpUtil.getLoginIdAsInt()).select(Post::getId));
        List<String> postId = new ArrayList<>();
        for (Post post : posts) {
            postId.add(String.valueOf(post.getId()));
        }
        //in条件 评论是否在当前用户的动态里面
//        inSql(R column, String inValue)
//        inSql(boolean condition, R column, String inValue)
//        例: inSql("age", "1,2,3,4,5,6")--->age in (1,2,3,4,5,6)
//        例: inSql("id", "select id from table where id < 3")--->id in (select id from table where id < 3)
        String in = String.join(",", postId);
        Page<Comment> commentPage = new Page<>(current, size);
        LambdaQueryWrapper<Comment> userCommentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userCommentLambdaQueryWrapper.select(Comment::getCommentId,Comment::getAvatar,Comment::getPostId,Comment::getUsername,Comment::getContent,Comment::getCreateTime,Comment::getAddress)
                .in(Comment::getPostId,in)
                .orderBy(true, false, Comment::getCreateTime);
        IPage<Comment> commentIPage = commentDao.selectPage(commentPage, userCommentLambdaQueryWrapper);
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
