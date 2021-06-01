package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.mq.MsgPo;
import com.ming.ls.modules.common.mq.RabbitSenderMsg;
import com.ming.ls.modules.common.util.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.ming.ls.modules.common.sysConst.Const.PICK_MSG_TEXT;
import static com.ming.ls.modules.common.sysConst.Const.VIP_MSG_TEXT;

/**
 * <p>
 *  短信控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/sms")
public class SmsController {
    @Autowired
    private RabbitSenderMsg rabbitSenderMsg;

    @PostMapping("/sendVip")
    public ServerResponse sendVip(@RequestParam String phones){
        MsgPo msgPo = new MsgPo(phones,VIP_MSG_TEXT);
        rabbitSenderMsg.sendMsgContent(msgPo);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("/sendPick")
    public ServerResponse sendPick(@RequestParam String phones){
        MsgPo msgPo = new MsgPo(phones,PICK_MSG_TEXT);
        rabbitSenderMsg.sendMsgContent(msgPo);
        return ServerResponse.createBySuccess();
    }

}
