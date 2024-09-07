package com.consumer.demo.config;

import com.consumer.demo.converter.RoutingKeyJavaTypeMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;


@Configuration
public class MessageConfig {
    private static final int RETRY_COUNT = 3;

    private static final int WAIT_ONE_SECOND = 1000;

    @Value("${rabbit.addresses}")
    private String address;

    @Value("${rabbit.user}")
    private String username;

    @Value("${rabbit.password}")
    private String password;

    @Value("${rabbit.exchange.name}")
    private String exchangeName;

    private String deadExchangeName = "dead.exchange";

    @Resource(name = "routingKeyClassMapping")
    private Map<String, String> routingKeyClassMapping;

    @Bean(name = "listeningConnectionFactory")
    @Primary
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
        cachingConnectionFactory.setAddresses(address);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

//    @Bean(name = "publishingConnectionFactory")
//    public ConnectionFactory getConnectionFactoryForSend(){
//        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
//        cachingConnectionFactory.setAddresses(address);
//        cachingConnectionFactory.setUsername(username);
//        cachingConnectionFactory.setPassword(password);
//        cachingConnectionFactory.setPublisherConfirms(true);
//        return cachingConnectionFactory;
//    }

    @Bean
    public RabbitAdmin rabbitAdmin(){
        return new RabbitAdmin(connectionFactory());
    }


    @Bean
    public TopicExchange topicExchange(){
        final TopicExchange topicExchange =new TopicExchange(exchangeName,true,false);
        topicExchange.setShouldDeclare(false);
        return topicExchange;
    }

//    @Bean
//    public TopicExchange deadLetterExchange(){
//        return new TopicExchange(deadExchangeName,true,false);
//    }

    @Bean
    public RabbitTransactionManager rabbitTransactionManager(){
        return new RabbitTransactionManager(connectionFactory());
    }

    public MessageConverter jsonMessageConverter(){
        final Jackson2JsonMessageConverter converter = new  Jackson2JsonMessageConverter(objectMapper());
        converter.setJavaTypeMapper(jackson2JavaTypeMapper());
        return  converter;
    }

    public Jackson2JavaTypeMapper jackson2JavaTypeMapper(){
        final RoutingKeyJavaTypeMapper javaTypeMapper = new RoutingKeyJavaTypeMapper();
        javaTypeMapper.setRoutingKeyMapping(routingKeyClassMapping);
        return javaTypeMapper;
    }

    public ObjectMapper objectMapper(){
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE,false);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE,false);
        return mapper;
    }

    @Bean
    public MethodInterceptor statefulRetryOperationsInterceptor(){
        return RetryInterceptorBuilder.stateful().backOffOptions(WAIT_ONE_SECOND,1,WAIT_ONE_SECOND)
                .maxAttempts(RETRY_COUNT).recoverer(new RejectAndDontRequeueRecoverer())
                .messageKeyGenerator(message -> {
                    if(StringUtils.isEmpty(message.getMessageProperties().getMessageId())){
                        return "invalid";
                    }
                    return message.getMessageProperties().getMessageId();
                }).build();
    }

}
