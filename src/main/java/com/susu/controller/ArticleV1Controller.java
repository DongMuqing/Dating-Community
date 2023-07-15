package com.susu.controller;

import com.susu.damian.Article;
import com.susu.damian.Code;
import com.susu.damian.Result;
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
public class ArticleV1Controller {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public Result getAll(){
        List<Article> articles = articleService.getAll();
        Integer code = articles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = articles != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(articles,code,msg);
    }

//    @GetMapping("/{id}")
//    public Result getById(@PathVariable Integer id){
//        User users = userService.getById(id);
//        Integer code = users != null ? Code.GET_OK : Code.GET_ERR;
//        String msg = users != null ? "查询成功" : "数据查询失败，请重试！";
//        return new Result(code,users,msg);
//    }
}
