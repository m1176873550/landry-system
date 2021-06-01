package com.ming.ls.modules.sys.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("sms")
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短信ID
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 洗衣店ID
     */
    @TableField(value = "laundry_id", fill = FieldFill.INSERT)
    private String laundryId;
    /**
     * 消息模板
     */
    private String template;

    /**
     * 是否关闭
     */
    private Integer closing;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE)
    private String editorId;


}
