package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import xyz.qingmumu.dao.CommentDao;
import xyz.qingmumu.dao.PostDao;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Comment;
import xyz.qingmumu.entity.Post;
import xyz.qingmumu.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @Autowired
    private PostDao postDao;
    @Autowired
    private CommentDao commentDao;


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
}
