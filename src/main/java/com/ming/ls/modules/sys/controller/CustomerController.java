package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.annotation.Authorized;
import com.ming.ls.modules.common.sysConst.PermissionConst;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.entity.Customer;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.query.customer.CustomerQuery;
import com.ming.ls.modules.sys.service.ICustomerService;
import com.ming.ls.modules.sys.vo.customer.CustomerNameAndPhone;
import com.ming.ls.modules.sys.vo.customer.CustomerVo;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.value.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService iCustomerService;


    @PostMapping("/checkVipBirth")
    public ServerResponse checkVipBirth(@RequestBody BaseQuery query) {
        IPage list = iCustomerService.checkVipBirth(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @GetMapping()
    public ServerResponse list(CustomerQuery query) {
        IPage list = iCustomerService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 手机号名字列表，value  名字
     *
     * @return
     */
    @GetMapping("/name-and-phone")
    public ServerResponse nameAndPhone() {
        List<CustomerNameAndPhone> nameAndPhones = iCustomerService.nameAndPhone();
        return ServerResponse.createBySuccess(nameAndPhones);
    }

    /**
     * 手机号名字列表，value  手机号
     *
     * @return
     */
    @GetMapping("/phones/{vip}")
    public ServerResponse vipPhones(@PathVariable String vip) {
        List<Value> phones = iCustomerService.sendPhones(vip);
        return ServerResponse.createBySuccess(phones);
    }
    /**
     * 手机号名字列表，value  手机号
     *
     * @return
     */
    @GetMapping("/phones")
    public ServerResponse phones() {
        List<Value> phones = iCustomerService.sendPhones("");
        return ServerResponse.createBySuccess(phones);
    }
    /**
     * 查询手机号是否会员
     *
     * @param phone
     * @return Customer
     */
    @GetMapping("/checkPhone/{phone}")
    public ServerResponse checkPhone(@PathVariable String phone) {
        CustomerVo customer = iCustomerService.checkVipByPhone(phone);
        return ServerResponse.createBySuccess(customer);
    }
    /**
     * 生成会员号
     *
     * @return Customer
     */
    @GetMapping("/generate-nic")
    public ServerResponse generateNic() {
        String nic = iCustomerService.generateNic();
        return ServerResponse.createBySuccess(nic);
    }

    /**
     * 新增
     *
     * @param customerVo
     * @return
     */
    @PostMapping()
    @Validated(value = CustomerVo.Create.class)
    public ServerResponse add(@RequestBody CustomerVo customerVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iCustomerService.add(customerVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 编辑
     *
     * @param customerVo
     * @return
     */
    @PutMapping()
    @Validated(value = CustomerVo.Update.class)
    public ServerResponse edit(@RequestBody CustomerVo customerVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iCustomerService.edit(customerVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 充值
     * @param customerVo
     * @param errors
     * @return
     */
    @PutMapping("/recharge")
    @Validated(value = CustomerVo.Recharge.class)
    public ServerResponse recharge(@RequestBody CustomerVo customerVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iCustomerService.rechargeAmount(customerVo);
        return ServerResponse.createBySuccess();
    }


    /**
     * 顾客未使用金额
     * @return
     */
    @GetMapping("/getNotUserMoneySummary")
    public ServerResponse notUsedMoneySum(@RequestParam Integer months) {
        OrdersIncomeByTime notUsedMoney = iCustomerService.notUsedMoneySum(months);
        return ServerResponse.createBySuccess(notUsedMoney);
    }

    /**
     * 顾客未使用金额
     * @return
     */
    @GetMapping("/vipMoneySum")
    public ServerResponse vipMoneySum(@RequestParam Integer months) {
        OrdersIncomeByTime vipMoney = iCustomerService.vipMoneySum(months);
        return ServerResponse.createBySuccess(vipMoney);
    }
}
