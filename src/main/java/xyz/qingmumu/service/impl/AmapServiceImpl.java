package xyz.qingmumu.service.impl;

import xyz.qingmumu.service.AmapService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;


/**
 * @Date:2023/7/15 20:43
 * @Created by Muqing
 */
@Service
public class AmapServiceImpl implements AmapService {

    @Resource
    private RestTemplate restTemplate;
    @Value("${amap.key}")
    private String key;
    //显示所有的天气
    private final String extensions = "all";
    //返回实况天气
    private final String extensionss="base";

    @Override
    public String getAddress(String ip) {
        String url = "https://restapi.amap.com/v3/ip?key=" + key + "&ip=" + ip;
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);
        return response;
    }

    @Override
    public String getWeather(String city) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + city + "&key=" + key + "&extensions=" + extensions;
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);
        return response;
    }

    @Override
    public String actualWeather(String city) {
        String url = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + city + "&key=" + key + "&extensions=" + extensionss;
        URI uri = URI.create(url);
        String response = restTemplate.getForObject(uri, String.class);
        return response;
    }

}
