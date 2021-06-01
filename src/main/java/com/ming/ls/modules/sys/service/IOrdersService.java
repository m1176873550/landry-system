package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.entity.Orders;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.orders.OrdersGroupByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IOrdersService extends IService<Orders> {
    IPage listPack(BaseQuery query);

    /**
     * 生成订单ID
     * @return
     */
    Integer generateId();

    /**
     * 列表
     * @param query
     * @return
     */
    IPage<OrdersVo> list(BaseQuery query);



    OrdersGroupByTime getDateSummary(Integer days);

    List<OrdersIncomeByTime> getDataIncome(Integer end);

}
