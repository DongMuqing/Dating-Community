package com.susu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.susu.damian.Result;
import com.susu.service.impl.AmapServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

/**
 * @Date:2023/7/15 20:58
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/weather")
@CrossOrigin
@Slf4j
public class WeatherInfoController {
    @Autowired
    private AmapServiceImpl amapService;

    @PostMapping
    public Result getWeather(@RequestBody String city) {
        //json写法
        Integer code = 0;
        String msg = "";
        //取出返回json中的city
        HashMap<String, String> cityMap = JSON.parseObject(city, new TypeReference<HashMap<String, String>>() {
        });
        String firstKey = cityMap.keySet().iterator().next();
        String citys = cityMap.get(firstKey);
        //传入获取天气的方法中进行获取天气信息
        String data = amapService.getWeather(citys);
        HashMap<String, String> weatherMap = JSON.parseObject(data, HashMap.class);
        return new Result(weatherMap, code, msg);
    }

}
