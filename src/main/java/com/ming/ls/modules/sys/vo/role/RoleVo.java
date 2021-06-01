package com.ming.ls.modules.sys.vo.role;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoleVo {
    @NotNull(message = "角色ID不能为空", groups = Updated.class)
    private String id;
    @NotNull(message = "角色名称不能为空", groups = {Updated.class, Created.class})
    private String name;
    @NotNull(message = "角色状态不能为空", groups = {Updated.class, Created.class})
    private Integer status;
    private String des;
    private List<String> permissionIds;

    public static interface Created {

    }

    public static interface Updated {
    }
}
