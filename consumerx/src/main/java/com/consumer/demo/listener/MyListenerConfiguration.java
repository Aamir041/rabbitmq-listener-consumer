package com.consumer.demo.listener;


import com.consumer.demo.config.MessageConfig;
import com.consumer.demo.processor.Processor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyListenerConfiguration extends MessageConfig {

    @Value("${rabbit.queue.name}")
    private String queueName;

    @Value("${rabbit.queue.dead.letter}")
    private String deadLetter;

    @Value("${rabbit.queue.routing.key}")
    private String routingKey;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public Binding queueBinding(){
        return BindingBuilder.bind(queue()).to(topicExchange()).with(routingKey);
    }

    @Bean
    public Queue queue(){
        final Map<String, Object> arguments = new HashMap<>();
        arguments.put("dead-letter-exchnage",deadLetter);
        final Queue queue = new Queue(queueName,true,false,false,arguments);
        queue.setAdminsThatShouldDeclare(rabbitAdmin());
        queue.setShouldDeclare(true);
        return queue;
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setTransactionManager(rabbitTransactionManager());
        container.setChannelTransacted(true);
        container.setQueues(queue());
        container.setAmqpAdmin(rabbitAdmin());
        container.setMessageListener(messageListener());
        container.setAdviceChain(new Advice[]{statefulRetryOperationsInterceptor()});
        container.setConcurrentConsumers(1);
        return container;
    }

    @Bean
    public MessageListener messageListener(){
        MessageListenerAdapter delegate = new MessageListenerAdapter(processor(), jsonMessageConverter());
        delegate.setDefaultListenerMethod("processEvent");
        return delegate;
    }

    @Bean
    public Processor processor(){
        return new Processor(objectMapper);
    }

}
