package com.ming.ls.modules.common.exception;

/**
 * @PackgeName: com.gdcccn.oa.modules.common.exception
 * @ClassName: OaBaseException
 * @Author: mj
 * Date: 2019/9/30 10:06
 */
public class LsBaseException extends RuntimeException {

    private static final long serialVersionUID = -4504694085380942245L;

    /**
     * 状态码
     */
    protected Integer code;

    /**
     * 异常信息
     */
    protected String message;

    public LsBaseException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public LsBaseException(Integer code) {
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
