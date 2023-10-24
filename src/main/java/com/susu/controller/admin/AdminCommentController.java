package com.susu.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.dao.CommentDao;
import com.susu.entity.Code;
import com.susu.entity.Comment;
import com.susu.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Date:2023/8/11 0:11
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/admin/comments")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AdminCommentController {
    @Autowired
    private CommentDao commentDao;

    /**
     * 获取所有的评论
     *
     * @return
     */
    @GetMapping
    @SaCheckRole("管理员")
    public Result getAll() {
        List<Comment> comments = commentDao.selectList(new QueryWrapper<Comment>().lambda().orderBy(true, false, Comment::getCreateTime));
        Integer code = comments != null ? Code.GET_OK : Code.GET_ERR;
        String msg = comments != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(comments, code, msg);
    }

    /**
     * 管理员根据id删除评论
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @SaCheckRole("管理员")
    public Result delete(@RequestBody Integer id) {
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.lambda().eq(Comment::getCommentId, id);
        int delete = commentDao.delete(query);
        List<Comment> comments = commentDao.selectList(null);
        Integer code = delete != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = delete != 0 ? "删除成功" : "删除失败，请重试！";
        return new Result(comments, code, msg);
    }
}
