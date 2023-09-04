package com.susu.service.impl;

import com.susu.entity.Menu;
import com.susu.dao.MenuDao;
import com.susu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date:2023/6/12 11:35
 * @Created by Muqing
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;
    @Override
    public List<Menu> getAll() {
        return menuDao.getAll();
    }
}
