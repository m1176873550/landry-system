package com.ming.ls.modules.sys.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
@Data
public class IdNameChildren {
    private String id;
    private String name;
    private String type;
    private String parentId;
    private List<IdNameChildren> children;
}
