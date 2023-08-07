package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.susu.damian.Article;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    @SaIgnore
    public Result getAll(){
        List<Article> articles = articleService.getAll();
        Integer code = articles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = articles != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(articles,code,msg);
    }
}
