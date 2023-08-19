package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.susu.damian.Article;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.dao.ArticleDao;
import com.susu.service.ArticleService;
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
    private ArticleService articleService;
    @Autowired
    private ArticleDao articleDao;

    @GetMapping
    @SaIgnore
    public Result getAll() {
        List<Article> articles = articleDao.selectList(null);
        Integer code = articles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = articles != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(articles, code, msg);
    }

    @PostMapping("/add")
    public Result addArticle(@RequestBody Article articles) {
        int flag = articleDao.insert(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "添加成功" : "数据添加失败，请重试！";
        return new Result(null, code, msg);
    }


    @DeleteMapping("/del")
    public Result delArticle(@RequestBody Article articles) {
        int flag = articleDao.deleteById(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "删除成功" : "数据删除失败，请重试！";
        List<Article> getArticles = articleDao.selectList(null);
        return new Result(getArticles, code, msg);
    }

    @PostMapping("/edit")
    public Result editArticle(@RequestBody Article articles) {
        int flag = articleDao.updateById(articles);
        Integer code = flag != 0 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag != 0 ? "修改成功" : "数据修改失败，请重试！";
        List<Article> getArticles = articleDao.selectList(null);
        return new Result(getArticles, code, msg);
    }
}
