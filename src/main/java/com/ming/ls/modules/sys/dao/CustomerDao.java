package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.customer.CustomerNameAndPhone;
import com.ming.ls.modules.sys.vo.customer.CustomerVo;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Mapper
public interface CustomerDao extends BaseMapper<Customer> {
    IPage<CustomerVo> birthVipList(IPage<CustomerVo> page,@Param("day") LocalDate day,
                                  @Param("daySubOne") LocalDate daySubOne,@Param("daySubTwo") LocalDate daySubTwo);

    String listBirthVipPhones(@Param("day") LocalDate day,
                                   @Param("daySubOne") LocalDate daySubOne,@Param("daySubTwo") LocalDate daySubTwo);

    /**
     * 返回名字和手机号
     * @param query
     * @return
     */
    List<CustomerNameAndPhone> nameAndPhone(@Param("query") BaseQuery query);

    OrdersIncomeByTime notUsedMoneySum(@Param("start") LocalDate start, @Param("end") LocalDate end);

    OrdersIncomeByTime vipMoneySum(@Param("start") LocalDate start, @Param("end") LocalDate end);


}
