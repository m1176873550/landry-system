package com.ming.ls.modules.common.mq;

import com.ming.ls.config.RabbitMsgConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RabbitSenderMsg {

    @Autowired
    private AmqpTemplate rabbitTemplate;
    /**
     * 发送消息
     *
     * @param po
     */
    public void sendMsgContent(MsgPo po) {
        //发送数据到队列中
        rabbitTemplate.convertAndSend(RabbitMsgConfig.SPRING_BOOT_MSG_QUEUE, po);
    }

}