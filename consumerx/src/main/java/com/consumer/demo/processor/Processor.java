package com.consumer.demo.processor;

import com.consumer.demo.dto.UserWrapperEvent;
import com.consumer.demo.mapper.UserWrapperEventMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.publisher.demo.dto.UserEventDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;

public class Processor {

    private ObjectMapper objectMapper;

    public Processor(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public void processEvent(LinkedHashMap<String,Object> messageObj){
        UserWrapperEvent userWrapperEvent = objectMapper.convertValue(messageObj, UserWrapperEvent.class);
        processEvent(userWrapperEvent);
    }


    private void processEvent(UserWrapperEvent user){
        System.out.println(user);
    }
}
