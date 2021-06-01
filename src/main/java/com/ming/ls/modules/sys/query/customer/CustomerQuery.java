package com.ming.ls.modules.sys.query.customer;

import com.ming.ls.modules.sys.query.BaseQuery;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors
@Data
public class CustomerQuery extends BaseQuery {
    private String isVip;
    private String phone;

}
