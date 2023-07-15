package com.susu.service.impl;

import com.alibaba.fastjson.JSON;
import com.susu.service.AmapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;
import java.util.*;

/**
 * @Date:2023/7/15 20:43
 * @Created by Muqing
 */
@Service
public class AmapServiceImpl implements AmapService {

    @Resource
    private RestTemplate restTemplate;
    private final String key="187dacc112e3d2a9f4e4c35f8f7d108f";
    //显示所有的天气
    private final String extensions="all";
    @Override
    public String getIp() {
        String url = "https://restapi.amap.com/v3/ip?key=" + key;
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);
        return response;
    }

    @Override
    public String getWeather(String province) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?city="+province+"&key="+key+"&extensions="+extensions;
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);
        return response;
    }

}
