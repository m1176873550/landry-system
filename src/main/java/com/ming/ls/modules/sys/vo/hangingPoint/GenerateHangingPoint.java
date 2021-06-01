package com.ming.ls.modules.sys.vo.hangingPoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 *
 * @author : 明杰
 * @date : 2020/2/12 17:18
    前台发过来的新增节点对象
 * @return : null
 */
@Data
@Accessors(chain = true)
public class GenerateHangingPoint {
    private Integer orderId;
    private Integer number;
    private String clothType;
    private String clothTypeId;
    private String colorId;
    private String brandId;
    private Integer isPayed;
    private String price;
    private String color;
    private String brand;
    private String service;
    private String des;
    private String payerId;
}
