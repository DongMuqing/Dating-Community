package xyz.qingmumu.service;

import xyz.qingmumu.entity.Article;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Date:2023/6/9 17:42
 * @Created by Muqing
 */
@Transactional
@Service
public interface ArticleService {
    List<Article> getAll();
}
