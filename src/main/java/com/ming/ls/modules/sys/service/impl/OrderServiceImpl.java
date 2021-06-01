package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.OrdersDao;
import com.ming.ls.modules.sys.entity.Employee;
import com.ming.ls.modules.sys.entity.Orders;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IDictionaryService;
import com.ming.ls.modules.sys.service.IOrdersService;
import com.ming.ls.modules.sys.vo.customer.CustomerVo;
import com.ming.ls.modules.sys.vo.hangingPoint.OrderVo;
import com.ming.ls.modules.sys.vo.orders.OrdersGroupByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.orders.OrdersVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrdersDao, Orders> implements IOrdersService {

    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private OrdersDao ordersDao;

    @Override
    public IPage listPack(BaseQuery query) {
        IPage<OrdersVo> page=new Page<>(query.getCurrentPage(),query.getSize());
        return ordersDao.listPack(page,LocalDate.now().plusDays(1));
    }

    /**
     * 生成订单ID
     *
     * @return
     */
    @Override
    public Integer generateId() {
        //查询到的ID列表
        List<Integer> ordersIds = new LinkedList<>();
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        List<Orders> list = ordersDao.selectList(new LambdaQueryWrapper<Orders>()
                .select(Orders::getId)
                .eq(Orders::getLaundryId, laundryId)
        );
        //赋值到ID列表
        if (list.size() > 0) {
            for (Orders order : list) {
                //取ID数字部分，
                Integer idNumber = order.getId();
                ordersIds.add(idNumber);
            }
            //获取集合中最大值
            Integer max = Collections.max(ordersIds);
            return max + 1;
        } else {
            return 1;
        }

    }

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public IPage<OrdersVo> list(BaseQuery query) {
        Employee currentUser = SessionUtil.getCurrentUser();
        //判断是否ADMIN
        Integer isAdmin = currentUser.getIsAdmin();
        query.setIsAdmin(isAdmin).setLaundryId(currentUser.getLaundryId());
        //分页对象
        IPage<OrdersVo> page = new Page<>(query.getCurrentPage(), query.getSize());
        //查询列表结果，接下来为订单号赋值
        List<OrdersVo> list = ordersDao.list(page, query).getRecords();

        //字典表查询最大0数
        Integer zeroCount = dictionaryService.findValueByName(Const.DICTIONARY_ZERO_COUNT_NAME);

        //对输出列表ID格式化
        for (OrdersVo ordersVo : list) {
            int integer = Integer.parseInt(ordersVo.getId());
            //0的个数
            int countZero = zeroCount - String.valueOf(integer).length();
            StringBuilder id = new StringBuilder("LS");
            //插入0
            for (int count = 0; count < countZero; count++) {
                id.append("0");
            }
            id.append(integer);
            //格式化后的订单赋值
            ordersVo.setOrdersId(id.toString());
        }
        return page.setRecords(list);
    }

    /**
     * 获取当日数据统计
     * @return
     */
    @Override
    public OrdersGroupByTime getDateSummary(Integer days) {
        LocalDate start;
        LocalDate end=LocalDate.now().plusDays(1);
        if (days==0){
            start=LocalDate.ofEpochDay(0);
        }else {
            start=end.minusDays(days);
        }
        return ordersDao.getDateSummary(start,end);
    }

    @Override
    public List<OrdersIncomeByTime> getDataIncome(Integer months) {
        LocalDate startMonth;
        LocalDate endMonth=LocalDate.now();
        if (months==0){
            startMonth=LocalDate.ofEpochDay(0);
        }else {
            startMonth=endMonth.minusMonths(months);
        }
        return ordersDao.getIncomeSummary(startMonth,endMonth);
    }

}
