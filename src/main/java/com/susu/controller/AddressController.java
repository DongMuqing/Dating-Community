package com.susu.controller;


import com.alibaba.fastjson.JSON;
import com.susu.damian.Code;
import com.susu.damian.Result;
import com.susu.service.impl.AmapServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result getAddress() {
        String ip = amapService.getIp();
        HashMap<String, String> ipMap = JSON.parseObject(ip, HashMap.class);
        Integer code = ipMap != null ? Code.GET_OK : Code.GET_ERR;
        String msg = ipMap != null ? "查询成功" : "数据查询失败，请重试！";
        return new Result(ipMap, code, msg);
    }
}
