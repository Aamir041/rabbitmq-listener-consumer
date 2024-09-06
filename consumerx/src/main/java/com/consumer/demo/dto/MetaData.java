package com.consumer.demo.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonRootName(value = "messageMetaData")
public class MetaData {
    private String eventName;
    private String eventOriginator;
    private String messageId;
    private String routingKey;
    private String type;
}
