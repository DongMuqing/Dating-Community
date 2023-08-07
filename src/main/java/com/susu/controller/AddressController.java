package com.susu.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.damian.VisitorInfo;
import com.susu.dao.VisitorInfoDao;
import com.susu.service.impl.AmapServiceImpl;
import com.susu.util.IPUtil;
import com.susu.util.IpInfo;
import com.susu.util.TimeUtil;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
@SaCheckLogin
public class AddressController {
    @Autowired
    private AmapServiceImpl amapService;
    @Autowired
    private VisitorInfoDao visitorInfoDao;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping
    @SaIgnore
    //获取访问者地址信息
    public Result getAddress(HttpServletRequest request) {
        String ip = IPUtil.getIpAddr(request);
        String address = amapService.getAddress(ip);
        HashMap<String, String> addressMap = JSON.parseObject(address, HashMap.class);
        Integer code = addressMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = addressMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(addressMap, code, msg);
    }


    @PostMapping("/visitorInfo")
    @SaIgnore
    //访问者信息
    public Result visitorInfo(HttpServletRequest request) throws Exception {
        int flag ;
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));
        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        String browser = userAgent.getBrowser().toString();
        String os = userAgent.getOperatingSystem().getName();
        String ip = IPUtil.getIpAddr(request);
        String ipInfo =  IpInfo.getInfo(request, resourceLoader);
        LocalDateTime accessTime = TimeUtil.getLocalDateTime();
        VisitorInfo visitorInfo = new VisitorInfo(accessTime, ip, ipInfo,clientType, os, browser);
        flag = visitorInfoDao.insert(visitorInfo);
        Integer code = flag == 1 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag == 1 ? "插入成功" : "插入失败！";
        return new Result(null, code, msg);
    }

    @PostMapping("/getVisitorInfo")
    public Result visitorInfo() {
        List<VisitorInfo> visitorInfos = visitorInfoDao.selectList(new QueryWrapper<VisitorInfo>().lambda().
                orderBy(true,false,VisitorInfo::getAccessTime));
        Integer code = visitorInfos != null? Code.GET_OK : Code.GET_ERR;
        String msg = visitorInfos != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(visitorInfos,code,msg);
    }

}
