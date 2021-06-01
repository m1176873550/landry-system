package com.ming.ls.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.config.RabbitSender;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.TimeUtil;
import com.ming.ls.modules.sys.dao.HangingPointDao;
import com.ming.ls.modules.sys.entity.*;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.query.hangingPoint.HangingPointQuery;
import com.ming.ls.modules.sys.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.hangingPoint.GenerateHangingPoint;
import com.ming.ls.modules.sys.vo.hangingPoint.HangingPointVo;
import com.ming.ls.modules.sys.vo.hangingPoint.IsPayedObj;
import com.ming.ls.modules.sys.vo.hangingPoint.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.ming.ls.modules.common.sysConst.PeriodConst.*;
import static com.ming.ls.modules.common.util.IDKeyUtil.generatid;
import static com.ming.ls.modules.sys.TimeUtil.strToLocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Service
public class HangingPointServiceImpl extends ServiceImpl<HangingPointDao, HangingPoint> implements IHangingPointService {

    @Autowired
    private HangingPointDao hangingPointDao;
    /**
     * 客户服务类
     */
    @Autowired
    private ICustomerService iCustomerService;

    @Autowired
    private IHangingPointService iHangingPointService;


    /**
     * 挂点服务类型服务层
     */
    @Autowired
    private IDictionaryService dictionaryService;

    @Autowired
    private IOrdersService iOrdersService;

    @Autowired
    private IClothingTypeService iClothingTypeService;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public IPage<HangingPointVo> list(HangingPointQuery query) {

        IPage<HangingPointVo> page = new Page<>(query.getCurrentPage(), query.getSize());
        Employee currentUser = SessionUtil.getCurrentUser();
        query.setIsAdmin(currentUser.getIsAdmin());
        //检查是否是顾客
        boolean isCustomer = iCustomerService.checkId(query.getCustomerId());
        if (isCustomer) {
            Customer customer = iCustomerService.getById(query.getCustomerId());
            query.setLaundryId(customer.getLaundryId());
            //店员
        } else {
            query.setCustomerId("")
                    .setLaundryId(currentUser.getLaundryId());
        }
        return hangingPointDao.list(page, query);
    }

    /**
     * 新增
     *
     * @param hangingPointVo
     * @return
     */
    @Override
    @Transactional
    public boolean add(HangingPointVo hangingPointVo) {
        HangingPoint hangingPoint = new HangingPoint();
        BeanUtil.copyProperties(hangingPointVo, hangingPoint,
                "number", "laundryId", "id", "serviceTypeId", "orderId", "pickingTime");

        //设置取衣时间
        hangingPoint.setServiceTypes(hangingPointVo.getServiceTypes());
        hangingPoint.setPickingTime(hangingPointVo.getPickingTime2());

        //票单号
        Integer id = iOrdersService.generateId();
        hangingPoint.setOrderId(id);

        //生成未使用的顺序挂点
        List<Integer> number = iHangingPointService.generateId(1);
        hangingPoint.setNumber(number.get(0));

        //插入订单
        Orders orders = new Orders().setId(id).setPeriod(getPeriod());
        iOrdersService.save(orders);

        //新建或检查用户，并赋值
        return checkCus(hangingPointVo, hangingPoint);
    }

