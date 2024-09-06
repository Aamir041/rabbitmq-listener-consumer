package com.publisher.demo.service;

import com.publisher.demo.dto.User;
import com.publisher.demo.dto.UserEventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SomeService {

    @Autowired
    private PublisherService publisherService;

    String eventName = "SOME-EVENT";
    String routingKey = "test.queue.hw1.rk";
    String messageVersion = "1.0";

    public String sendToRabbitMQ(String userName){
        UserEventDto userEventDto = new UserEventDto();

        User user = new User();
        user.setUserGuid(UUID.randomUUID());
        user.setName(userName);

        userEventDto.setUser(user);

        publisherService.publishMessage(userEventDto, eventName, routingKey, messageVersion);
        return userName;
    }

}
