package com.publisher.demo.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class User {

    private UUID userGuid;
    private String name;
}
