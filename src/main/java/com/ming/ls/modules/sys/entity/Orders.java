package com.ming.ls.modules.sys.entity;

import java.math.BigDecimal;
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
 * @since 2020-01-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
public class Orders extends Model<Orders> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private Integer id;
    @TableField(value = "is_remind")
    private Boolean remind;
    /**
     * 洗衣店ID
     */
    @TableField(value = "laundry_id", fill = FieldFill.INSERT)
    private String laundryId;
    private Integer isPay;
    private Integer isFinished;
    private BigDecimal totalPrice;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    private String period;
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE)
    private String editorId;


}
