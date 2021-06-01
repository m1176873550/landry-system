package com.ming.ls.modules.sys.vo.color;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ColorVo {
    @TableId(type = IdType.ID_WORKER_STR)
    @NotNull(message = "颜色ID不能为空",groups = Update.class)
    private String id;
    @NotNull(message = "颜色名称不能为空",groups = {Create.class,Update.class})
    private String name;


    public static interface Create{

    }
    public static interface Update{

    }
}
