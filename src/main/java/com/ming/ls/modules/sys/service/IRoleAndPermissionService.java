package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.RoleAndPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.vo.permission.PerNameAndUrl;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IRoleAndPermissionService extends IService<RoleAndPermission> {
    /**
     * 批量新增插入
     * @param permissionId
     * @param roleId
     * @return
     */
    boolean addList(List<String> permissionId, String roleId);

    /**
     * 通过角色ID查询权限ID集合
     * @param id
     * @return
     */
    List<String> getPerIdsByRoleId(String id);

    /**
     * 批量删除通过角色ID
     * @param roleId
     */
    void delByRoleId(String roleId);
    /**
     * 获取权限名称，url，ID列表,
     *
     * @return
     */
    List<PerNameAndUrl> getNameAndUrl();

    /**
     * 当前用户权限URL
     * @return
     */
    List<String> getPermissionUrl();

    /**
     * 当前权限的路径的编码列表
     * @return
     */
    List<String> getPermissionCode(String employeeId);

    /**
     * 当前权限的路径列表
     * @return
     */
    List<String> getPermissionButton();
    /**
     * 通过编码查按钮权限
     * @param code
     * @return
     */
    boolean getPermissionButtonByCode(String code);
}
