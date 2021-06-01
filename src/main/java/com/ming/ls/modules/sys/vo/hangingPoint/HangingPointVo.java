package com.ming.ls.modules.sys.vo.hangingPoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class HangingPointVo {
    @NotNull(message = "挂点ID不能为空", groups = Update.class)
    private String id;
    private Integer number;
    private Integer orderId;
    /**
     * 预计取衣时间
     */
    private String pickingTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime pickingTime2;
    @NotNull(message = "付款人不能为空", groups = {Update.class,Create.class})
    private String payer;
    private String clothType;
    /**
     * 是否被取
     */
    private Integer beanTook;
    private String colorId;
    private String brandId;
    /**
     * 服务类型
     */
    private String serviceTypes;
    @NotNull(message = "衣物类型不能为空", groups = {Update.class,Create.class})
    private String clothTypeId;
    private String brand;
    private String annexUrl;
    private String des;
    /**
     * 是否返洗
     */
    private Integer isBack;
    private Integer refund;
    /**
     * 退款金额
     */
    private String refundAmount;
    private String price;
    private String editor;
    /**
     * 是否完成交易
     */
    private Integer isFinished;
    /**
     * 是否可取
     */
    private Integer pending;
    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空", groups = {Update.class,Create.class})
    private String phone;
    /**
     * 折扣
     */
    private String discount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedAt;
    /**
     * 会员ID
     */
    private String nic;
    /**
     * 会员ID
     */
    private Integer isPay;
    public static interface Create {
    }

    public static interface Update {
    }

}
