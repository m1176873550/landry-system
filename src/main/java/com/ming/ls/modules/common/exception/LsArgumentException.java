package com.ming.ls.modules.common.exception;

import com.ming.ls.modules.common.util.ResponseCode;

import java.util.Map;

/**
 * 控制器参数异常
 */
public class LsArgumentException extends LsBaseException {

    private static final long serialVersionUID = 647197721646580717L;

    /**
     * 错误信息
     * 字段 : 错误
     */
    private Map<String, String> errors;

    public LsArgumentException(Map<String, String> errors) {
        super(ResponseCode.ERROR.getCode());
        this.errors = errors;
    }

    public LsArgumentException(int code, Map<String, String> errors) {
        super(code);
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
