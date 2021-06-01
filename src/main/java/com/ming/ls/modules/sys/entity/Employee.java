package com.ming.ls.modules.sys.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mj
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("employee")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 洗衣店ID
     */
    @TableField(value = "laundry_id", fill = FieldFill.INSERT)
    private String laundryId;
    /**
     * 身份证号
     */
    private String idCard;

    private String fullName;

    private String loginName;

    private String phone;

    private Integer isAdmin;

    private String address;

    private String des;

    private String password;

    private String attachmentUrl;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE)
    private String editorId;


}
