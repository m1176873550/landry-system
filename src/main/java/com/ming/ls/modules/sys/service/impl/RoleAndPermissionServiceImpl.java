package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.entity.Employee;
import com.ming.ls.modules.sys.entity.EmployeeAndRole;
import com.ming.ls.modules.sys.entity.Permission;
import com.ming.ls.modules.sys.entity.RoleAndPermission;
import com.ming.ls.modules.sys.dao.RoleAndPermissionDao;
import com.ming.ls.modules.sys.service.IEmployeeAndRoleService;
import com.ming.ls.modules.sys.service.IEmployeeService;
import com.ming.ls.modules.sys.service.IPermissionService;
import com.ming.ls.modules.sys.service.IRoleAndPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.permission.PerNameAndUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Service
@Transactional(timeout = 30)
public class RoleAndPermissionServiceImpl extends ServiceImpl<RoleAndPermissionDao, RoleAndPermission> implements IRoleAndPermissionService {
    @Autowired
    private IRoleAndPermissionService iRoleAndPermissionService;
    @Autowired
    private RoleAndPermissionDao roleAndPermissionDao;
    @Autowired
    private IPermissionService iPermissionService;
    @Autowired
    private IEmployeeAndRoleService iEmployeeAndRoleService;
    @Autowired
    private IEmployeeService iEmployeeService;


