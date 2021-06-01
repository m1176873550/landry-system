package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Orders;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.orders.OrdersGroupByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Mapper

public interface OrdersDao extends BaseMapper<Orders> {
    IPage<OrdersVo> list(IPage<OrdersVo> page, @Param("query") BaseQuery query);

    String listPackPhones(@Param("day") LocalDate day);

    List<Orders> getNotRemindOrdersByPhones(@Param("phones") List<String> phones);

    IPage<OrdersVo> listPack(IPage<OrdersVo> page,@Param("day") LocalDate day);

    OrdersGroupByTime getDateSummary(@Param("start") LocalDate start,@Param("end") LocalDate end);

    List<OrdersIncomeByTime> getIncomeSummary(@Param("start") LocalDate start, @Param("end") LocalDate end);


}
