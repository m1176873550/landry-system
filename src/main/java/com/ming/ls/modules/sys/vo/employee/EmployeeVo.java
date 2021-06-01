package com.ming.ls.modules.sys.vo.employee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class EmployeeVo {

    @TableId(type = IdType.ID_WORKER_STR)
    @NotNull(message = "职员ID不能为空", groups = {Update.class})
    private String id;

    private String laundryId;

    @NotNull(message = "职员全名不能为空", groups = {Create.class, Update.class})
    private String fullName;
    @NotNull(message = "登录名称不能为空", groups = {Create.class, Update.class})
    private String loginName;

    private String address;

    @NotNull(message = "手机号码不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = "^[1]([3-9])[0-9]{9}$",
            message = "手机号码格式错误", groups = {Create.class, Update.class})
    private String phone;

    private List<String> roleId;
    @NotNull(message = "身份证号不能为空", groups = {Create.class, Update.class})
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$",
            message = "身份证号格式错误", groups = {Create.class, Update.class})
    private String idCard;

    private String des;

    public static interface Create {

    }

    public static interface Update {

    }
}