    /**
     * 批量新增插入
     *
     * @param permissionIds
     * @param roleId
     * @return
     */
    @Override
    public boolean addList(List<String> permissionIds, String roleId) {
        List<RoleAndPermission> list = new LinkedList<>();
        //索引权限ID集合
        if (permissionIds.size() > 0) {
            Collection<Permission> permissions = iPermissionService.listByIds(permissionIds);
            //集合不为空，且传过来的列表不为空，查询到的列表条数与传入列表相等
            //通过条件，操作数据库
            if (permissions != null && permissionIds.size() > 0) {
                //循环对列表赋值
                for (String permissionId : permissionIds) {
                    list.add(
                            new RoleAndPermission().setRoleId(roleId).setPermissionId(permissionId)
                    );
                }
            }
        }

        //删除旧列表

        //索引权限角色集合
        //角色权限表IdWrapper对象
        LambdaQueryWrapper<RoleAndPermission> idWrapper = new LambdaQueryWrapper<RoleAndPermission>()
                .select(RoleAndPermission::getId)
                .eq(RoleAndPermission::getRoleId, roleId);
        //查询结果
        List<RoleAndPermission> roleAndPermissions = roleAndPermissionDao.selectList(idWrapper);
        if (roleAndPermissions != null && roleAndPermissions.size() > 0) {
            //删除的ID列表
            List<String> papIds = new LinkedList<>();
            //删除列表赋值
            for (RoleAndPermission pap : roleAndPermissions) {
                papIds.add(pap.getId());
            }
            //存在旧表数据，删除这些数据
            if (papIds.size() > 0) {
                if (!iRoleAndPermissionService.removeByIds(papIds)) {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "批量修改角色权限删除老版角色权限失败，数据库异常");
                }
            }
            //不存在，直接插入修改的列表
        }
        //插入修改的列表
        if (list.size() > 0) {
            if (!iRoleAndPermissionService.saveOrUpdateBatch(list)) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "批量插入角色权限失败，数据库异常");
            }
        }
        return true;
    }

    /**
     * 通过角色ID查询权限ID集合
     *
     * @param roleId
     * @return
     */
    @Override
    public List<String> getPerIdsByRoleId(String roleId) {
        LambdaQueryWrapper<RoleAndPermission> roleIdWrapper = new LambdaQueryWrapper<RoleAndPermission>()
                .select(RoleAndPermission::getPermissionId)
                .eq(RoleAndPermission::getRoleId, roleId);
        //查询结果
        List<RoleAndPermission> roleAndPermissions = iRoleAndPermissionService.list(roleIdWrapper);
        //返回对象
        List<String> perIds = new LinkedList<>();
        //判断是否有查询结果,有结果将结果perId赋值到返回对象
        if (roleAndPermissions != null && roleAndPermissions.size() > 0) {
            for (RoleAndPermission roleAndPer : roleAndPermissions) {
                perIds.add(roleAndPer.getPermissionId());
            }
            return perIds;
        } else {
            //没有直接返回空
            return null;
        }
    }

    /**
     * 检查角色ID
     *
     * @param roleId
     * @return
     */
    @Override
    public void delByRoleId(String roleId) {
        //索引权限角色集合
        //角色权限表IdWrapper对象
        LambdaQueryWrapper<RoleAndPermission> idWrapper = new LambdaQueryWrapper<RoleAndPermission>()
                .select(RoleAndPermission::getId)
                .eq(RoleAndPermission::getRoleId, roleId);
        //查询结果
        List<RoleAndPermission> roleAndPermissions = roleAndPermissionDao.selectList(idWrapper);
        //删除的ID列表
        List<String> papIds = new LinkedList<>();
        //删除列表赋值
        for (RoleAndPermission pap : roleAndPermissions) {
            papIds.add(pap.getId());
        }
        //存在，删除这些
        if (papIds.size() > 0) {
            if (!iRoleAndPermissionService.removeByIds(papIds)) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "批量修改角色权限删除老版角色权限失败，数据库异常");
            }
        }
    }

    /**
     * 获取权限名称，url，ID列表,
     *
     * @return
     */
    @Override
    public List<PerNameAndUrl> getNameAndUrl() {
        String employeeId = SessionUtil.getCurrentUser().getId();
        //查询职员的角色ID
        List<EmployeeAndRole> roleIdsObj = iEmployeeAndRoleService.list(new LambdaQueryWrapper<EmployeeAndRole>()
                .select(EmployeeAndRole::getRoleId)
                .eq(EmployeeAndRole::getEmployeeId, employeeId));
        if (roleIdsObj != null && roleIdsObj.size() > 0) {
            List<String> roleIds = new LinkedList<>();
            for (EmployeeAndRole ear : roleIdsObj) {
                roleIds.add(ear.getRoleId());
            }
            return roleAndPermissionDao.getNameAndUrl(roleIds);
        } else {
            return null;
        }
    }

    /**
     * 当前用户权限URL
     *
     * @return
     */
    @Override
    public List<String> getPermissionUrl() {
        String employeeId = SessionUtil.getCurrentUser().getId();
        List<String> roleIds = new LinkedList<>();
        //通过数据库查询是否管理员
        if (!iEmployeeService.checkIsAdmin(employeeId)) {
            roleIds.addAll(iEmployeeAndRoleService.getRoleIdByEmpId(employeeId));
            //非管理员，但没有角色
            if (roleIds.size() == 0) {
                return null;
                //对应查询
            } else {
                return roleAndPermissionDao.getPermissionUrl(roleIds);
            }
            //管理员直接查全部
        } else {
            return roleAndPermissionDao.getPermissionUrl(roleIds);
        }
    }

    /**
     * 当前权限的路径的编码列表
     *
     * @return
     */
    @Override
    public List<String> getPermissionCode(String employeeId) {
        List<String> roleIds = new LinkedList<>();
        //通过数据库查询是否管理员
        if (!iEmployeeService.checkIsAdmin(employeeId)) {
            roleIds.addAll(iEmployeeAndRoleService.getRoleIdByEmpId(employeeId));
            //非管理员，但没有角色
            if (roleIds.size() == 0) {
                return null;
                //对应查询
            } else {
                return roleAndPermissionDao.getPermissionCode(roleIds);
            }
            //管理员直接查全部
        } else {
            return roleAndPermissionDao.getPermissionCode(roleIds);
        }
    }

    /**
     * 当前权限的路径列表
     *
     * @return
     */
    @Override
    public List<String> getPermissionButton() {
        String employeeId = SessionUtil.getCurrentUser().getId();
        List<String> roleIds = new LinkedList<>();
        //通过数据库查询是否管理员
        if (!iEmployeeService.checkIsAdmin(employeeId)) {
            roleIds.addAll(iEmployeeAndRoleService.getRoleIdByEmpId(employeeId));
            //非管理员，但没有角色
            if (roleIds.size() == 0) {
                return null;
                //对应查询
            } else {
                return roleAndPermissionDao.getPermissionButton(roleIds);
            }
            //管理员直接查全部
        } else {
            return roleAndPermissionDao.getPermissionButton(roleIds);
        }
    }

    /**
     * 通过编码查按钮权限
     *
     * @param code
     * @return
     */
    @Override
    public boolean getPermissionButtonByCode(String code) {
        List<String> permissionButtons = getPermissionButton();
        return permissionButtons.contains(code);
    }
}
