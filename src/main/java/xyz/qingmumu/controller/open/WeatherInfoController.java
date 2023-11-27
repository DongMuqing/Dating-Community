package xyz.qingmumu.controller.open;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qingmumu.entity.Code;
import xyz.qingmumu.entity.Result;
import xyz.qingmumu.service.impl.AmapServiceImpl;
import xyz.qingmumu.util.IpInfo;

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

    /**
     * 返回预报天气
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping
    @SaIgnore
    public Result getWeather(HttpServletRequest request) throws Exception {
        String city = IpInfo.getAddress(request, resourceLoader);
        //传入获取天气的方法中进行获取天气信息
        String data = amapService.getWeather(city);
        HashMap<String, String> weatherMap = JSON.parseObject(data, HashMap.class);
        weatherMap.put("city", city);
        Integer code = weatherMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = weatherMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(weatherMap, code, msg);
    }

    /**
     * 返回实况天气
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/base")
    @SaIgnore
    public Result getActualWeather(HttpServletRequest request) throws Exception {
        String city = IpInfo.getAddress(request, resourceLoader);
        //传入获取天气的方法中进行获取天气信息
        String data = amapService.actualWeather(city);
        HashMap<String, String> weatherMap = JSON.parseObject(data, HashMap.class);
        weatherMap.put("city", city);
        Integer code = weatherMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = weatherMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(weatherMap, code, msg);
    }

}
