package com.ming.ls.modules.sys.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PerNameAndUrl {
    private String id;
    private String name;
    private String url;
}
