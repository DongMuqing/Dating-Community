package xyz.qingmumu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import xyz.qingmumu.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date:2023/6/9 17:37
 * @Created by Muqing
 */
@Mapper
public interface UserDao extends BaseMapper<User> {
}