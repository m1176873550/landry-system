package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.EmployeeAndRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
public interface IEmployeeAndRoleService extends IService<EmployeeAndRole> {
    /**
     * 检查是否存在这一职员绑定该权限
     * @param roleId
     * @return
     */
    boolean isExistEmployee(String roleId);

    /**
     * 通过职员ID查询角色ID集合
     * @param employeeId
     * @return
     */
    List<String> getRoleIdByEmpId(String employeeId);
}
