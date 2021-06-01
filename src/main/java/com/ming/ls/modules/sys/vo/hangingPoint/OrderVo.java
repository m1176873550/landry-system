package com.ming.ls.modules.sys.vo.hangingPoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ming.ls.modules.sys.entity.HangingPoint;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author : 明杰
 * @date : 2020/2/12 17:18
    前台发过来订单信息
 * @return : null
 */
@Data
@Accessors(chain = true)
public class OrderVo {
    private String ordersId;
    private String receivePayment;
    private String actualPayment;
    private String name;
    private Integer printTimes;
    private String phone;
    /**
     * 0-未支付，1支付
     */
    private Boolean isPayed;
    /**
     * 会员号
     */
    private String nic;
    private String pickingTime;
    private String payMethod;
    private List<GenerateHangingPoint> hangingPoints;

    private String creator;
}
