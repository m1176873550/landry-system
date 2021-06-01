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
@TableName("permission")
public class Permission implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 路径
     */
    private String url;
    private String parentId;
    private String type;
    private String code;
    /**
     * 功能名称
     */
    private String name;
    /**
     * 权限级别
     */
    private Integer level;

    private String des;
    private Integer Status;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE)
    private String editorId;


}
