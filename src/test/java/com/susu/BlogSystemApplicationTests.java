package com.susu;


import com.susu.service.impl.AmapServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
class BlogSystemApplicationTests {

    @Autowired
    private AmapServiceImpl amapService;


    @Test
    public void getWeather( ) {
    }
}
