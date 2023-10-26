package xyz.qingmumu.service.impl;

import xyz.qingmumu.entity.Article;
import xyz.qingmumu.dao.ArticleDao;
import xyz.qingmumu.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date:2023/6/9 17:44
 * @Created by Muqing
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    //注入dao对象
    @Autowired
    private ArticleDao articleDao;


    @Override
    public List<Article> getAll() {
        return articleDao.getAll();
    }
}
