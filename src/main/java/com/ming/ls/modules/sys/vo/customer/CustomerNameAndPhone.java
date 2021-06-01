package com.ming.ls.modules.sys.vo.customer;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerNameAndPhone {
    private String value;
    private String phone;
}
