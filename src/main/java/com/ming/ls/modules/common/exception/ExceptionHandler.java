package com.ming.ls.modules.common.exception;

import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.service.IEmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {



    /**
     * 普通异常拦截器
     * @param e 异常
     * @return ServerResponse
     */
//    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
//    public ServerResponse handleException(Exception e){
//        log.error("Exception: ", e);
//        if (e instanceof ActivitiException){
//            String[] split = e.getMessage().split(":");
//            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), split[2]);
//        }
//        return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), "服务器异常, 请联系管理员.");
//    }

    /**
     * 参数异常拦截器
     * @param e LsArgumentException
     * @return ServerResponse
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(LsArgumentException.class)
    public ServerResponse handleLsArgumentException(LsArgumentException e) {
        log.error("LsArgumentException: ", e);
        return ServerResponse.createByErrorCodeData(e.getCode(), e.getErrors());
    }

    /**
     * 业务异常拦截器
     * @param e LsServiceException
     * @return ServerResponse
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(LsServiceException.class)
    public ServerResponse handleLsServiceException(LsServiceException e) {
        log.error("LsServiceException: ", e);
        e.setCode(ResponseCode.ERROR.getCode());
        return ServerResponse.createByErrorCodeMessage(e.getCode(), e.getMessage());
    }

    /**
     * 登录异常拦截器
     * @param e OaLoginException
     * @return ServerResponse
     */
//    @org.springframework.web.bind.annotation.ExceptionHandler(OaLoginException.class)
//    public ServerResponse handleLsArgumentException(OaLoginException e) {
//        log.error("OaLoginException: ", e);
//        return ServerResponse.createByErrorCodeMessage(e.getCode(), e.getMessage());
//    }

    /**
     * 异常拦截器
     * @param e OaException
     * @return ServerResponse
     */
//    @org.springframework.web.bind.annotation.ExceptionHandler(OaException.class)
//    public ServerResponse handleLsArgumentException(OaException e) {
//        log.error("OaException: ", e);
//        return ServerResponse.createByErrorMessage(e.getMsg());
//    }

    /**
     * 权限异常拦截器
     * @param e OaAuthorizedException
     * @return ServerResponse
     */
//    @org.springframework.web.bind.annotation.ExceptionHandler(OaAuthorizedException.class)
//    public ServerResponse handleLsArgumentException(OaAuthorizedException e, HttpServletRequest request, HttpServletResponse response) {
//
//        log.error("OaAuthorizedException: ", e);
//
//        // 退出登录
//        userService.layout(SessionUtil.getSession(), request, response);
//
//        return ServerResponse.createByErrorCodeMessage(e.getCode(), e.getMessage());
//    }
}
