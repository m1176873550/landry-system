package com.ming.ls.modules.sys.vo.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EmployeeListVo {
    private String id;
    private String idCard;
    private String fullName;
    private String loginName;

    /**
     * 洗衣店名
     */
    private String laundryName;
    private String laundryId;
    private String phone;
    private String isAdmin;
    private String address;
    private String des;
    private String password;
    /**
     * 附件
     */
    private String attachmentUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
}
