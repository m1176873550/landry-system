package com.ming.ls.modules.sys.vo.cloth;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ming.ls.modules.sys.vo.employee.EmployeeVo;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 衣物Vo
 *
 * @author : 明杰
 * @date : 2020/2/7 19:42
 * @return : null
 */
@Data
@Accessors(chain = true)
public class ClothVo {
    @NotNull(message = "衣物类型ID不能为空", groups = Update.class)
    private String id;
    @NotNull(message = "衣物类型名称不能为空", groups = {Create.class, Update.class})
    private String name;
    /**
     * 父级名字
     */
    @NotNull(message = "衣物类型父级名称不能为空", groups = {Create.class})
    private String parentName;
    /**
     * ID
     */
    @NotNull(message = "衣物类型父级ID不能为空", groups = {Update.class})
    private String parentId;
    @NotNull(message = "衣物类型价格不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = "[1-9]\\d*.\\d*|0\\.\\d*[1-9]\\d*",
            message = "衣物类型价格格式错误", groups = {Create.class, Update.class})
    private String price;

    public static interface Update {
    }

    public static interface Create {
    }
}
