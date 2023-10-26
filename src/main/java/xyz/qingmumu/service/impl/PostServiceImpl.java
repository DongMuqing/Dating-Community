package xyz.qingmumu.service.impl;

import xyz.qingmumu.dao.PostDao;
import xyz.qingmumu.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Date:2023/6/13 17:50
 * @Created by Muqing
 */
@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostDao postDao;

}
