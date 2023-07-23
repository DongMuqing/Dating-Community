package com.susu.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.damian.VisitorInfo;
import com.susu.dao.VisitorInfoDao;
import com.susu.service.impl.AmapServiceImpl;
import com.susu.util.IPUtil;
import com.susu.util.TimeUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @Date:2023/7/20 16:33
 * @Created by Muqing
 */
@RestController
@RequestMapping("api/{version}/ip")
@CrossOrigin
@Slf4j
public class AddressController {
    @Autowired
    private AmapServiceImpl amapService;
    @Autowired
    private VisitorInfoDao visitorInfoDao;

    @GetMapping
    //获取访问者地址信息
    public Result getAddress(HttpServletRequest request) {
        String ip = IPUtil.getIpAddr(request);
        String address = amapService.getAddress(ip);
        HashMap<String, String> addressMap = JSON.parseObject(address, HashMap.class);
        Integer code = addressMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = addressMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(addressMap, code, msg);
    }


    @GetMapping("/visitorInfo")
    //访问者信息
    public Result visitorInfo(HttpServletRequest request) {
        int flag = 0;
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        String browser = userAgent.getBrowser().toString();
        String os = userAgent.getOperatingSystem().getName();
        String ip = IPUtil.getIpAddr(request);
        LocalDateTime accessTime = TimeUtil.getLocalDateTime();
        String address = amapService.getAddress(ip);
        HashMap<String, List<String>> addressMap = JSON.parseObject(address, HashMap.class);
        List<String> city = addressMap.get("city");
        if (!city.isEmpty()) {
            VisitorInfo visitorInfo = new VisitorInfo(accessTime, ip, clientType, os, browser, city.get(0));
            flag = visitorInfoDao.insert(visitorInfo);
        }
        Integer code = flag == 1 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag == 1 ? "插入成功" : "数据插入失败，请重试！";
        return new Result(null, code, msg);
    }
}
