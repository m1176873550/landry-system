package com.ming.ls.modules.sys.vo.Laundry;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IdAndNameList {
    private String id;
    private String name;
}
