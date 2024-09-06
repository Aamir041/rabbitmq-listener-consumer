package com.consumer.demo.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class User {
    private UUID userGuid;
    private String name;
}
