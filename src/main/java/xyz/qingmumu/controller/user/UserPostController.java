package xyz.qingmumu.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import xyz.qingmumu.dao.CommentDao;
import xyz.qingmumu.dao.PostDao;
import xyz.qingmumu.dao.UserDao;
import xyz.qingmumu.entity.*;
import xyz.qingmumu.util.AliOSSUtils;
import xyz.qingmumu.util.IpInfo;
import xyz.qingmumu.util.TimeUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/6/13 17:51
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/user/post")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class UserPostController {
    private static final String POST_FILE_PATH = "Post/";
    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping("/publish")
    @SaCheckRole("用户")
    public Result publishByUser(@RequestParam("content") String content,
                                @RequestParam("title") String title,
                                @RequestPart("files") String fileUrl,
                                HttpServletRequest request) throws Exception {
        if (content == null || content.trim().isEmpty()) {
            return new Result(null, Code.SAVE_ERR, "内容不可为空!");
        }
        if (title == null || title.trim().isEmpty()) {
            return new Result(null, Code.SAVE_ERR, "标题不可为空!");
        }
        if (fileUrl == null || fileUrl.trim().isEmpty()) {
            return new Result(null, Code.SAVE_ERR, "请上传图片！");
        }
        //添加发布地址
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        Integer userId = StpUtil.getLoginIdAsInt();
        LocalDateTime createTime = TimeUtil.getLocalDateTime();
        //查询用户信息
        User user = userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        //post对象
        Post post = new Post(userId, createTime, title, content, addressInfo, fileUrl, user.getAvatar(), user.getUsername());
        int flag = postDao.insert(post);
        Integer code = flag != 0 ? Code.SAVE_OK : Code.SAVE_ERR;
        String msg = flag != 0 ? "发布成功！" : "发布失败，请重试！";
        return new Result(null, code, msg);
    }

    /**
     * 获取当前用户的动态分页信息
     *
     * @param current
     * @param size
     * @return
     */
    @PostMapping("/paging")
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
    @PostMapping("/edit")
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
    @DeleteMapping("/delete")
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
