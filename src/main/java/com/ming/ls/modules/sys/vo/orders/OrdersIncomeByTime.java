package com.ming.ls.modules.sys.vo.orders;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class OrdersIncomeByTime {
    private BigDecimal totalPrice;
    private String one="0";
    private String two="0";
    private String three="0";
    private String four="0";
    private String five="0";
    private String six="0";
    private String seven="0";
    private String eight="0";
    private String nine="0";
    private String ten="0";
    private String eleven="0";
    private String twelve="0";
    private Integer monthTime;
    private BigDecimal income;
}
