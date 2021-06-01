package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.entity.OrdersLog;
import com.ming.ls.modules.sys.query.orders.LogQuery;
import com.ming.ls.modules.sys.service.IOrdersLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-05-14
 */
@RestController
@RequestMapping("/ordersLog")
public class OrdersLogController {
    @Autowired
    private IOrdersLogService ordersLogService;

    @GetMapping
    public ServerResponse findPage(LogQuery query){
        IPage<OrdersLog> page = ordersLogService.findPage(query);
        return ServerResponse.createBySuccess(page);
    }

    @PostMapping
    public ServerResponse save(@RequestBody OrdersLog ordersLog) {
        ordersLogService.saveLog(ordersLog);
        return ServerResponse.createBySuccess();
    }
}
