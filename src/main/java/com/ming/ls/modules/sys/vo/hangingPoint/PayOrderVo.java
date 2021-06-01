package com.ming.ls.modules.sys.vo.hangingPoint;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class PayOrderVo {
    private String ordersId;
    private String actualPayment;
}
