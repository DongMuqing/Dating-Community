package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qingmumu.dao.ArticleDao;
import xyz.qingmumu.entity.Article;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;

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
        List<Article> articles = articleDao.selectList(new QueryWrapper<Article>().lambda()
                .orderBy(true, false, Article::getCreateTime));
        Integer code = articles != null ? Code.GET_OK : Code.GET_ERR;
        String msg = articles != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(articles, code, msg);
    }
}
