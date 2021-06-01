package com.ming.ls.modules.sys.vo.serviceType;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ming.ls.modules.sys.vo.color.ColorVo;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class ServiceTypeVo {

    @TableId(type = IdType.ID_WORKER_STR)
    @NotNull(message = "服务类型ID不能为空",groups = ColorVo.Update.class)
    private String id;
    @NotNull(message = "服务类型名称不能为空",groups = {ColorVo.Create.class, ColorVo.Update.class})
    private String name;


    public static interface Create{

    }
    public static interface Update{

    }

}
