package com.susu.service;

import com.susu.damian.Article;
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
