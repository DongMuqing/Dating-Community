package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.susu.damian.*;
import com.susu.dao.CommentDao;
import com.susu.dao.PostDao;
import com.susu.util.IpInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Date:2023/6/13 17:51
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/post")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class PostController {
    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private ResourceLoader resourceLoader;
    @GetMapping
    public Result getAll() {
        //时间降序查询
        List<Post> posts = postDao.selectList(new QueryWrapper<Post>().lambda().orderBy(true, false, Post::getCreateTime));
        Integer code = posts != null ? Code.GET_OK : Code.GET_ERR;
        String msg = posts != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(posts, code, msg);
    }

    /**
     * 动态的发布
     *
     * @return
     */
    @PostMapping
    public Result publish(@RequestBody Post post, HttpServletRequest request) throws Exception {
        //添加发布地址
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        post.setLocation(addressInfo);
        int flag = postDao.insert(post);
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
        List<Post> posts = postDao.selectList(new QueryWrapper<Post>().lambda().orderBy(true, false, Post::getCreateTime));
        List<Comment> comments = commentDao.selectList(new QueryWrapper<Comment>().lambda().orderBy(true, false, Comment::getCreateTime));
        Map<Integer, Post> DynamicAndCommentMap = new HashMap<>();
        //数据量： 如果数据量很大，直接通过连接表查询可能更高效，因为减少了多次查询和数据传输的开销。
        //复杂性： 如果你需要进行复杂的数据整合和处理，通过Java整合可能更加灵活和易于管理。
        // 将 dynamics 转化为 Map，以便通过 ID 进行快速查找
        //为什么i不能为0？ 为0时第一条动态评论为空？
        int i=0;
        for (Post post : posts) {
            post.setComments(new ArrayList<>()); // 初始化评论列表
            for (Comment comment : comments) {
                if (post.getId().equals(comment.getPostId())) {
                    post.getComments().add(comment);
                }
            }
        }
        // 将 comments 关联到 dynamics
        Integer code = posts != null ? Code.GET_OK : Code.GET_ERR;
        String msg = posts != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(posts, code, msg);
    }

    @PostMapping("/upvote")
    //忽略认证
    @SaIgnore
    /**
     * 只需要接受一个动态id和点赞数来进行更新数据
     */
    public Result upvote(@RequestBody Post post){
        UpdateWrapper<Post> updateWrapper =new UpdateWrapper<>();
        updateWrapper.lambda().eq(Post::getId, post.getId()).
                               set(Post::getUpvoteNum, post.getUpvoteNum());
        int update = postDao.update(null, updateWrapper);
        Integer code = update != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = update != 0 ? "点赞成功" : "点赞失败，QAQ！";
        return new Result(null,code,msg);
    }

    @DeleteMapping("/delete")
    public Result deleteById(@RequestBody Integer id){
        //存在外键 先删除对应评论
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.lambda().eq(Comment::getPostId, id);
        commentDao.delete(query);
        Integer flag = postDao.deleteById(id);
        //返回数据更新页面数据
        List<Post> posts = postDao.selectList(null);
        Integer code = flag != 0 ? Code.DELETE_OK : Code.DELETE_ERR;
        String msg = flag != 0 ? "删除成功" : "删除失败，QAQ！";
        return new Result(posts,code,msg);
    }

    @PostMapping("/edit")
    public Result edits(@RequestBody Post post){
        int flag= postDao.updateById(post);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "修改成功" : "修改失败，QAQ！";
        List<Post> posts = postDao.selectList(null);
        return new Result(posts,code,msg);
    }

    /**
     * post的分页查询
     * 默认查询第一页 每页大小为10条数据
     * @param current 查询第几页
     * @param size 每页的大小
     * @return
     */
    @PostMapping("/paging")
    public Result paging(@RequestParam(defaultValue = "1") long current,
                         @RequestParam(defaultValue = "10") long size){
        LambdaQueryWrapper<Post> postLambdaQueryWrapper = Wrappers.lambdaQuery();
        postLambdaQueryWrapper.orderBy(true, false, Post::getCreateTime);
        Page<Post> postPage = new Page<>(current , size);
        IPage<Post> postIPage = postDao.selectPage(postPage , postLambdaQueryWrapper);
        HashMap<String, Object> postMap=new HashMap<>();
        if (postIPage!=null){
            postMap.put("data",postIPage.getRecords());
            postMap.put("pages",postIPage.getPages());
            postMap.put("total",postIPage.getTotal());
            return new Result(postMap,Code.GET_OK,"查询成功");
        }else{
            return new Result(null,Code.GET_ERR,"查询失败");
        }
    }
}
