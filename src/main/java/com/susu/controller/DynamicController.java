package com.susu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.config.ApiVersion;
import com.susu.damian.Code;
import com.susu.damian.Dynamic;
import com.susu.damian.Result;
import com.susu.dao.DynamicDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date:2023/6/13 17:51
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/dynamic")
@CrossOrigin
@Slf4j
public class DynamicController {
    @Autowired
    private DynamicDao dynamicDao;

    @GetMapping
    public Result getAll() {
        //时间降序查询
        List<Dynamic> dynamics = dynamicDao.selectList(new QueryWrapper<Dynamic>()
                .lambda().orderBy(true, false, Dynamic::getCreateTime));
        Integer code = dynamics != null ? Code.GET_OK : Code.GET_ERR;
        String msg = dynamics != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(dynamics, code, msg);
    }
}
