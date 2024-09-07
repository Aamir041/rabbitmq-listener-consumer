package com.consumer.demo.mapper;

import com.consumer.demo.dto.MetaData;
import com.consumer.demo.dto.User;
import com.consumer.demo.dto.UserWrapperEvent;

import java.util.LinkedHashMap;
import java.util.UUID;

public class UserWrapperEventMapper {
    public static UserWrapperEvent mapLinkedHashMapToDTO(LinkedHashMap<String, Object> linkedHashMap){
        LinkedHashMap<String, Object> userMap = (LinkedHashMap<String, Object>) linkedHashMap.get("user");
        LinkedHashMap<String, Object> messageMetaDataMap = (LinkedHashMap<String, Object>) linkedHashMap.get("messageMetadata");

        User user = mapToUser(userMap);
        MetaData metaData = mapToMetaData(messageMetaDataMap);

        UserWrapperEvent userWrapperEvent = new UserWrapperEvent();
        userWrapperEvent.setUser(user);
        userWrapperEvent.setMessageMetadata(metaData);

        return userWrapperEvent;
    }

    private static MetaData mapToMetaData(LinkedHashMap<String, Object> messageMetaDataMap) {
        MetaData metaData = new MetaData();
        metaData.setEventName((String) messageMetaDataMap.get("eventName"));
        metaData.setEventOriginator((String) messageMetaDataMap.get("eventOriginator"));
        metaData.setMessageId((String) messageMetaDataMap.get("messageId"));
        metaData.setMessageVersionId((String) messageMetaDataMap.get("messageVersionId"));
        metaData.setRoutingKey((String) messageMetaDataMap.get("routingKey"));
        metaData.setType((String) messageMetaDataMap.get("type"));

        return metaData;
    }

    private static User mapToUser(LinkedHashMap<String, Object> userMap) {
        User user = new User();
        user.setUserGuid(UUID.fromString((String) userMap.get("userGuid")));
        user.setName((String) userMap.get("name"));
        return user;
    }

}
