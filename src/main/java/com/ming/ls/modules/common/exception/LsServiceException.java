package com.ming.ls.modules.common.exception;


/**
 * 业务异常
 *
 * @PackgeName:
 * @ClassName: LsServiceException
 * Date: 2019/9/30 10:11
 */
public class LsServiceException extends LsBaseException {
    private static final long serialVersionUID = 619023828058861634L;

    public LsServiceException(Integer code, String message) {
        super(code, message);
    }

    public LsServiceException(String message) {
        super(1, message);
    }
    public LsServiceException(Integer code) {
        super(code);
    }
}
