package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.query.customer.CustomerQuery;
import com.ming.ls.modules.sys.vo.customer.CustomerNameAndPhone;
import com.ming.ls.modules.sys.vo.customer.CustomerVo;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.value.Value;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface ICustomerService extends IService<Customer> {

    IPage<CustomerVo> checkVipBirth(BaseQuery query);
    IPage list(CustomerQuery query);

    /**
     * 检查ID
     * @param id
     * @return
     */
    boolean checkId(String id);

    /**
     * 检查会员
     * @param nic
     * @return phone
     */
    String checkNic(String nic);
    /**
     * 返回用户姓名和电话的列表
     * @return
     */
    List<CustomerNameAndPhone> nameAndPhone();

    /**
     * 号码列表
     * @return
     */
    List<Value> sendPhones(String vip);

    /**
     * 检查手机号是否会员
     * @param phone
     * @return
     */
    Customer checkPhone(String phone);
    /**
     * 检查手机号是否会员
     * @param phone
     * @return
     */
    Customer checkNormalPhone(String phone);

    /**
     * 检查VIP
     * @param phone
     * @return
     */
    CustomerVo checkVipByPhone(String phone);
    /**
     * 新增
     * @param customerVo
     * @return
     */
    boolean add(CustomerVo customerVo);

    /**
     * 编辑
     * @param customerVo
     * @return
     */
    boolean edit(CustomerVo customerVo);

    /**
     * 根据ID返回顾客对象
     * @param id
     * @return
     */
    Customer getCustomerById(String id);

    /**
     * 随机生成ID
     * @return
     */
    String generateNic();

    /**
     * 充值接口
     * @param customerVo
     */
    void rechargeAmount(CustomerVo customerVo);


    OrdersIncomeByTime notUsedMoneySum(Integer months);

    OrdersIncomeByTime vipMoneySum(Integer months);

}
