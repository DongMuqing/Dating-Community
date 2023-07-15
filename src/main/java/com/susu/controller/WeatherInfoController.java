package com.susu.controller;

import com.alibaba.fastjson.JSON;
import com.susu.damian.Code;
import com.susu.damian.Menu;
import com.susu.damian.Result;
import com.susu.service.impl.AmapServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @GetMapping
    public Result getWeather() {
        String province = "";
        Integer code = 0;
        String msg = "";
        HashMap<String, String> weatherMap = new HashMap<>();
        String ip = amapService.getIp();
        HashMap<String, String> ipMap = JSON.parseObject(ip, HashMap.class);
        Set<Map.Entry<String, String>> en = ipMap.entrySet();
        for (Map.Entry<String, String> entry : en) {
            if (entry.getKey() == "province") {
                province = entry.getValue();
                weatherMap = JSON.parseObject(amapService.getWeather(province), HashMap.class);
                code = Code.GET_OK;
                msg = "查询成功";
            } else if (entry.getKey() == "status" && entry.getValue() == "0") {
                //查询失败
                weatherMap = null;
                code = Code.GET_ERR;
                msg = "数据查询失败，请重试！";
            }
        }
        return new Result(weatherMap, code, msg);
    }
}
