package com.ming.ls.modules.sys.vo.orders;


import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class OrdersGroupByTime {
    private String totalPrice;
    private String one;
    private String two;
    private String three;
    private String four;
    private String five;
    private String six;
    private String seven;
}
