package com.publisher.demo.config;

import lombok.Data;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Data
@Component
@Configuration
public class MessagingConfig {
    @Value("${rabbit.user}")
    String username;

    @Value("${rabbit.password}")
    String password;

    @Value("${rabbit.virtual.host}")
    String virtualHost;

    @Value("${rabbit.address}")
    String host;

    @Bean
    public ConnectionFactory defaultConnectionFactory() throws IOException {
        CachingConnectionFactory cf = new CachingConnectionFactory();
        cf.setAddresses(host);
        cf.setUsername(username);
        cf.setPassword(password);

        return cf;
    }


}
