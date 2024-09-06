package com.consumer.demo.processor;

import com.consumer.demo.dto.UserWrapperEvent;

public class Processor {
    public void processEvent(UserWrapperEvent userWrapperEvent){
        System.out.println(userWrapperEvent);
    }
}
