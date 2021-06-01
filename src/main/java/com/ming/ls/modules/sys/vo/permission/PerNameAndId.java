package com.ming.ls.modules.sys.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PerNameAndId {
    private String id;
    private String name;
}
