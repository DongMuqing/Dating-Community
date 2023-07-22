package com.susu;


import com.alibaba.fastjson.JSON;
import com.susu.damian.Result;
import com.susu.service.impl.AmapServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;



@Slf4j
@SpringBootTest
class BlogSystemApplicationTests {

    @Autowired
    private AmapServiceImpl amapService;


    @Test
    public void getWeather( ) {
    }
}
