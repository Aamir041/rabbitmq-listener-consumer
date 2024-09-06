package com.consumer.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean("routingKeyClassMapping")
    public Map<String,String> routingKey(){
        final Map<String, String> routingKeyMap = new HashMap<>();
        routingKeyMap.put("test.queue.hw1.rk", "com.consumer.demo.dto.UserWrapperEvent");
        return routingKeyMap;
    }

}
