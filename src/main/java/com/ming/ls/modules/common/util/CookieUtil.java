package com.ming.ls.modules.common.util;

import cn.hutool.json.JSONUtil;
import com.ming.ls.modules.sys.entity.Employee;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class CookieUtil {

    private final static String COOKIE_DOMAIN = "localhost";  // Tomcat 8.5 以上的不要加点
    private final static String COOKIE_NAME = "userName";

    /**
     * 写入 cookie
     * @param response HttpServletResponse
     * @param token cookie 的值
     */
    public static void writeLoginToken(HttpServletResponse response, String token, int time)
    {
        String encodeCookie="";
        try {
            encodeCookie = URLEncoder.encode(token,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Cookie ck = new Cookie(COOKIE_NAME, encodeCookie);
        ck.setDomain(COOKIE_DOMAIN);
        ck.setPath("/");                         // 代表设置在根目录
        ck.setHttpOnly(true);                    // 不许通过脚本读取
        ck.setMaxAge(time);                      // 单位:s  -1 永久有效 0 删除cookie, 如果不赋值,则不会写入硬盘, 只在当前页面有效

        response.addCookie(ck);
    }

    /**
     * 读取 cookie
     * @param request HttpServletRequest
     * @return String
     */
    public static String readLoginToken(HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cks = request.getCookies();
        if (cks != null) {
            for (Cookie ck : cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    String result= URLDecoder.decode(ck.getValue() , "utf-8");
                    Employee user = JSONUtil.toBean(result, Employee.class);
//                    if (user.getTime()==null||user.getTime().isBefore(LocalDateTime.now()))
//                        return null;
                    SessionUtil.setCurrentUser(request, user);
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * 删除 cookie
     * @param request   HttpServletRequest
     * @param response  HttpServletResponse
     */
    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cks = request.getCookies();

        if (cks != null) {
            for (Cookie ck : cks) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    ck.setDomain(COOKIE_DOMAIN);
                    ck.setPath("/");
                    ck.setMaxAge(0);       // 设置 0 为删除 cookie

                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}
