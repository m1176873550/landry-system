package com.ming.ls.modules.sys.query.orders;

import com.ming.ls.modules.sys.query.BaseQuery;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class LogQuery extends BaseQuery {
    private String start;
    private String end;
}
