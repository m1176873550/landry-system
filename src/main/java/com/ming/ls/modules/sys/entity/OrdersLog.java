package com.ming.ls.modules.sys.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author mj
 * @since 2020-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrdersLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * laundryId
     */
    @TableField(value = "laundry_id", fill = FieldFill.INSERT)
    private String laundryId;
    /**
     * 结账金额
     */
    private BigDecimal amount;

    /**
     * 备注
     */
    private String des;

    /**
     * 结账时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss" ,timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 结账人ID
     */
    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;


}
