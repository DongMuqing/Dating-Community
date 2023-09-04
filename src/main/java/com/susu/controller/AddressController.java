package com.susu.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.susu.entity.Code;
import com.susu.entity.Post;
import com.susu.entity.Result;
import com.susu.entity.VisitorInfo;
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


    @PostMapping("/visitorInfo")
    @SaIgnore
    //访问者信息
    public Result visitorInfo(HttpServletRequest request) throws Exception {
        int flag;
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("user-agent"));

        String clientType = userAgent.getOperatingSystem().getDeviceType().toString();
        String browser = userAgent.getBrowser().toString();
        String os = userAgent.getOperatingSystem().getName();
        String ip = IPUtil.getIpAddr(request);
        String ipInfo = IpInfo.getInfo(request, resourceLoader);
        LocalDateTime accessTime = TimeUtil.getLocalDateTime();

        VisitorInfo visitorInfo = new VisitorInfo(accessTime, ip, ipInfo, clientType, os, browser);
        flag = visitorInfoDao.insert(visitorInfo);
        Integer code = flag == 1 ? Code.GET_OK : Code.GET_ERR;
        String msg = flag == 1 ? "插入成功" : "插入失败！";
        return new Result(null, code, msg);
    }

    /**
     * 获取访客信息
     * @return
     */
    @PostMapping("/getVisitorInfo")
    public Result visitorInfo(@RequestParam(defaultValue = "1") long current,
                              @RequestParam(defaultValue = "20") long size) {
        LambdaQueryWrapper<VisitorInfo> visitorInfoLambdaQueryWrapper = Wrappers.lambdaQuery();
        visitorInfoLambdaQueryWrapper.orderBy(true, false, VisitorInfo::getAccessTime);
        Page<VisitorInfo> visitorInfoPage = new Page<>(current , size);
        IPage<VisitorInfo> postIPage = visitorInfoDao.selectPage(visitorInfoPage , visitorInfoLambdaQueryWrapper);
        HashMap<String, Object> visitorInfoMap=new HashMap<>();
        if (postIPage!=null){
            visitorInfoMap.put("data",postIPage.getRecords());
            visitorInfoMap.put("pages",postIPage.getPages());
            visitorInfoMap.put("total",postIPage.getTotal());
            return new Result(visitorInfoMap,Code.GET_OK,"查询成功");
        }else{
            return new Result(null,Code.GET_ERR,"查询失败");
        }
    }

}
