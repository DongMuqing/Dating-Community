package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.susu.damian.Backdrop;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.dao.BackdropDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @Date:2023/6/13 19:42
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/backdrop")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class BackdropController {
    @Autowired
    private BackdropDao  backdropDao;

    @GetMapping
    @SaIgnore
    public Result getAll(){
        List<Backdrop> backdrops =backdropDao.selectList(null);
        Integer code = backdrops != null ? Code.GET_OK : Code.GET_ERR;
        String msg = backdrops != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(backdrops,code,msg);
    }
}
