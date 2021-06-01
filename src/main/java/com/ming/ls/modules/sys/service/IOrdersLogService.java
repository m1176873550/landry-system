package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.entity.OrdersLog;
import com.ming.ls.modules.sys.query.orders.LogQuery;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-05-14
 */
public interface IOrdersLogService extends IService<OrdersLog> {
    IPage<OrdersLog> findPage(LogQuery query);

    void saveLog(OrdersLog ordersLog);
}
