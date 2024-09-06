package com.publisher.demo.controller;

import com.publisher.demo.service.SomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController {

    @Autowired
    private SomeService someService;

    @RequestMapping(value = "/addUser/{userName}", method = RequestMethod.POST)
    public String addUser(@PathVariable("userName") String userName){
        String userNameRes = someService.sendToRabbitMQ(userName);
        return userNameRes;
    }

}