    private boolean checkCus(HangingPointVo hangingPointVo, HangingPoint hangingPoint) {
        //新建或检查用户，并赋值
        iHangingPointService.addCustAndSetVal(hangingPointVo, hangingPoint);
        //如果是会员，且编号正确
        Customer vip = iCustomerService.checkPhone(hangingPointVo.getPhone());
        if (vip != null && StringUtils.equals(vip.getNic(), hangingPointVo.getNic())) {
            //会员余额是否够
            if (vip.getBalance().subtract(hangingPoint.getPrice()).compareTo(new BigDecimal(0)) < 0) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "会员余额不足，请充值");
            }
            //更新会员余额
            vip.setBalance(vip.getBalance().subtract(hangingPoint.getPrice()));
            iCustomerService.updateById(vip);
        }
        if (hangingPointDao.insert(hangingPoint) > 0) {
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增挂点订单失败，数据库异常");
        }
    }

    /**
     * 编辑
     *
     * @param hangingPointVo
     * @return
     */
    @Override
    @Transactional
    public boolean edit(HangingPointVo hangingPointVo) {
        HangingPoint hangingPoint = new HangingPoint();
        BeanUtil.copyProperties(hangingPointVo, hangingPoint,
                "number", "laundryId", "serviceTypeId", "orderId","pickingTime");
        //新建或检查用户，并赋值
        iHangingPointService.addCustAndSetVal(hangingPointVo, hangingPoint);
        hangingPoint.setPickingTime(hangingPointVo.getPickingTime2());
        if (hangingPointDao.updateById(hangingPoint) > 0) {
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "编辑挂点订单失败，数据库异常");
        }
    }


    private void updateOrder(Integer orderId){
        LambdaQueryWrapper<HangingPoint> countFinishWrapper = new LambdaQueryWrapper<HangingPoint>()
                .select(HangingPoint::getOrderId)
                .eq(HangingPoint::getBeanTook, 0)
                .eq(HangingPoint::getOrderId,orderId);
        List<HangingPoint> hp = iHangingPointService.list(countFinishWrapper);
        if (hp !=null && hp.size()==1){
            Orders o = iOrdersService.getById(orderId);
            o.setIsFinished(1);
            iOrdersService.updateById(o);
        }
    }
    @Override
    public void checkId(String id) {
        if (hangingPointDao.selectById(id) == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "挂点ID异常");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(String id) {
        try {
            //删除的对象
            HangingPoint hp = iHangingPointService.getById(id);
            //修改订单表数据
            LambdaQueryWrapper<HangingPoint> hpWrapper = new LambdaQueryWrapper<HangingPoint>()
                    .select(HangingPoint::getId)
                    .eq(HangingPoint::getOrderId, hp.getOrderId());
            int count = iHangingPointService.count(hpWrapper);

            //超过一条，修改订单价格
            if (count > 1) {
                Orders order = iOrdersService.getById(hp.getOrderId());
                order.setTotalPrice(order.getTotalPrice().subtract(hp.getPrice()));
                if (!iOrdersService.updateById(order)) {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "更新订单表数据异常，删除失败");
                }
                //只有一条挂点对应订单，直接删除
            } else if (count == 1) {
                iOrdersService.removeById(hp.getOrderId());
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "挂点ID异常，删除失败");
            }

            //删除挂点挂的数据
            iHangingPointService.removeById(id);

        } catch (LsServiceException e) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "删除挂点失败，数据库异常");
        }
    }

    @Override
    public HangingPoint byName(String name) {
        return null;
    }

    /**
     * 顺序挂点生成
     *
     * @param count
     * @return
     */
    @Override
    public List<Integer> generateId(int count) {
        //返回对象
        List<Integer> returnNumbers = new LinkedList<>();
        Employee currentUser = SessionUtil.getCurrentUser();
        BaseQuery query = new BaseQuery()
                .setIsAdmin(currentUser.getIsAdmin())
                .setLaundryId(currentUser.getLaundryId());
        //已生成的挂点列表
        List<Integer> numbers = hangingPointDao.generateId(query);
        //有挂点存在
        if (numbers.size() > 0) {
            //List从0开始,所以减一
            int lastNumber = numbers.size() - 1;
            //当前最大的挂点值
            int max = numbers.get(lastNumber);
            //计算1-max中是否全部已存在
            //从numbers的第一个元素开始对比，知道最后
            int number;
            for (number = 1; number <= max + count; number++) {
                boolean numberExist = false;
                //直到生成count个挂点
                //判断是否存在
                for (int currentNumber : numbers) {
                    //如果没找到，就直接返回当前值
                    //从1计数，到max时
                    if (currentNumber == number) {
                        numberExist = true;
                        //退出第一层循环到            if (numberExist){
                        break;
                    }
                }
                if (numberExist) {
                    //到         for (number = 1; number <= max; number++) {
                    continue;
                }
                returnNumbers.add(number);
                if (count == returnNumbers.size()) {
                    return returnNumbers;
                }
            }
        } else {
            int startNumber;
            for (startNumber = 1; startNumber <= count; startNumber++) {
                numbers.add(startNumber);
            }
            return numbers;
        }
        //无挂点存在直接生成1
        throw new LsServiceException(ResponseCode.ERROR.getCode(), "生成挂点异常");
    }

    /**
     * 生成的挂点对象,并查询衣物类型ID，赋值
     *
     * @return
     */
    @Override
    public GenerateHangingPoint generateHangingPoint(GenerateHangingPoint hangingPoint) {
        ClothingType clothingType = iClothingTypeService.checkName(hangingPoint.getClothType());
        if (clothingType != null) {
            hangingPoint.setClothTypeId(clothingType.getId());
            return hangingPoint;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "无当前衣物类型");
        }
    }

    /**
     * 提交数据，生成订单，返回订单
     *
     * @param orderVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderVo submit(OrderVo orderVo) {
        //生成的订单ID
        Integer integer = iOrdersService.generateId();
        //对挂点列表处理
        List<GenerateHangingPoint> hangingPoints = orderVo.getHangingPoints();
        //格式化收钱金额
        BigDecimal price = new BigDecimal(orderVo.getActualPayment());
        //计数订单条数,条数大于0，才计入订单
        int size = hangingPoints.size();
        if (size > 0) {
            //查询付款人是否是会员
            Customer customer = iCustomerService.checkPhone(orderVo.getPhone());
            //不为空是会员，且会员号相同
            if (customer != null
                    && StringUtils.equals(orderVo.getNic(), customer.getNic())
                    && orderVo.getIsPayed()) {
                //余额足够
                if (customer.getBalance().compareTo(price) > 0) {
                    customer.setBalance(customer.getBalance().subtract(price));
                    //扣钱
                    if (!iCustomerService.updateById(customer)) {
                        throw new LsServiceException(ResponseCode.ERROR.getCode(), "会员消费失败，数据库异常");
                    }
                    //提示余额不足
                } else {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "会员消费失败，余额不足");
                }
            }

            //设置addCustAndSetVal所需参数 phone,payerName，执行完设置addCustAndSetVal所需参数（）后，hangingPoint付款人iD
            HangingPointVo hangingPointVo = new HangingPointVo()
                    .setPhone(orderVo.getPhone())
                    .setPayer(orderVo.getName());
            HangingPoint hangingPoint = new HangingPoint();
            //若用户不存在，新建用户，存在直接赋值给hangingPoint的payerId，执行后，hangingPoint有付款人
            iHangingPointService.addCustAndSetVal(hangingPointVo, hangingPoint);
            //生成对应挂点
            List<Integer> numbers = iHangingPointService.generateId(size);
            //订单挂点赋值,包括挂点号，订单号，付款人ID
            for (int i = 0; i < numbers.size(); i++) {
                hangingPoints.get(i).setNumber(numbers.get(i))
                        .setOrderId(integer)
                        .setPayerId(hangingPoint.getPayerId())
                        .setIsPayed(orderVo.getIsPayed() ? 1 : 0);
            }
            //批量插入订单列表
            iHangingPointService.addList(hangingPoints, strToLocalDateTime(orderVo.getPickingTime()));
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "请选中有效数据");
        }
        //字典表查询最大0数
        Integer zeroCount = dictionaryService.findValueByName(Const.DICTIONARY_ZERO_COUNT_NAME);
        //0的个数
        int countZero = zeroCount - String.valueOf(integer).length();
        StringBuilder id = new StringBuilder("LS");
        //插入0
        for (int count = 0; count < countZero; count++) {
            id.append("0");
        }
        id.append(integer.toString());
        //格式化后的订单赋值
        orderVo.setOrdersId(id.toString());
        //返回赋值的订单
        Orders orders = new Orders();
        //插入订单
        String period = getPeriod();
        orders.setIsFinished(0)
                .setTotalPrice(price)
                .setId(integer)
                .setIsPay(orderVo.getIsPayed() ? 1 : 0)
                .setPeriod(period);
        //新增
        if (!iOrdersService.save(orders)) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增订单失败，数据库异常");
        }
        //赋值操作人名称
        orderVo.setCreator(SessionUtil.getCurrentUser().getFullName());
        //返回打印对象
        return orderVo;
    }


    private String getPeriod() {
        int hour = LocalDateTime.now().getHour();
        switch (hour) {
            case 9:
            case 10:
                return NINE_ELEVEN;
            case 11:
            case 12:
                return ELEVEN_THIRTEEN;
            case 13:
            case 14:
                return THIRTEEN_FIFTEEN;
            case 15:
            case 16:
                return FIFTEEN_SEVENTEEN;
            case 17:
            case 18:
                return SEVENTEEN_NINETEEN;
            case 19:
            case 20:
                return NINETEEN_TWENTY_ONE;
            case 21:
            case 22:
            case 23:
                return TWENTY_ONE_TWENTY_THREE;
            default:
                return NINE_ELEVEN;
        }
    }

    /**
     * 插入挂点列表
     *
     * @param hangingPointVos
     * @return
     */
    @Override
    @Transactional(rollbackFor = LsServiceException.class)
    public boolean addList(List<GenerateHangingPoint> hangingPointVos, LocalDateTime pickingTime) {
        //插入到数据库的列表对象
        List<HangingPoint> hangingPointList = new LinkedList<>();
        //入参的长度
        //为插入列表对象赋值
        for (GenerateHangingPoint ghp : hangingPointVos) {
            //插入到数据库的列表对象中的一个
            HangingPoint addPoint = new HangingPoint();
            addPoint.setNumber(ghp.getNumber())
                    .setPrice(new BigDecimal(ghp.getPrice()))
                    .setPayerId(ghp.getPayerId())
                    .setOrderId(ghp.getOrderId())
                    .setClothTypeId(ghp.getClothTypeId())
                    .setColorId(ghp.getColorId())
                    .setBrandId(ghp.getBrandId())
                    .setServiceTypes(ghp.getService())
                    .setDes(ghp.getDes())
                    .setIsPay(ghp.getIsPayed())
                    .setBeanTook(0)
                    .setPickingTime(pickingTime);
            hangingPointList.add(addPoint);
        }
        //未自动注入的信息
        Employee employee = SessionUtil.getCurrentUser();
        String userId = employee.getId();
        String laundryId = employee.getLaundryId();
        LocalDateTime now = LocalDateTime.now();
        for (HangingPoint h : hangingPointList) {
            h.setId(String.valueOf(generatid())).setCreatedAt(now).setUpdatedAt(now).setCreatorId(userId).setEditorId(userId).setLaundryId(laundryId);
        }
        iHangingPointService.saveBatch(hangingPointList);
        return true;
    }

    /**
     * 修改为已被取衣
     *
     * @param id
     * @return
     */
    @Override
    public boolean changeBeanTook(String id) {
        iHangingPointService.checkId(id);
        HangingPoint hangingPoint = new HangingPoint();
        hangingPoint.setId(id).setBeanTook(1);
        HangingPoint byId = iHangingPointService.getById(id);
        updateOrder(byId.getOrderId());
        if (iHangingPointService.updateById(hangingPoint)) {
            //若全部都被取，更新订单状态为完成
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "修改衣物是否被取失败，数据库异常");
        }
    }

    /**
     * 修改已付款
     *
     * @return
     */
    @Override
    public boolean changeIsPayed(IsPayedObj isPayedObj) {
        iHangingPointService.checkId(isPayedObj.getId());
        HangingPoint hangingPoint = new HangingPoint();
        hangingPoint.setId(isPayedObj.getId()).setIsPay(1);
        //更新订单对象
        Orders order = new Orders();
        HangingPoint hpById = iHangingPointService.getById(isPayedObj.getId());
        order.setId(hpById.getOrderId()).setTotalPrice(isPayedObj.getPrice()).setIsPay(1);
        if (iHangingPointService.updateById(hangingPoint)) {
            iOrdersService.updateById(order);
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "修改衣物是否被取失败，数据库异常");
        }
    }

    @Override
    public BigDecimal getIncomeByDay() {
        return hangingPointDao.getIncomeByDay(LocalDate.now()).add(hangingPointDao.getVipIncomeByDay(LocalDate.now()));
    }

    /**
     * 新建用户，存在直接赋值
     *
     * @param hangingPointVo
     * @return
     */
    @Override
    public boolean addCustAndSetVal(HangingPointVo hangingPointVo, HangingPoint hangingPoint) {
        if (StringUtils.isNotBlank(hangingPointVo.getPhone())) {
            Customer customer = iCustomerService.checkNormalPhone(hangingPointVo.getPhone());
            //检查用户是否存在
            if (customer != null) {
                //查看用户名是否一致，
                if (StringUtils.equals(hangingPointVo.getPayer(), customer.getFullName())) {
                    hangingPoint.setPayerId(customer.getId());
                    //不一致，修改
                } else {
                    //当用户不是会员，修改用户名字
                    if (!StringUtils.isNotEmpty(customer.getNic())) {
                        customer.setFullName(hangingPointVo.getPayer());
                        //判断是否存库成功
                        if (!iCustomerService.updateById(customer)) {
                            throw new LsServiceException(ResponseCode.ERROR.getCode(), "修改顾客姓名失败，数据库异常");
                            //更新成功
                        } else {
                            hangingPoint.setPayerId(customer.getId());
                        }
                        //是会员，不修改，直接判断，会员名不正确
                    } else {
                        throw new LsServiceException(ResponseCode.ERROR.getCode(), "消费顾客与用户姓名不一致");
                    }
                }
                return true;
                //顾客不存在
            } else {
                //生成订单ID
                String custId = iOrdersService.generateId().toString();
                Customer cust = new Customer()
                        .setFullName(hangingPointVo.getPayer())
                        .setPhone(hangingPointVo.getPhone())
                        .setId(custId)
                        .setPassword(hangingPointVo.getPhone().substring(5, 11));
                //更新
                if (iCustomerService.save(cust)) {
                    //对插入挂点列表对象赋值
                    hangingPoint.setPayerId(custId);
                    return true;
                } else {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "新曾顾客失败，数据库异常");
                }
            }
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "顾客手机号不能为空");
        }
    }
}
