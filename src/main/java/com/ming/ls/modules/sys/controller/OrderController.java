package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.annotation.Authorized;
import com.ming.ls.modules.common.sysConst.PermissionConst;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IOrdersService;
import com.ming.ls.modules.sys.vo.orders.OrdersGroupByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private IOrdersService iOrdersService;


    @PostMapping("/listPack")
    public ServerResponse listPack(@RequestBody BaseQuery query){
        IPage page = iOrdersService.listPack(query);
        return ServerResponse.createBySuccess(page);
    }

    /**
     * 列表
     * @param query
     * @return
     */
    @PostMapping()
//    @Authorized(codes = {PermissionConst.ORDERS_LIST})
    public ServerResponse list(@RequestBody BaseQuery query){
        IPage<OrdersVo> page = iOrdersService.list(query);
        return ServerResponse.createBySuccess(page);
    }


    /**
     * 今日数据统计
     * @return
     */
    @GetMapping("/get-date-summary")
    public ServerResponse getDateSummary(@RequestParam("days") Integer days){
        OrdersGroupByTime dateSummary = iOrdersService.getDateSummary(days);
        return ServerResponse.createBySuccess(dateSummary);
    }

    /**
     * 今月数据统计
     * @return
     */
    @GetMapping("/getDataIncome")
    public ServerResponse getDataIncome(@RequestParam("months") Integer months){
        List<OrdersIncomeByTime> dataIncome = iOrdersService.getDataIncome(months);
        return ServerResponse.createBySuccess(dataIncome);
    }
}
