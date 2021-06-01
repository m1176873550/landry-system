package com.ming.ls.modules.sys.vo.orders;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class OrdersVo {
    private String laundryId;
    private String laundryName;
    /**
     * 订单ID
     */
    private String id;
    private String ordersId;
    private Integer isPay;
    private Integer isFinished;
    private BigDecimal totalPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd" ,timezone = "GMT+8")
    private LocalDateTime packingTime;
    private Boolean remind;
    private String creator;
    private String payer;
    private String phone;
    private String address;
}
