package com.susu.dao;

import com.susu.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Date:2023/6/12 11:32
 * @Created by Muqing
 */
@Mapper
public interface MenuDao {

    @Select("select * from tb_menu")
    List<Menu> getAll();
}
