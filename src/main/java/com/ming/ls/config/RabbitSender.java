package com.ming.ls.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


//@Component
public class RabbitSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    /**
     * 发送消息
     *
     * @param list
     */
    public void sendMsgContent(List<?> list) {
        //发送数据到队列中
        rabbitTemplate.convertAndSend(RabbitConfig.SPRING_BOOT_EXCHANGE, RabbitConfig.SPRING_BOOT_BIND_KEY, list );
    }

}