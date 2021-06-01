package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.service.IEmployeeAndRoleService;
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
 * @since 2020-01-10
 */
@RestController
@RequestMapping("/employeeAndRole")
public class EmployeeAndRoleController {
    @Autowired
    private IEmployeeAndRoleService iEmployeeAndRoleService;

    /**
     * 职员ID获取角色ID
     * @param id
     * @return
     */
    @GetMapping("/{id}/get-role-id")
    public ServerResponse getEmployeeRoleId(@PathVariable String id){
        List<String> roleIds = iEmployeeAndRoleService.getRoleIdByEmpId(id);
        return ServerResponse.createBySuccess(roleIds);
    }
}
