package com.ming.ls.modules.common.util;

/**
 * 响应类型
 */
public enum ResponseCode {
    SUCCESS(1, "SUCCESS"),                      // 成功
    ERROR(0, "ERROR"),                          // 错误
    NEED_LOGIN(10, "NEED_LOGIN"),               // 需要登录
    ILLEGAL_ARGUMENT(2, "ILLEGAL_ARGUMENT"),    // 非法参数
    LOGIN_ERROR(11, "LOGIN_ERROR");

    private final int code;     // 状态码
    private final String desc;  // 描述

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
