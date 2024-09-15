package com.publisher.demo.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.publisher.demo.dto.MyMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PublisherService {

    private static final Logger LOG = LoggerFactory.getLogger(PublisherService.class);

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void publishMessage(MyMessage message, String eventName, String routingKey, String version){
        LOG.info("Start to publish message");
        publish(routingKey, buildAmpqMessage(message, eventName, routingKey, version));
        LOG.info("Completed Message");
    }

    private void publish(String routingKey, Message message){
        try {
            message.getMessageProperties().getHeaders().remove("__TypeId__");
            rabbitTemplate.send(routingKey,message);
        }
        catch (AmqpException amqpException){
            LOG.error("Error while publishing event : ", amqpException);
        }
    }

    private Message buildAmpqMessage(MyMessage message, String eventName, String routingKey, String version){
        MessageProperties messageProperties = createMessageProperties(eventName);
        message.setMessageMetadata(buildMessageHeader(eventName, routingKey, version, messageProperties));
//        LinkedHashMap<String, Object> mymessage =  convertObjectToLinkedHashMap(message);
        return rabbitTemplate.getMessageConverter().toMessage(message, messageProperties);

    }

    private MessageProperties createMessageProperties(String eventName){
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
        String messageId = null;
        if (eventName != null){
            messageId = eventName + "_" + UUID.randomUUID();
            messageProperties.setMessageId(messageId);
            messageProperties.setHeader("messageId",messageId);
        }
        return messageProperties;
    }

    private Map<String,String> buildMessageHeader(String eventName, String routingKey, String version, MessageProperties messageProperties){
        Map<String,String> messageHeader = createMetaData(eventName, messageProperties.getMessageId(), routingKey, version);
        for(Map.Entry<String,String> entry : messageHeader.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            messageProperties.setHeader(key,value);
        }
        return messageHeader;
    }

    private Map<String, String> createMetaData(String eventName, String messageId, String routingKey, String version) {
        Map<String, String> messageMetaData = new HashMap<>();
        messageMetaData.put("eventName", eventName);
        messageMetaData.put("eventOriginator","FROM-AAMIR");
        messageMetaData.put("messageVersionId",version);
        messageMetaData.put("messageId",messageId);
        messageMetaData.put("routingKey",routingKey);
        return messageMetaData;
    }

    private LinkedHashMap<String, Object> convertObjectToLinkedHashMap(MyMessage message){
        ObjectMapper mapper = new ObjectMapper();
        LinkedHashMap<String,Object> mymessage =  mapper.convertValue(message, LinkedHashMap.class);
        System.out.println(mymessage);
        return mymessage;
    }


}
