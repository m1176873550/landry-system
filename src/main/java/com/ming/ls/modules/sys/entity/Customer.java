package com.ming.ls.modules.sys.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 洗衣店ID
     */
    @TableField(value = "laundry_id", fill = FieldFill.INSERT, strategy = FieldStrategy.NOT_NULL)
    private String laundryId;
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private String phone;
    @TableField(value = "is_remind")
    private Boolean remind;
    /**
     * 会员NIC
     */
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private String nic;
    /**
     * 余额
     */
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private BigDecimal balance;
    /**
     * 折扣
     */
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private Float discount;
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private String fullName;
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private String password;

    /**
     * 地址
     */
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private String address;
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private String des;
    @TableField(strategy = FieldStrategy.NOT_NULL)
    private LocalDate birthday;

    @TableField(value = "created_at", fill = FieldFill.INSERT, strategy = FieldStrategy.NOT_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE, strategy = FieldStrategy.NOT_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT, strategy = FieldStrategy.NOT_NULL)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE, strategy = FieldStrategy.NOT_NULL)
    private String editorId;


}
