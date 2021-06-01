package com.ming.ls.modules.sys.vo.hangingPoint;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class IsPayedObj {
    private String id;
    private BigDecimal price;
    private String payer;
    private String isPay;
    private String phone;
}
