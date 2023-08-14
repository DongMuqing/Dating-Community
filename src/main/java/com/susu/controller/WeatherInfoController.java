package com.susu.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSON;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.service.impl.AmapServiceImpl;
import com.susu.util.IpInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Date:2023/7/15 20:58
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/weather")
@CrossOrigin
@Slf4j
@SaCheckLogin
public class WeatherInfoController {
    @Autowired
    private AmapServiceImpl amapService;
    @Autowired
    private ResourceLoader resourceLoader;

    @PostMapping
    @SaIgnore
    public Result getWeather(HttpServletRequest request) throws Exception {
        String citys = IpInfo.getAddress(request, resourceLoader);
        //传入获取天气的方法中进行获取天气信息
        String data = amapService.getWeather(citys);
        HashMap<String, String> weatherMap = JSON.parseObject(data, HashMap.class);
        Integer code = weatherMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = weatherMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(weatherMap, code, msg);
    }

}
