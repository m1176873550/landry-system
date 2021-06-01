package com.ming.ls.modules.sys.vo.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
public class PermissionListVo {
    @NotNull(message = "权限ID不能为空", groups = Update.class)
    private String id;
    /**
     * 权限路径
     */
    private String url;
    @NotNull(message = "权限类型不能为空", groups = {Update.class,Create.class})
    private String type;
    private String code;
    /**
     * 权限名
     */
    @NotNull(message = "权限名称不能为空", groups = {Update.class,Create.class})
    private String name;
    @NotNull(message = "权限状态不能为空", groups = {Update.class,Create.class})
    private Integer status;
    private String parentId;
    private List<PermissionListVo> children;

    /**
     * 描述
     */
    private String des;
    /**
     * 权限级别
     */
    private Integer level;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private String updatedAt;

    public static interface Create {
    }

    public static interface Update {
    }
}
