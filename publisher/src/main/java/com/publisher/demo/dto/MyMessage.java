package com.publisher.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public abstract class MyMessage {
    private Map<String, String> messageMetadata;
}
