package com.susu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class BlogSystemApplication {
    //当使用外部url获取数据的时候
    //springboot是无法自动注入RestTemplate 的，但是springcloud就可以自动注入，因此将其独立出来之后不能够简单照旧；需要我们在springboot配置类中注入RestTemplate，
    @Autowired
    //RestTemplateBuilder
    private RestTemplateBuilder builder;
    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogSystemApplication.class, args);
    }
    //解决在线上服务器会相差8小时问题
    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }

}
