package com.ming.ls.modules.sys.vo.employee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class LoginVo {

    @NotNull(message = "登陆名不能为空")
    private String name;
    @NotNull(message = "密码不能为空")
    private String password;
    private String newPassword;
    private String idCard;

}
