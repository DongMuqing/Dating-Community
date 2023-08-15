package com.susu.service.impl;

import com.susu.dao.PostDao;
import com.susu.service.PostService;
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
