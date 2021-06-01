package com.ming.ls.modules.sys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.dao.EmployeeAndRoleDao;
import com.ming.ls.modules.sys.entity.EmployeeAndRole;
import com.ming.ls.modules.sys.service.IEmployeeAndRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
@Service
public class EmployeeAndRoleServiceImpl extends ServiceImpl<EmployeeAndRoleDao, EmployeeAndRole> implements IEmployeeAndRoleService {
    @Autowired
    private IEmployeeAndRoleService iEmployeeAndRoleService;
    @Autowired
    private EmployeeAndRoleDao employeeAndRoleDao;

    /**
     * 检查是否存在这一职员绑定该权限
     *
     * @param roleId
     * @return
     */
    @Override
    public boolean isExistEmployee(String roleId) {
        LambdaQueryWrapper<EmployeeAndRole> roleIdWrapper = new LambdaQueryWrapper<EmployeeAndRole>()
                .select(EmployeeAndRole::getId)
                .eq(EmployeeAndRole::getRoleId, roleId);
        EmployeeAndRole ear = iEmployeeAndRoleService.getOne(roleIdWrapper);
        return ear != null;
    }

    /**
     * 通过职员ID查询角色ID集合
     *
     * @param employeeId
     * @return
     */
    @Override
    public List<String> getRoleIdByEmpId(String employeeId) {
        return employeeAndRoleDao.getRoleIdByEmpId(employeeId);
    }
}
