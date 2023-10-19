package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.dao.ArticleDao;
import com.susu.entity.Article;
import com.susu.entity.Code;
import com.susu.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Date:2023/6/9 17:46
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/article")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class ArticleV1Controller {
    @Autowired
    private ArticleDao articleDao;

    @GetMapping
    @SaIgnore
    public Result getAll() {
        List<Article> articles = articleDao.selectList(new QueryWrapper<Article>().lambda().orderBy(true, false, Article::getCreateTime));
        Integer code = articles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = articles != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(articles, code, msg);
    }

    @PostMapping("/add")
    @SaCheckRole("管理员,用户")
    public Result addArticle(@RequestBody Article articles) {
        articles.setUserId((Integer) StpUtil.getLoginId());
        int flag = articleDao.insert(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "添加成功" : "数据添加失败，请重试！";
        return new Result(null, code, msg);
    }


    @DeleteMapping("/del")
    @SaCheckRole("管理员")
    public Result delArticle(@RequestBody Article articles) {
        int flag = articleDao.deleteById(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "删除成功" : "数据删除失败，请重试！";
        List<Article> getArticles = articleDao.selectList(null);
        return new Result(getArticles, code, msg);
    }

    @PostMapping("/edit")
    @SaCheckRole("管理员")
    public Result editArticle(@RequestBody Article articles) {
        int flag = articleDao.updateById(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "修改成功" : "数据修改失败，请重试！";
        List<Article> getArticles = articleDao.selectList(null);
        return new Result(getArticles, code, msg);
    }

    @DeleteMapping("/del/id")
    @SaCheckRole("用户")
    public Result delArticleByUserId(@RequestBody Article articles) {
        if (articleDao.selectById(articles.getId()).getUserId() == StpUtil.getLoginId()) {
            int flag = articleDao.deleteById(articles);
            Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
            String msg = flag != 0 ? "删除成功" : "数据删除失败，请重试！";
            List<Article> getArticles = articleDao.selectList(new QueryWrapper<Article>().lambda().eq(Article::getUserId, StpUtil.getLoginId()));
            return new Result(getArticles, code, msg);
        } else {
            return new Result(null, Code.DELETE_ERR, "删除失败！");
        }
    }

    @PostMapping("/edit/id")
    @SaCheckRole("用户")
    public Result editArticleByUserId(@RequestBody Article articles) {
        if (articleDao.selectById(articles.getId()).getUserId() == StpUtil.getLoginId()) {
            int flag = articleDao.updateById(articles);
            Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
            String msg = flag != 0 ? "修改成功" : "数据修改失败，请重试！";
            List<Article> getArticles = articleDao.selectList(new QueryWrapper<Article>().lambda().eq(Article::getUserId, StpUtil.getLoginId()));
            return new Result(getArticles, code, msg);
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
    public Result getArticleByUserId() {
        List<Article> articles = articleDao.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getUserId, StpUtil.getLoginId())
                .orderBy(true, false, Article::getCreateTime));
        Integer code = articles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = articles != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(articles, code, msg);
    }

}
