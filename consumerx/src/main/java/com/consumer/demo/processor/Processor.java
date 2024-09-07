package com.consumer.demo.processor;

import com.consumer.demo.dto.UserWrapperEvent;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

public class Processor {

    private ObjectMapper objectMapper;

    private Logger LOG = LoggerFactory.getLogger(Processor.class);

    public Processor(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public void processEvent(LinkedHashMap<String,Object> messageObj){
        LOG.info("Processing Message....");
        UserWrapperEvent userWrapperEvent = objectMapper.convertValue(messageObj, UserWrapperEvent.class);
        processEvent(userWrapperEvent);
        LOG.info("Message Processed....");
    }


    private void processEvent(UserWrapperEvent user){
        String userInfo = user.getUser().toString();
        LOG.info("User from publisher :: {}",userInfo);
    }
}
