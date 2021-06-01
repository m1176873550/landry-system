package com.ming.ls.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitConfig {

    // 队列名称
    public final static String SPRING_BOOT_QUEUE = "hello";

    // 交换机名称
    public final static String SPRING_BOOT_EXCHANGE = "spring-boot-exchange-msg-convert";
    // 绑定的值
    public static final String SPRING_BOOT_BIND_KEY = "spring-boot-bind-key-msg-convert";


    // === 在RabbitMQ上创建queue,exchange,binding 方法一：通过@Bean实现 begin ===
    /**
     * 定义队列：
     * @return
     */
    @Bean
    Queue queue() {
        return new Queue(SPRING_BOOT_QUEUE, true);
    }

    /**
     * 定义交换机
     * @return
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(SPRING_BOOT_EXCHANGE);
    }

    /**
     * 定义绑定
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(SPRING_BOOT_BIND_KEY );
    }
}
