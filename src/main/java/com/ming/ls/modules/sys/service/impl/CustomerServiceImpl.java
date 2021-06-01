package com.ming.ls.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.IDKeyUtil;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.CustomerDao;
import com.ming.ls.modules.sys.entity.Customer;
import com.ming.ls.modules.sys.entity.Employee;
import com.ming.ls.modules.sys.entity.VipLog;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.query.customer.CustomerQuery;
import com.ming.ls.modules.sys.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.service.IVipLogService;
import com.ming.ls.modules.sys.vo.customer.CustomerNameAndPhone;
import com.ming.ls.modules.sys.vo.customer.CustomerVo;
import com.ming.ls.modules.sys.vo.orders.OrdersIncomeByTime;
import com.ming.ls.modules.sys.vo.value.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class CustomerServiceImpl extends ServiceImpl<CustomerDao, Customer> implements ICustomerService {

    @Autowired
    private ICustomerService iCustomerService;
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private IVipLogService vipLogService;

    @Override
    public IPage<CustomerVo> checkVipBirth(BaseQuery query) {
        IPage<CustomerVo> page = new Page<>(query.getCurrentPage(), query.getSize());
        LocalDate now = LocalDate.now();
        return customerDao.birthVipList(page, now, now.plusDays(1), now.plusDays(2));
    }

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public IPage list(CustomerQuery query) {
        IPage<Customer> page = new Page<>(query.getCurrentPage(), query.getSize());
        Employee currentUser = SessionUtil.getCurrentUser();
        LambdaQueryWrapper<Customer> customerWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(query.getKey())) {
            //纯数字
            if (ReUtil.isMatch("^[0-9]*$", query.getKey())) {
                customerWrapper.or().like(Customer::getPhone, query.getKey());
            } else {     //否则
                customerWrapper.like(Customer::getFullName, query.getKey());
            }
        }
        //管理员
        if (currentUser.getIsAdmin() != 1) {
            customerWrapper.eq(Customer::getLaundryId, currentUser.getLaundryId());
        }
        //查询会员
        if (StringUtils.equals(query.getIsVip(), Const.IS_VIP)) {
            customerWrapper.ne(Customer::getNic, "")
                    .isNotNull(Customer::getNic);
        }
        //是否传入排序字段,排序
        if (StringUtils.isNotBlank(query.getOrder())) {
            if (StringUtils.equals(query.getOrder(), "asc")) {
                switch (query.getOrderKey()) {
                    case "birthday":
                        customerWrapper.orderByAsc(Customer::getBirthday);
                        break;
                    case "balance":
                        customerWrapper.orderByAsc(Customer::getBalance);
                        break;
                    case "discount":
                        customerWrapper.orderByAsc(Customer::getDiscount);
                        break;
                    case "updatedAt":
                        customerWrapper.orderByAsc(Customer::getUpdatedAt);
                        break;
                }
            } else if (StringUtils.equals(query.getOrder(), "desc")) {
                switch (query.getOrderKey()) {
                    case "birthday":
                        customerWrapper.orderByDesc(Customer::getBirthday);
                        break;
                    case "balance":
                        customerWrapper.orderByDesc(Customer::getBalance);
                        break;
                    case "discount":
                        customerWrapper.orderByDesc(Customer::getDiscount);
                        break;
                    case "updatedAt":
                        customerWrapper.orderByDesc(Customer::getUpdatedAt);
                        break;
                }
            }
        }

        //店铺ID模糊查询
        if (StringUtils.isNotBlank(query.getLaundryId())) {
            customerWrapper.eq(Customer::getLaundryId, query.getLaundryId());
        }
        //结果
        return iCustomerService.page(page, customerWrapper);
    }

    /**
     * 检查ID
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkId(String id) {
        if (StringUtils.isNotEmpty(id)) {
            Customer c = iCustomerService.getById(id);
            return c != null;
        } else {
            return false;
        }
    }

    /**
     * 检查会员NIC号，返回手机号
     *
     * @param nic
     * @return
     */
    @Override
    public String checkNic(String nic) {
        //检查编号长度
        if (nic.length() == Const.ID_LENGTH) {
            LambdaQueryWrapper<Customer> nicWrapper = new LambdaQueryWrapper<Customer>()
                    .select(Customer::getPhone)
                    .eq(Customer::getNic, nic)
                    .eq(Customer::getLaundryId, SessionUtil.getCurrentUser().getLaundryId());
            Customer cus = iCustomerService.getOne(nicWrapper);
            if (cus != null) {
                return cus.getPhone();
            }
            return null;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "会员编号异常");
        }
    }


    /**
     * 返回用户姓名和电话的列表
     *
     * @return
     */
    @Override
    public List<CustomerNameAndPhone> nameAndPhone() {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        return customerDao.nameAndPhone(new BaseQuery().setLaundryId(laundryId));
    }

    /**
     * 检查会员手机号，返回VIP会员信息
     *
     * @param phone
     * @return
     */
    @Override
    public Customer checkPhone(String phone) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        return iCustomerService.getOne(new LambdaQueryWrapper<Customer>()
                .isNotNull(Customer::getNic)
                .ne(Customer::getNic, StringUtils.EMPTY)
                .eq(Customer::getLaundryId, laundryId)
                .eq(Customer::getPhone, phone));
    }

    /**
     * 检查普通手机号
     *
     * @param phone
     * @return
     */
    @Override
    public Customer checkNormalPhone(String phone) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        return iCustomerService.getOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getLaundryId, laundryId)
                .eq(Customer::getPhone, phone));
    }

    /**
     * 检查VIP，返回充值对象
     *
     * @param phone
     * @return
     */
    @Override
    public CustomerVo checkVipByPhone(String phone) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        Customer customer = iCustomerService.getOne(new LambdaQueryWrapper<Customer>()
                .isNotNull(Customer::getNic)
                .notIn(Customer::getNic, "")
                .eq(Customer::getLaundryId, laundryId).eq(Customer::getPhone, phone));
        CustomerVo customerVo = new CustomerVo();
        BeanUtil.copyProperties(customer, customerVo);
        return customerVo;
    }

    private Customer findPhoneCus(String phone) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        return iCustomerService.getOne(new LambdaQueryWrapper<Customer>()
                .eq(Customer::getNic, StringUtils.EMPTY)
                .eq(Customer::getLaundryId, laundryId)
                .eq(Customer::getPhone, phone));
    }

    /**
     * 新增
     *
     * @param customerVo
     * @return
     */
    @Override
    @Transactional
    public boolean add(CustomerVo customerVo) {
        if (StringUtils.isBlank(customerVo.getNic())) {
            customerVo.setNic(null);
        }

        Customer customer = new Customer();
        Customer vipIsExist = iCustomerService.checkPhone(customerVo.getPhone());
        if (vipIsExist != null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前手机号已被使用,新增失败");
        }
        //检查是否普通会员
        Customer phoneCus = findPhoneCus(customerVo.getPhone());
        if (phoneCus != null) {
            String id = phoneCus.getId();
            BeanUtil.copyProperties(customerVo, phoneCus, "id", "laundryId", "updatedAt");
            phoneCus.setId(id);
            if (iCustomerService.updateById(phoneCus)) {
                VipLog vipLog = new VipLog();
                vipLog.setAmount(phoneCus.getBalance()).setNic(phoneCus.getNic());
                vipLogService.recordLog(vipLog);
                return true;
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增会员失败，数据库异常");
            }
        }
        BeanUtil.copyProperties(customerVo, customer, "id", "laundryId", "updatedAt");
        if (StringUtils.isNotBlank(customer.getNic())) {
            VipLog vipLog = new VipLog();
            vipLog.setAmount(customer.getBalance()).setNic(customer.getNic());
            vipLogService.recordLog(vipLog);
        }
        //默认手机号后6为设置为密码
        customer.setPassword(customerVo.getPhone().substring(5, 11));
        if (iCustomerService.save(customer)) {
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增失败，数据库异常");
        }


    }

    /**
     * 编辑
     *
     * @param customerVo
     * @return
     */
    @Override
    @Transactional
    public boolean edit(CustomerVo customerVo) {
        //检查ID
        if (iCustomerService.checkId(customerVo.getId())) {
            Customer isExist = iCustomerService.checkPhone(customerVo.getPhone());
            //存在且手机号与当前用户不同
            if (isExist != null && !StringUtils.equals(isExist.getPhone(), customerVo.getPhone())) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前手机号已被使用，编辑失败");
            }
            //复制对象，排除字段
            Customer customer = new Customer();
            BeanUtil.copyProperties(customerVo, customer,
                    "balance", "nic", "laundryId", "updatedAt", "createdAt", "editorId", "creatorId");
            if (iCustomerService.updateById(customer)) {
                return true;
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "编辑失败，数据库异常");
            }
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "顾客ID异常");
        }
    }

    /**
     * ID返回顾客对象
     *
     * @param id
     * @return
     */
    @Override
    public Customer getCustomerById(String id) {
        iCustomerService.checkId(id);
        return iCustomerService.getById(id);
    }

    /**
     * 随机生成ID
     *
     * @return
     */
    @Override
    public String generateNic() {
        return IDKeyUtil.generatid().toString();
    }

    /**
     * 充值，新增会员,验证NIC,手机号，余额是否足够
     *
     * @param customerVo
     */
    @Override
    public void rechargeAmount(CustomerVo customerVo) {
        //新增会员
        if (StringUtils.isBlank(customerVo.getId()) && StringUtils.isNotBlank(customerVo.getNic())) {
            String phone = iCustomerService.checkNic(customerVo.getNic());
            if (StringUtils.isNotEmpty(phone)) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增失败，会员编号异常");
            }
            //是否存在用户
            Customer customer = iCustomerService.checkNormalPhone(customerVo.getPhone());
            //存在且手机号相同，编辑用户NIC,余额，地址，描述，生日,折扣
            if (customer != null && StringUtils.equals(customer.getPhone(), customerVo.getPhone())) {
                boolean update = updateById(customer
                        .setBalance(customerVo.getRechargeAmount())
                        .setNic(customerVo.getNic())
                        .setAddress(customerVo.getAddress())
                        .setDes(customerVo.getDes())
                        .setBirthday(customerVo.getBirthday())
                        .setDiscount(Float.valueOf(customerVo.getDiscount()))
                        .setFullName(customerVo.getFullName())
                );
                if (update) {
                    //充值登记
                    VipLog vipLog = new VipLog();
                    vipLog.setAmount(customerVo.getRechargeAmount()).setNic(customerVo.getNic());
                    vipLogService.recordLog(vipLog);
                    return;
                } else {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增失败，数据库异常");
                }
            }
            //用户不存在或手机号不同，新增
            customerVo.setBalance(customerVo.getRechargeAmount());
            if (iCustomerService.add(customerVo)) {
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增会员失败，数据库异常");
            }
            //普通充值通道
        } else if (iCustomerService.checkId(customerVo.getId())) {  //检查ID
            Customer isExist = iCustomerService.checkPhone(customerVo.getPhone());
            //存在且手机号与当前用户不同
            if (isExist != null && !StringUtils.equals(isExist.getPhone(), customerVo.getPhone())) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前手机号已被使用，编辑失败");
            }
            //复制对象，排除字段
            Customer customer = new Customer();
            if (isExist != null
                    && StringUtils.equals(isExist.getNic(), customerVo.getNic())
                    && customerVo.getRechargeAmount().compareTo(BigDecimal.valueOf(0)) > 0) {
                customer.setId(customerVo.getId()).setBalance(customerVo.getRechargeAmount().add(isExist.getBalance()))
                        .setFullName(customerVo.getFullName());
            }
            if (iCustomerService.updateById(customer)) {
                VipLog vipLog = new VipLog();
                vipLog.setAmount(customerVo.getRechargeAmount()).setNic(customerVo.getNic());
                vipLogService.recordLog(vipLog);
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "充值失败，数据库异常");
            }
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "顾客ID异常");
        }


    }


    @Override
    public OrdersIncomeByTime notUsedMoneySum(Integer months) {
        LocalDate startMonth;
        LocalDate endMonth = LocalDate.now();
        if (months == 0) {
            startMonth = LocalDate.ofEpochDay(0);
        } else {
            startMonth = endMonth.minusMonths(months);
        }
        return customerDao.notUsedMoneySum(startMonth, endMonth);
    }

    @Override
    public OrdersIncomeByTime vipMoneySum(Integer months) {
        LocalDate startMonth;
        LocalDate endMonth = LocalDate.now();
        if (months == 0) {
            startMonth = LocalDate.ofEpochDay(0);
        } else {
            startMonth = endMonth.minusMonths(months);
        }
        return customerDao.vipMoneySum(startMonth, endMonth);
    }

    /**
     * 顾客或会员号码列表
     *
     * @return
     */
    @Override
    public List<Value> sendPhones(String vip) {
        List<Value> phones = new LinkedList<>();
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<Customer> phoneWrapper = new LambdaQueryWrapper<Customer>()
                .select(Customer::getPhone, Customer::getFullName)
                .eq(Customer::getLaundryId, laundryId);
        if (StringUtils.equals(vip, Const.IS_VIP)) {
            phoneWrapper.isNotNull(Customer::getNic).ne(Customer::getNic, StringUtils.EMPTY);
        }
        List<Customer> customers = customerDao.selectList(phoneWrapper);
        for (Customer customer : customers) {
            Value var = new Value();
            var.setValue(customer.getPhone());
            var.setName(customer.getFullName());
            phones.add(var);
        }
        return phones;
    }
}
