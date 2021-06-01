package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.service.IRoleAndPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/roleAndPermission")
public class RoleAndPermissionController {

    @Autowired
    private IRoleAndPermissionService iRoleAndPermissionService;

    /**
     * 通过角色ID查询权限ID集合
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ServerResponse roleIdToPerIds(@PathVariable(value = "id") String id){
        List<String> ids = iRoleAndPermissionService.getPerIdsByRoleId(id);
        return ServerResponse.createBySuccess(ids);
    }

    /**
     * 权限URL集合
     * @return
     */
    @GetMapping("getPermissionUrl")
    public ServerResponse getPermissionUrl(){
        List<String> urls = iRoleAndPermissionService.getPermissionUrl();
        return ServerResponse.createBySuccess(urls);
    }

    /**
     * 权限按钮集合
     * @return
     */
    @GetMapping("/getPermissionButton")
    public ServerResponse getPermissionButton(){
        List<String> buttons = iRoleAndPermissionService.getPermissionButton();
        return ServerResponse.createBySuccess(buttons);
    }

    /**
     * 权限按钮集合
     * @return
     */
    @GetMapping("/button-by-code/{code}")
    public ServerResponse getPermissionButtonByCode(@PathVariable String code){
        boolean isShow = iRoleAndPermissionService.getPermissionButtonByCode(code);
        return ServerResponse.createBySuccess(isShow);
    }
}
