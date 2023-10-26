package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import xyz.qingmumu.dao.CommentDao;
import xyz.qingmumu.dao.UserDao;
import xyz.qingmumu.entity.*;
import xyz.qingmumu.util.AliOSSUtils;
import xyz.qingmumu.util.IpInfo;
import xyz.qingmumu.util.UUIDUsernameUtil;

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
    //游客头像地址
    private static final String USER_PICS_PATH = "Userpics/";
    @Autowired
    private UserDao userDao;
    @Autowired
    private CommentDao commentDao;
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * 评论的添加
     *
     * @param comment
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/add")
    @SaIgnore
    public Result add(@RequestBody Comment comment, HttpServletRequest request) throws Exception {
        if (comment.getContent() == null) {
            return new Result(null, Code.SAVE_ERR, "评论不可为空！");
        }
        // (3, 1, 'qing', '哈哈哈', '2022-02-03 00:00:00','上海');
        //commentid 动态id 用户名 评论 时间 地址
        //未登录 就随机一个游客名字
        if (StpUtil.isLogin()) {
            int loginId = StpUtil.getLoginIdAsInt();
            User user = userDao.selectOne(new QueryWrapper<User>().lambda().eq(User::getId, loginId));
            comment.setUsername(user.getUsername());
            comment.setAvatar(user.getAvatar());
        } else {
            //未登录游客
            comment.setUsername("游客" + UUIDUsernameUtil.generateRandomUsername());
            //随机一个头像
            List<AliOss> userpics = aliOSSUtils.ListRequest(USER_PICS_PATH);
            int index = (int) (Math.random() * userpics.size());
            comment.setAvatar(userpics.get(index).getPath());
        }
        String addressInfo = IpInfo.getAddress(request, resourceLoader);
        comment.setAddress(addressInfo);
        int flag = commentDao.insert(comment);
        //获取对应动态的评论并返回 用于更新界面评论数据
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Comment::getPostId, comment.getPostId()).orderBy(true, false, Comment::getCreateTime);
        List<Comment> comments1 = commentDao.selectList(wrapper);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "发送成功" : "发送失败，请重试！";
        return new Result(comments1, code, msg);
    }
}
