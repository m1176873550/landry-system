package com.ming.ls.modules.sys.query;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseQuery {
    private String id;
    /**
     * 关键字
     */
    private String key="";
    /**
     * 当前页
     */
    private Integer currentPage=1;
    /**
     * 数据数
     */
    private Integer size=10;
    /**
     * 排序
     */
    private String order;
    /**
     * 排序关键字
     */
    private String orderKey;
    /**
     * 是否管理员
     */
    private Integer isAdmin;
    /**
     * 店铺ID
     */
//    @TableField(value = "laundry_id", fill = FieldFill.INSERT)
    private String laundryId;
}
