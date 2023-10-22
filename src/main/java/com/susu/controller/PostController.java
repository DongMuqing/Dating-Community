package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.susu.dao.CommentDao;
import com.susu.dao.PostDao;
import com.susu.entity.Code;
import com.susu.entity.Comment;
import com.susu.entity.Post;
import com.susu.entity.Result;
import com.susu.util.AliOSSUtils;
import com.susu.util.IOUtil;
import com.susu.util.IpInfo;
import com.susu.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private static final String POST_FILE_PATH = "post";
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 获取所有动态
     *
     * @return 所有动态
     */
    @GetMapping
    public Result getAll() {
        //时间降序查询
        List<Post> posts = postDao.selectList(new QueryWrapper<Post>().lambda().orderBy(true, false, Post::getCreateTime));
        Integer code = posts != null ? Code.GET_OK : Code.GET_ERR;
        String msg = posts != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(posts, code, msg);
    }

    /**
     * 只需要接受一个动态id和点赞数来进行更新数据
     */
    @PostMapping("/upvote")
    @SaIgnore
    public Result upvote(@RequestBody Post post) {
        UpdateWrapper<Post> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Post::getId, post.getId()).
                set(Post::getUpvoteNum, post.getUpvoteNum());
        int update = postDao.update(null, updateWrapper);
        Integer code = update != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = update != 0 ? "点赞成功" : "点赞失败，QAQ！";
        return new Result(null, code, msg);
    }

    /**
     * @return 动态以及评论
     */
    @PostMapping("/DynamicAndComment")
    @SaIgnore
    public Result getDynamicAndComment() {
        //时间降序查询
        List<Post> posts = postDao.selectList(new QueryWrapper<Post>().lambda().orderBy(true, false, Post::getCreateTime));
        List<Comment> comments = commentDao.selectList(new QueryWrapper<Comment>().lambda().orderBy(true, false, Comment::getCreateTime));
        //数据量： 如果数据量很大，直接通过连接表查询可能更高效，因为减少了多次查询和数据传输的开销。
        //复杂性： 如果你需要进行复杂的数据整合和处理，通过Java整合可能更加灵活和易于管理。
        // 将 dynamics 转化为 Map，以便通过 ID 进行快速查找
        // 将 comments 关联到 post
        for (Post post : posts) {
            post.setComments(new ArrayList<>()); // 初始化评论列表
            for (Comment comment : comments) {
                if (post.getId().equals(comment.getPostId())) {
                    post.getComments().add(comment);
                }
            }
        }
        // 将 comments 关联到 dynamics
        return new Result(posts, Code.GET_OK, "查询成功");
    }

    /**
     * 动态的发布
     *
     * @param content 内容
     * @param title   标题
     * @param files   动态图片资源
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/publish")
    //满足角色权限其一即可
    @SaCheckPermission(orRole = {"管理员", "用户"})
    public Result publishByUser(@RequestParam("content") String content,
                                @RequestParam("title") String title,
                                @RequestPart("files") MultipartFile[] files,
                                HttpServletRequest request) throws Exception {
        if (content == null || content.trim().isEmpty()) {
            return new Result(null, Code.SAVE_ERR, "内容不可为空!");
        }
        if (title == null || title.trim().isEmpty()) {
            return new Result(null, Code.SAVE_ERR, "标题不可为空!");
        }
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                return new Result(null, Code.SAVE_ERR, "请选择图片!");
            }
            if (!IOUtil.isImage(file)) {
                return new Result(null, Code.SAVE_ERR, "请上传jpeg,png格式的图片!");
            }
        }
        List<String> fileUrl = new ArrayList<>();
        //将动态资源上传，返回其地址添加到post对象中
        for (MultipartFile file : files) {
            String url = aliOSSUtils.upload(file, POST_FILE_PATH);
            fileUrl.add(url);
        }
        Post post = new Post(TimeUtil.getLocalDateTime(), title, content);
        //添加发布地址
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        post.setLocation(addressInfo);
        //添加发布用户的id
        post.setUserId(Integer.valueOf((String) StpUtil.getLoginId()));
        post.setImgSrcList(fileUrl.toString());
        int flag = postDao.insert(post);
        Integer code = flag != 0 ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = flag != 0 ? "发布成功！" : "发布失败，请重试！";
        return new Result(null, code, msg);
    }

    /**
     * 根据id删除动态
     *
     * @param id 动态id
     * @return 返回删除后的新数据
     */
    @DeleteMapping("/delete")
    @SaCheckRole("管理员")
    public Result deleteById(@RequestParam("id") Integer id) {
        List<Post> posts = deletePost(id);
        return new Result(posts, Code.DELETE_OK, "删除成功");
    }

    @PostMapping("/edit")
    @SaCheckRole("管理员")
    public Result edits(@RequestBody Post post) {
        int flag = postDao.updateById(post);
        Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
        String msg = flag != 0 ? "修改成功" : "修改失败，QAQ！";
        List<Post> posts = postDao.selectList(null);
        return new Result(posts, code, msg);
    }

    /**
     * 所有post数据的分页查询
     * 默认查询第一页 每页大小为10条数据
     *
     * @param current 查询第几页
     * @param size    每页的大小
     * @return
     */
    @PostMapping("/paging")
    @SaCheckRole("管理员")
    public Result pagingAll(@RequestParam(defaultValue = "1") long current,
                            @RequestParam(defaultValue = "10") long size) {
        LambdaQueryWrapper<Post> postLambdaQueryWrapper = Wrappers.lambdaQuery();
        postLambdaQueryWrapper.orderBy(true, false, Post::getCreateTime);
        Page<Post> postPage = new Page<>(current, size);
        IPage<Post> postIPage = postDao.selectPage(postPage, postLambdaQueryWrapper);
        HashMap<String, Object> postMap = new HashMap<>();
        if (postIPage != null) {
            postMap.put("data", postIPage.getRecords());
            postMap.put("pages", postIPage.getPages());
            postMap.put("total", postIPage.getTotal());
            return new Result(postMap, Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }

    /**
     * 获取当前用户的动态分页信息
     *
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/paging/user")
    @SaCheckRole("用户")
    public Result pagingByUserId(@RequestParam(defaultValue = "1") long current,
                                 @RequestParam(defaultValue = "10") long size) {
        if (pagingPost(current, size) != null) {
            return new Result(pagingPost(current, size), Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }

    /**
     * 用户修改动态
     *
     * @param post 新动态信息
     * @return
     */
    @PostMapping("/edit/user")
    @SaCheckRole("用户")
    public Result editsByUserId(@RequestBody Post post) {
        //当前用户只能修改自己的数据
        if (isCurrentUser(postDao.selectById(post.getId()).getUserId())) {
            int flag = postDao.updateById(post);
            Integer code = flag != 0 ? Code.UPDATE_OK : Code.UPDATE_ERR;
            String msg = flag != 0 ? "修改成功" : "修改失败，QAQ！";
            //返回修改后的新信息
            LambdaQueryWrapper<Post> postLambdaQueryWrapper = Wrappers.lambdaQuery();
            postLambdaQueryWrapper.eq(Post::getUserId, StpUtil.getLoginId()).orderBy(true, false, Post::getCreateTime);
            List<Post> posts = postDao.selectList(postLambdaQueryWrapper);
            return new Result(posts, code, msg);
        } else {
            return new Result(null, Code.SAVE_ERR, "修改失败！");
        }
    }

    /**
     * @param id 动态id
     * @return 删除后的数据
     */
    @DeleteMapping("/delete/user")
    @SaCheckRole("用户")
    public Result deleteByUserId(@RequestParam("id") Integer id) {
        //当前用户只能删除自己的数据
        if (isCurrentUser(postDao.selectById(id).getUserId())) {
            List<Post> posts = deletePost(id);
            return new Result(posts, Code.DELETE_OK, "删除成功!");
        } else {
            return new Result(null, Code.DELETE_ERR, "删除失败！");
        }
    }

    /**
     * 动态删除
     *
     * @param id
     * @return
     */
    public List<Post> deletePost(Integer id) {
        //存在外键 先删除对应评论
        QueryWrapper<Comment> query = new QueryWrapper<>();
        query.lambda().eq(Comment::getPostId, id);
        commentDao.delete(query);
        postDao.deleteById(id);
        //返回数据更新页面数据
        LambdaQueryWrapper<Post> postLambdaQueryWrapper = Wrappers.lambdaQuery();
        postLambdaQueryWrapper.eq(Post::getUserId, StpUtil.getLoginId()).orderBy(true, false, Post::getCreateTime);
        return postDao.selectList(postLambdaQueryWrapper);
    }

    /**
     * 用户分页动态数据
     *
     * @param current
     * @param size
     * @return
     */
    public HashMap<String, Object> pagingPost(long current, long size) {
        LambdaQueryWrapper<Post> postLambdaQueryWrapper = Wrappers.lambdaQuery();
        postLambdaQueryWrapper.eq(Post::getUserId, StpUtil.getLoginId()).orderBy(true, false, Post::getCreateTime);
        Page<Post> postPage = new Page<>(current, size);
        IPage<Post> postIPage = postDao.selectPage(postPage, postLambdaQueryWrapper);
        HashMap<String, Object> postMap = new HashMap<>();
        if (postIPage != null) {
            postMap.put("data", postIPage.getRecords());
            postMap.put("pages", postIPage.getPages());
            postMap.put("total", postIPage.getTotal());
        } else {
            return null;
        }
        return postMap;
    }

    /**
     * 操作的数据是否是当前用户的
     *
     * @param id 被操作数据的用户id
     * @return
     */
    public boolean isCurrentUser(Integer id) {
        if (id.equals(Integer.valueOf((String) StpUtil.getLoginId()))) {
            return true;
        }
        return false;
    }
}
