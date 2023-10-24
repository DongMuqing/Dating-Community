package com.susu.controller.admin;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.susu.dao.CommentDao;
import com.susu.dao.PostDao;
import com.susu.dao.UserDao;
import com.susu.entity.*;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/6/13 17:51
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/admin/post")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class AdminPostController {
    private static final String POST_FILE_PATH = "Post/";
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
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
    @SaCheckRole("管理员")
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
     * @param content 内容
     * @param title   标题
     * @param files   动态图片资源
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/publish")
    @SaCheckRole("管理员")
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
        //添加发布地址
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        Integer userId = Integer.valueOf((String) StpUtil.getLoginId());
        // 图片资源地址
        String fileUrlString = String.join(",", fileUrl);
        LocalDateTime createTime = TimeUtil.getLocalDateTime();
        //查询用户信息
        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        //post对象
        Post post = new Post(userId, createTime, title, content, addressInfo, fileUrlString, user.getAvatar(), user.getUsername());
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
