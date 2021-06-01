package com.ming.ls.modules.sys.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Laundry implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 店名
     */
    private String name;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE)
    private String editorId;


}
