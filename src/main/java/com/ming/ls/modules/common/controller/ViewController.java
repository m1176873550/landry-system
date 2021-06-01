package com.ming.ls.modules.common.controller;

import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.service.IRoleAndPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 统一视图控制器
 */
@Controller
@RequestMapping("/")
public class ViewController {

    /**
     * 权限验证业务类
     */
    @Autowired
    private IRoleAndPermissionService iRoleAndPermissionService;

    @RequestMapping("view/{folder}/{page}.htm")
    public String view(@PathVariable("folder") String folder, @PathVariable("page") String page, HttpServletRequest request) {
        //登陆页面
//        if (folder.equals("login") && page.equals("login")) {
//            return "sys/" + folder + "/" + page;
//            //访问主页,拥有权限才能通过
//        } else if ((folder.equals("home") && page.equals("home"))) {
//            //登陆了，进入对应页面
//            if(SessionUtil.getCurrentUser2()!=null){
//                return "sys/" + folder + "/" + page;
//                //未登录，去登陆界面
//            }else {
//                return "sys/login/login";
//            }
//        } else {
//            //登陆了，进入对应页面
//            if(SessionUtil.getCurrentUser2()!=null){
//                List<String> permissionUrl = iRoleAndPermissionService.getPermissionUrl();
//                if (permissionUrl.contains(request.getRequestURI())){
//                    return "sys/" + folder + "/" + page;
//                }else {
//                    return "sys/404/404";
//                }
//                //未登录，去登陆界面
//            }else {
//                return "sys/login/login";
//            }
//        }
        return null;
    }

    @RequestMapping("view/{page}.htm")
    public String view(@PathVariable("page") String page) {
        return "sys/" + page;
    }

    @RequestMapping("/")
    public String view(){
        return "sys/login/login";
    }


}

