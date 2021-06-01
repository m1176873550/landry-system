package com.ming.ls.modules.sys.vo.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CustomerVo {
    @NotNull(message = "顾客ID不能为空",groups = {Update.class})
    private String id;
    @NotNull(message = "顾客姓名不能为空",groups = {Update.class,Create.class})
    private String fullName;
    private String laundryId;
    private String des;
    private String discount;
    private BigDecimal balance;
    @NotNull(message = "顾客会员编码不能为空",groups = {Recharge.class})
    private String nic;
    @NotNull(message = "顾客手机号不能为空",groups = {Update.class,Create.class,Recharge.class})
    private String phone;
    private BigDecimal rechargeAmount;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthday;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
    private Boolean remind;

    public static interface Create{}
    public static interface Update{}
    public static interface Recharge{}

}
