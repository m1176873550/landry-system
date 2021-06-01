package com.ming.ls.modules.sys.vo.cloth;


import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
public class ClothTypeGroup {
    private String value;
    private String label;
    private BigDecimal price;
    private List<ClothTypeGroup> children;
    /**
     * 是否可选
     */
    private boolean disabled;
}
