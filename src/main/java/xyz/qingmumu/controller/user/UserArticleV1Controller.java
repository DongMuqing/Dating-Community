package xyz.qingmumu.controller.user;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.qingmumu.dao.ArticleDao;
import xyz.qingmumu.entity.Article;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/6/9 17:46
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/user/article")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class UserArticleV1Controller {
    @Autowired
    private ArticleDao articleDao;



    @PostMapping("/add")
    @SaCheckRole("用户")
    public Result addArticle(@RequestBody Article articles) {
        articles.setUserId(StpUtil.getLoginIdAsInt());
        int flag = articleDao.insert(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "添加成功" : "数据添加失败，请重试！";
        return new Result(null, code, msg);
    }

    /**
     * 删除用户文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @SaCheckRole("用户")
    public Result delArticleByUserId(@RequestParam("id") Integer id) {
        if (isCurrentUser(articleDao.selectById(id).getUserId())) {
            int flag = articleDao.deleteById(id);
            Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
            String msg = flag != 0 ? "删除成功" : "数据删除失败，请重试！";
            return new Result(null, code, msg);
        } else {
            return new Result(null, Code.DELETE_ERR, "删除失败！");
        }
    }

    /**
     * 修改用户自己的文章信息
     *
     * @param articles
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole("用户")
    public Result editArticleByUserId(@RequestBody Article articles) {
        if (isCurrentUser(articleDao.selectById(articles.getId()).getUserId())) {
            int flag = articleDao.updateById(articles);
            Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
            String msg = flag != 0 ? "修改成功" : "数据修改失败，请重试！";
            return new Result(null, code, msg);
        } else {
            return new Result(null, Code.SAVE_ERR, "数据修改失败！");
        }
    }

    /**
     * 获取用户自己的文章数据
     *
     * @return
     */
    @GetMapping("/get")
    @SaCheckRole("用户")
    public Result getArticleByUserId(@RequestParam(defaultValue = "1") long current,
                                     @RequestParam(defaultValue = "10") long size) {
       return getUserArticle(current, size);
    }

    /**
     * 操作的数据是否是当前用户的
     *
     * @param id 被操作数据的用户id
     * @return
     */
    public boolean isCurrentUser(Integer id) {
        return id.equals(StpUtil.getLoginIdAsInt());
    }

    /**
     * 查询当前用户的文章并按时间降序排
     * @return
     */
    public Result getUserArticle(long current,long size) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = Wrappers.lambdaQuery();
        articleLambdaQueryWrapper.eq(Article::getUserId,StpUtil.getLoginIdAsInt()).orderBy(true, false, Article::getCreateTime);
        Page<Article> articlePage = new Page<>(current, size);
        IPage<Article> articleIPage = articleDao.selectPage(articlePage, articleLambdaQueryWrapper);
        HashMap<String, Object> postMap = new HashMap<>();
        if (articleIPage != null) {
            postMap.put("data", articleIPage.getRecords());
            postMap.put("pages", articleIPage.getPages());
            postMap.put("total", articleIPage.getTotal());
            return new Result(postMap, Code.GET_OK, "查询成功");
        } else {
            return new Result(null, Code.GET_ERR, "查询失败");
        }
    }
}