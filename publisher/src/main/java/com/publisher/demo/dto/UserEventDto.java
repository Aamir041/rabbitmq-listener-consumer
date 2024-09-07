package com.publisher.demo.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"user", "messageMetadata"})
public class UserEventDto extends MyMessage{
        private User user;
}
