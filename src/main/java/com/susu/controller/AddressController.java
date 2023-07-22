package com.susu.controller;


import com.alibaba.fastjson.JSON;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.service.impl.AmapServiceImpl;
import com.susu.util.IPUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

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

    @GetMapping
    public Result getAddress(HttpServletRequest request) {
        String ip = IPUtil.getIpAddr(request);
        log.info(ip);
        String address = amapService.getAddress(ip);
        HashMap<String, String> addressMap = JSON.parseObject(address, HashMap.class);
        Integer code = addressMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = addressMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(addressMap, code, msg);
    }

    @GetMapping("/address")
    public Result userController(HttpServletRequest request) {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        HashMap<String,String> infoMap=new HashMap<>();
        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        infoMap.put("clientType",clientType);
        String os = userAgent.getOperatingSystem().getName();
        infoMap.put("os",os);
        String ip = IPUtil.getIpAddr(request);
        infoMap.put("ip",ip);
        String browser = userAgent.getBrowser().toString();
        infoMap.put("browser",browser);
        return new Result(infoMap,20001,"ok");
    }
}
