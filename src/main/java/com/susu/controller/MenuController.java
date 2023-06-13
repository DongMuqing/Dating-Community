package com.susu.controller;

import com.susu.config.ApiVersion;
import com.susu.damian.Code;
import com.susu.damian.Menu;
import com.susu.damian.Result;
import com.susu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
@ApiVersion(1)
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping
    public Result getAll(){
        List<Menu> menus = menuService.getAll();
        log.info("执行了");
        log.info(menus.toString());
        Integer code = menus != null ? Code.GET_OK : Code.GET_ERR;
        String msg = menus != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(menus,code,msg);
    }
}
