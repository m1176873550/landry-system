package com.ming.ls.modules.common.mq;

import com.ming.ls.config.RabbitMsgConfig;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.sys.service.IDictionaryService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.ming.ls.modules.common.sysConst.Const.ON_MSG;
import static com.ming.ls.modules.common.yunpan.MsgClient.batchSendMsg;
import static com.ming.ls.modules.common.yunpan.MsgClient.batchSendPick;

@Component
@Slf4j
@RabbitListener(queues = RabbitMsgConfig.SPRING_BOOT_MSG_QUEUE)
public class RabbitReceiverMsg {

    @Autowired
    private IDictionaryService dictionaryService;

    /**
     * 获取信息:
     * queue也可以支持RabbitMQ中对队列的模糊匹配
     *
     * @param
     */
    @RabbitHandler
    public void receiveMsgContent(MsgPo msgPo, Channel channel, Message message) throws IOException {
        try {
            //若有数据才操作
            if (StringUtils.isNotEmpty(msgPo.getPhones())) {
                //发送短信,==1说明短信启用
                if (new Integer(1).equals(dictionaryService.findValueByName(ON_MSG))){
                    batchSendMsg(msgPo.getText(),msgPo.getPhones());
                }
                //已经接受并消费掉了，不会再重复发送消费
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "短信发送失败，数据库异常");
            }
            //自定义异常，捕获rabbitmq发生的异常
        } catch (Exception e) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "短信发送失败，数据库异常");
        }
    }
}