package com.ming.ls.modules.sys.vo.permission;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class PerIdAndStatus {
    @NotNull(message = "权限ID不能为空")
    private String id;
    @NotNull(message = "权限状态不能为空")
    private Integer status;
}
