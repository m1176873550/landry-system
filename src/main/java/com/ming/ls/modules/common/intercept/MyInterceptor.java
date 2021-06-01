package com.ming.ls.modules.common.intercept;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ming.ls.modules.common.exception.LsArgumentException;
import com.ming.ls.modules.common.exception.LsBaseException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.service.IEmployeeService;
import com.ming.ls.modules.sys.service.impl.EmployeeServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

//@Component
public class MyInterceptor implements HandlerInterceptor {
    Logger logger = LoggerFactory.getLogger("interceptor");

    /**
     * 返回true则放行，返回false则将其拦截住
     * @param req
     * @param resp
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String name = handlerMethod.getMethod().getName();
            //已登录，或未登录
            if (SessionUtil.getCurrentUser2() != null || "login".equals(name) ||
                    "view".equals(name)||"payController".equals(name)||"doForgetPwd".equals(name)) {
                return true;
            } else {
                throw new LsBaseException(ResponseCode.ERROR.getCode(), "路径访问异常");
            }
        } else {
            throw new LsBaseException(ResponseCode.ERROR.getCode(), "路径访问异常");
        }
    }
}
