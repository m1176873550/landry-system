package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.annotation.Authorized;
import com.ming.ls.modules.common.sysConst.PermissionConst;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.query.hangingPoint.HangingPointQuery;
import com.ming.ls.modules.sys.service.IHangingPointService;
import com.ming.ls.modules.sys.vo.hangingPoint.GenerateHangingPoint;
import com.ming.ls.modules.sys.vo.hangingPoint.HangingPointVo;
import com.ming.ls.modules.sys.vo.hangingPoint.IsPayedObj;
import com.ming.ls.modules.sys.vo.hangingPoint.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/hangingPoint")
public class HangingPointController {
    @Autowired
    private IHangingPointService iHangingPointService;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @PostMapping
    public ServerResponse list(@RequestBody HangingPointQuery query) {
        IPage<HangingPointVo> list = iHangingPointService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 编辑
     *
     * @param hangingPointVo
     * @param errors
     * @return
     */
    @PutMapping
    public ServerResponse edit(@Validated(value = HangingPointVo.Update.class) @RequestBody HangingPointVo hangingPointVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iHangingPointService.edit(hangingPointVo);
        return ServerResponse.createBySuccess();

    }

    /**
     * 新增
     *
     * @param hangingPointVo
     * @return
     */
    @PostMapping("/add")
    public ServerResponse add( @RequestBody HangingPointVo hangingPointVo) {
        iHangingPointService.add(hangingPointVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse del(@PathVariable String id) {
        iHangingPointService.del(id);
        return ServerResponse.createBySuccess();
    }

    /**
     * 生成挂点对象，返回到前台
     *
     * @param hangingPoint
     * @return
     */
    @PostMapping("/generate-hanging-point")
    public ServerResponse generateHangingPoint(@RequestBody GenerateHangingPoint hangingPoint) {
        GenerateHangingPoint ghp = iHangingPointService.generateHangingPoint(hangingPoint);
        return ServerResponse.createBySuccess(ghp);
    }

    /**
     * 生成挂点对象，返回到前台
     *
     * @param orderVo
     * @return
     */
    @PostMapping("/submit-orders")
    public ServerResponse submitOrders(@RequestBody OrderVo orderVo) {
        OrderVo submit = iHangingPointService.submit(orderVo);
        return ServerResponse.createBySuccess(submit);
    }

    /**
     * 修改已被取衣
     *
     * @param id
     * @return
     */
    @PutMapping("/{id}/change-bean-took")
    public ServerResponse changeBeanTook(@PathVariable String id) {
        iHangingPointService.changeBeanTook(id);
        return ServerResponse.createBySuccess();
    }

    /**
     * 修改是否付款状态
     * @return
     */
    @PutMapping("/change-is-payed")
    public ServerResponse changeIsPayed(@RequestBody IsPayedObj isPayedObj) {
        iHangingPointService.changeIsPayed(isPayedObj);
        return ServerResponse.createBySuccess();
    }

    /**
     *
     * @return
     */
    @GetMapping("/getIncomeByDay")
    public ServerResponse getIncomeByDay() {
        BigDecimal income = iHangingPointService.getIncomeByDay();
        return ServerResponse.createBySuccess(income);
    }
}
