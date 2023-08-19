package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.susu.damian.AfterMenu;
import com.susu.damian.Code;
import com.susu.damian.Menu;
import com.susu.damian.Result;
import com.susu.dao.AfterMenuDao;
import com.susu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Date:2023/6/12 11:36
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/menu")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private AfterMenuDao afterMenuDao;

    @GetMapping
    @SaIgnore
    public Result getMenu() {
        List<Menu> menus = menuService.getAll();
        Integer code = menus != null ? Code.GET_OK : Code.GET_ERR;
        String msg = menus != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(menus, code, msg);
    }

    @GetMapping("/after")
    public Result getMenus() {
        List<AfterMenu> menus = afterMenuDao.selectList(null);
        Integer code = menus != null ? Code.GET_OK : Code.GET_ERR;
        String msg = menus != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(menus, code, msg);
    }
}
