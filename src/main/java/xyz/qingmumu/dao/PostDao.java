package xyz.qingmumu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.qingmumu.entity.Post;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date:2023/6/13 17:33
 * @Created by Muqing
 */
@Mapper
public interface PostDao extends BaseMapper<Post> {

}
