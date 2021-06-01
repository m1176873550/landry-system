package com.ming.ls.modules.sys.entity;

import java.math.BigDecimal;
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
@TableName("hanging_point")
public class HangingPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 洗衣店ID
     */
    @TableField(value = "laundry_id", fill = FieldFill.INSERT)
    private String laundryId;
    /**
     * 挂点编号
     */
    private Integer number;

    private Integer orderId;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GTM+8")
    private LocalDateTime pickingTime;

    private String payerId;

    private String clothTypeId;

    private String serviceTypes;

    private String colorId;

    private String brandId;
    /**
     * 是否被取
     */
    private Integer beanTook;
    private Integer isPay;

    /**
     * 附件图片
     */
    private String annexUrl;


    /**
     * 价格
     */
    private BigDecimal price;

    private String des;
    /**
     * 是否待取
     */
    private String isFinished;

    /**
     * 是否返洗
     */
    private Integer isBack;

    /**
     * 是否退款
     */
    private BigDecimal refund;

    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableField(value = "creator_id", fill = FieldFill.INSERT)
    private String creatorId;

    @TableField(value = "editor_id", fill = FieldFill.INSERT_UPDATE)
    private String editorId;


}
