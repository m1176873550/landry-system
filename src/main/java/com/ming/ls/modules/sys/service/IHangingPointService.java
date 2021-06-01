package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.entity.HangingPoint;
import com.ming.ls.modules.sys.query.hangingPoint.HangingPointQuery;
import com.ming.ls.modules.sys.vo.hangingPoint.GenerateHangingPoint;
import com.ming.ls.modules.sys.vo.hangingPoint.HangingPointVo;
import com.ming.ls.modules.sys.vo.hangingPoint.IsPayedObj;
import com.ming.ls.modules.sys.vo.hangingPoint.OrderVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 挂点服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IHangingPointService extends IService<HangingPoint> {
    /**
     * 列表
     *
     * @param query
     * @return
     */
    IPage<HangingPointVo> list(HangingPointQuery query);

    /**
     * 增
     *
     * @return
     */
    boolean add(HangingPointVo hangingPointVo);

    /**
     * 编辑
     *
     * @return
     */
    boolean edit(HangingPointVo hangingPointVo);

    /**
     * 检查ID
     *
     * @param id
     */
    void checkId(String id);

    /**
     * 检查ID
     *
     * @param id
     */
    void del(String id);
    /**
     * 返回查询名称对象
     *
     * @param name
     * @return
     */
    HangingPoint byName(String name);

    /**
     * 顺序ID生成
     *
     * @return
     */
    List<Integer> generateId(int numbers);

    /**
     * 生成的挂点对象
     *
     * @return
     */
    GenerateHangingPoint generateHangingPoint(GenerateHangingPoint hangingPoint);

    /**
     * 提交订单
     *
     * @return
     */
    OrderVo submit(OrderVo orderVo);

    /**
     * 新建用户，存在直接赋值
     *
     * @param hangingPointVo
     * @return
     */
    boolean addCustAndSetVal(HangingPointVo hangingPointVo, HangingPoint hangingPoint);

    /**
     * 批量插入挂点
     *
     * @param hangingPointVos
     * @return
     */
    boolean addList(List<GenerateHangingPoint> hangingPointVos, LocalDateTime pickingTime);

    /**
     * 修改是否取衣
     *
     * @param id
     * @return
     */
    boolean changeBeanTook(String id);

    /**
     * 修改已付款
     *
     * @return
     */
    boolean changeIsPayed(IsPayedObj isPayedObj);

    BigDecimal getIncomeByDay();



}
