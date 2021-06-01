package com.ming.ls.modules.sys.query.permission;

import com.ming.ls.modules.sys.query.BaseQuery;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PermQuery extends BaseQuery {
    private String hasParent;
}
