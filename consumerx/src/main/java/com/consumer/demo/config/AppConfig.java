package com.consumer.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {

    @Bean("routingKeyClassMapping")
    public Map<String,String> routingKey(){
        final Map<String, String> routingKeyMap = new HashMap<>();
        routingKeyMap.put("test.queue.hw1.rk", "java.lang.Object");
        return routingKeyMap;
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
