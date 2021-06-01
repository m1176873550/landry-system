package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.OrdersLogDao;
import com.ming.ls.modules.sys.entity.Customer;
import com.ming.ls.modules.sys.entity.OrdersLog;
import com.ming.ls.modules.sys.query.orders.LogQuery;
import com.ming.ls.modules.sys.service.ICustomerService;
import com.ming.ls.modules.sys.service.IOrdersLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-05-14
 */
@Service
public class OrdersLogServiceImpl extends ServiceImpl<OrdersLogDao, OrdersLog> implements IOrdersLogService {

    @Autowired
    private IOrdersLogService ordersLogService;

    @Autowired
    private ICustomerService customerService;

    @Override
    public IPage<OrdersLog> findPage(LogQuery query) {
        IPage<OrdersLog> page=new Page<>(query.getCurrentPage(),query.getSize());
        //返回待查询的wrapper
        LambdaQueryWrapper<OrdersLog> wrapper = getPageWrapper();

        return ordersLogService.page(page, wrapper);
    }

    @Override
    public void saveLog(OrdersLog ordersLog) {
        ordersLogService.save(ordersLog);
    }

    private LambdaQueryWrapper<OrdersLog> getPageWrapper(){
        LambdaQueryWrapper<OrdersLog> wrapper = new LambdaQueryWrapper<>();
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        return wrapper.eq(OrdersLog::getLaundryId,laundryId).orderByDesc(OrdersLog::getCreatedAt);
    }


}
