package com.susu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.susu.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import java.util.List;

/**
 * @Date:2023/6/9 17:38
 * @Created by Muqing
 */
@Mapper
public interface ArticleDao extends BaseMapper<Article> {

    @Select("select * from tb_article ")
    @Results({
            @Result(column="id", property="id", jdbcType= JdbcType.INTEGER, id=true),
            @Result(column="create_time", property="createTime", jdbcType=JdbcType.VARCHAR),
            @Result(column="content", property="content", jdbcType=JdbcType.INTEGER)
    })
    List<Article> getAll();
}
