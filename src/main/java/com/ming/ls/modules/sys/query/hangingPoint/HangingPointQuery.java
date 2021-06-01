package com.ming.ls.modules.sys.query.hangingPoint;

import com.ming.ls.modules.sys.query.BaseQuery;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 *
 * @author : 明杰
 * @date : 2020/2/8 20:21
    挂点查询对象
 * @return : null
 */
@Data
@Accessors(chain = true)
public class HangingPointQuery extends BaseQuery {
    /**
     * 挂点
     */
    private String number;
    /**
     * 订单ID
     */
    private Integer orderId;
    /**
     * 是否完成待取
     */
    private String isFinished;
    /**
     *付款人名
     */
    private String payer;
    /**
     *顾客号码
     */
    private String phone;
    /**
     *是否返厂
     */
    private String isBack;
    /**
     *是否退款
     */
    private String refund;
    /**
     * 顾客ID
     */
    private String customerId;
    private String beanTook;


}
