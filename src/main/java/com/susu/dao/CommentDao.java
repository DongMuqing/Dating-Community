package com.susu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.susu.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date:2023/8/11 0:10
 * @Created by Muqing
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {
}
