package com.ojo.mullyuojo.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


@Configuration
public class QueueConfig {

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Value("${message.exchange}")
    private String exchange;
    @Value("${message.queue.delivery}")
    private String queueDelivery;

    @Value("${message.err.exchange}")
    private String exchangeErr;
    @Value("${message.queue.err.delivery}")
    private String queueErrDelivery;


    //동일하게 쓰는 exchange ( market )
    @Bean
    public TopicExchange exchange() { return new TopicExchange(exchange); }

    //Queue 생성 ( 이름 지정 )
    @Bean
    public Queue queueDelivery() { return new Queue(queueDelivery); }

    //바인딩 ( 큐의 이름과 똑같이 생성 ). bind -> 어떤 큐? . to -> exchange객체 . with -> Routing key
    @Bean
    public Binding bindingProduct() { return BindingBuilder.bind(queueDelivery()).to(exchange()).with(queueDelivery); }

    @Bean
    public TopicExchange exchangeErr() { return new TopicExchange(exchangeErr); }

    @Bean
    public Queue queueErrOrder() { return new Queue(queueErrDelivery); }


    @Bean
    public Binding bindingErrOrder() { return BindingBuilder.bind(queueErrOrder()).to(exchangeErr()).with(queueErrDelivery); }
}