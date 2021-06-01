package com.ming.ls.modules.sys.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.mq.MsgPo;
import com.ming.ls.modules.common.mq.RabbitSenderMsg;
import com.ming.ls.modules.sys.dao.CustomerDao;
import com.ming.ls.modules.sys.dao.OrdersDao;
import com.ming.ls.modules.sys.entity.Customer;
import com.ming.ls.modules.sys.entity.Orders;
import com.ming.ls.modules.sys.service.ICustomerService;
import com.ming.ls.modules.sys.service.IOrdersService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.ming.ls.modules.common.sysConst.Const.PICK_MSG_TEXT;
import static com.ming.ls.modules.common.sysConst.Const.VIP_MSG_TEXT;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class OrderSchedule {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private OrdersDao ordersDao;

    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private RabbitSenderMsg rabbitSenderMsg;

    @Scheduled(cron = "*/20 * * * * ?")
    @Transactional(rollbackFor = LsServiceException.class)
    public void checkWarningVipBirth() {
        LocalDate now = LocalDate.now();
        String phones = customerDao.listBirthVipPhones(now, now.minusDays(1), now.minusDays(2));
        if (StringUtils.isNotEmpty(phones)) {
            try {
                //发送短信
                MsgPo msgPo=new MsgPo().setPhones(phones).setText(VIP_MSG_TEXT);
                //消息队列发送短信
                rabbitSenderMsg.sendMsgContent(msgPo);
                //数组转列表
                List<String> updatePhones = Arrays.asList(phones.split(","));
                //待更新wrapper
                LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<Customer>()
                        .in(Customer::getPhone, updatePhones)
                        .eq(Customer::getRemind, false);
                //待更新的列表
                List<Customer> list = customerService.list(wrapper);
                //赋值并更新
                list.forEach(e -> {
                    e.setRemind(true);
                    customerService.updateById(e);
                });
            } catch (Exception e) {
                throw new LsServiceException("短信发送异常");
            }
        }

    }


    @Scheduled(cron = "*/20 * * * * ?")
    @Transactional(rollbackFor = LsServiceException.class)
    public void checkWarningOrder() {
        String phones = ordersDao.listPackPhones(LocalDate.now());
        if (StringUtils.isNotEmpty(phones)) {
            try {
                //发送短信
                MsgPo msgPo=new MsgPo().setPhones(phones).setText(PICK_MSG_TEXT);
                //消息队列发送短信
                rabbitSenderMsg.sendMsgContent(msgPo);
                List<String> updatePhones = Arrays.asList(phones.split(","));
                List<Orders> orderIds = ordersDao.getNotRemindOrdersByPhones(updatePhones);
                orderIds.forEach(e -> {
                    e.setRemind(true);
                    ordersService.updateById(e);
                });
            } catch (Exception e) {
                throw new LsServiceException("短信发送异常");
            }
        }
    }

}
