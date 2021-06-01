package com.ming.ls.modules.sys.vo.hangingPoint;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class HangingPointGroup {
    private String id;
    private String name;
    private List<HangingPointGroup> children;
    /**
     * 是否可选
     */
    private boolean disabled;

}
