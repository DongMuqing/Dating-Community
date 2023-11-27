package xyz.qingmumu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.qingmumu.entity.Message;

/**
 * @Date:2023/11/25 15:51
 * @Created by Muqing
 */
@Mapper
public interface MessageDao extends BaseMapper<Message> {
}
