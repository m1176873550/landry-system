package com.ming.ls.modules.common.util;

import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.sys.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * session 操作工具类
 *
 * @PackgeName: com.gdcccn.oa.modules.common.utils
 * @Author: nza
 * Date: 2019/10/14 15:16
 */
@Slf4j
public class SessionUtil {

    /**
     * 设置当前登录用户
     *
     * @param request 请求
     */
    public static void setCurrentUser(HttpServletRequest request, Employee employee) {
        request.getSession().setAttribute(Const.CURRENT_USER, employee);
        request.getSession().setMaxInactiveInterval(-1);
    }

    /**
     * 设置当前登录用户
     */
    public static void removeSession(HttpServletRequest request, String key) {
        request.getSession().removeAttribute(key);
    }

    /**
     * 获取当前登录用户
     *
     * @return User
     */
    public static Employee getCurrentUser() {
        Employee user = (Employee) getSession().getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new LsServiceException(ResponseCode.NEED_LOGIN.getCode(), "用户未登录， 请先登录");
        }
        return user;
    }

    public static Employee getCurrentUser2() {
        Employee user = (Employee) getSession().getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return null;
        }
        return user;
    }

    /**
     * 设置当前登录用户
     *
     * @param user user
     */
    public static void setCurrentUser(Employee user) {
        getSession().setAttribute(Const.CURRENT_USER, user);
    }

    /**
     * 设置当前登录用户
     */
    public static void removeSession(String key) {
        getSession().removeAttribute(key);
    }

    /**
     * 获取当前 session
     *
     * @return HttpSession
     */
    public static HttpSession getSession() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();
    }
}
