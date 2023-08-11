package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.damian.Code;
import com.susu.damian.Comment;
import com.susu.damian.Dynamic;
import com.susu.damian.Result;
import com.susu.dao.CommentDao;
import com.susu.dao.DynamicDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Date:2023/6/13 17:51
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/dynamic")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class DynamicController {
    @Autowired
    private DynamicDao dynamicDao;

    @Autowired
    private CommentDao commentDao;

    @GetMapping
    //忽略认证
    @SaIgnore
    public Result getAll() {
        //时间降序查询
        List<Dynamic> dynamics = dynamicDao.selectList(new QueryWrapper<Dynamic>().lambda().orderBy(true, false, Dynamic::getCreateTime));
        Integer code = dynamics != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dynamics != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(dynamics, code, msg);
    }

    /**
     * 动态的发布
     *
     * @return
     */
    @PostMapping
    public Result publish(@RequestBody Dynamic dynamic) {
        int flag = dynamicDao.insert(dynamic);
        Integer code = flag != 0 ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = flag != 0 ? "提交成功！" : "数据提交失败，请重试！";
        return new Result(null, code, msg);
    }

    @PostMapping("/DynamicAndComment")
    @SaIgnore
    /**
     * 获取动态以及评论
     */
    public Result getDynamicAndComment() {
        //时间降序查询
        List<Dynamic> dynamics = dynamicDao.selectList(new QueryWrapper<Dynamic>().lambda().orderBy(true, false, Dynamic::getCreateTime));
        List<Comment> comments = commentDao.selectList(new QueryWrapper<Comment>().lambda().orderBy(true, false, Comment::getCreateTime));
        Map<Integer, Dynamic> DynamicAndCommentMap = new HashMap<>();
        //数据量： 如果数据量很大，直接通过连接表查询可能更高效，因为减少了多次查询和数据传输的开销。
        //复杂性： 如果你需要进行复杂的数据整合和处理，通过Java整合可能更加灵活和易于管理。
        // 将 dynamics 转化为 Map，以便通过 ID 进行快速查找
        for (Dynamic dynamic : dynamics) {
            dynamic.setComments(new ArrayList<>()); // 初始化评论列表
            DynamicAndCommentMap.put(dynamic.getId(), dynamic);
        }
        // 将 comments 关联到 dynamics
        for (Comment comment : comments) {
            Dynamic dynamic = DynamicAndCommentMap.get(comment.getPostId());
            if (dynamic != null) {
                dynamic.getComments().add(comment);
            }
        }
        Integer code = dynamics != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dynamics != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(DynamicAndCommentMap, code, msg);
    }

}
