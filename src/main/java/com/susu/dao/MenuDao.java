package com.susu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.susu.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Date:2023/6/12 11:32
 * @Created by Muqing
 */
@Mapper
public interface MenuDao extends BaseMapper<Menu> {
}
