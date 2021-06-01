package com.ming.ls.config;

import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.sys.entity.HangingPoint;
import com.ming.ls.modules.sys.service.IHangingPointService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@Slf4j
//@RabbitListener(queues = RabbitConfig.SPRING_BOOT_QUEUE)
public class RabbitReceiver {
    @Autowired
    private IHangingPointService hangingPointService;

    /**
     * 获取信息:
     * queue也可以支持RabbitMQ中对队列的模糊匹配
     *
     * @param list
     */
    @RabbitHandler
    public void receiveMsgContent(List<HangingPoint> list, Channel channel, Message message){
        try {
            //若有数据才操作
            if (list != null && list.size() > 0) {
                //批量更新挂点数据
                hangingPointService.saveBatch(list);
                //这段代码表示，这次消息，我已经接受并消费掉了，不会再重复发送消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                //更新失败
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增挂点列表失败，数据库异常");
            }
            //自定义异常，捕获rabbitmq发生的异常
        } catch (Exception e) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增挂点列表失败，数据库异常");
        }
    }
}