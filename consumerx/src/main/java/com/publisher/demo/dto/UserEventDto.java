package com.publisher.demo.dto;

import com.consumer.demo.dto.MetaData;
import com.consumer.demo.dto.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserEventDto {
    private User user;
    private MetaData messageMetadata;
}
