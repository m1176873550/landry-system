package com.ming.ls.modules.sys.dao;

import com.ming.ls.modules.sys.entity.RoleAndPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.vo.permission.PerNameAndUrl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Mapper

public interface RoleAndPermissionDao extends BaseMapper<RoleAndPermission> {
    /**
     * 获取权限名称，url，ID列表,
     *
     * @return
     */
    List<PerNameAndUrl> getNameAndUrl(@Param("roleIds") List<String> roleIds);

    /**
     * 通过角色ID的查询权限ID
     * @param roleId
     * @return
     */
    String getPerIdByRoleId(@Param("roleId") String roleId);

    /**
     * 当前用户权限URL
     * @param roleIds
     * @return
     */
    List<String> getPermissionUrl(@Param("roleIds") List<String> roleIds);

    /**
     * 当前权限的路径的编码列表
     * @return
     */
    List<String> getPermissionCode(@Param("roleIds") List<String> roleIds);
    /**
     * 当前用户权限按钮
     * @param roleIds
     * @return
     */
    List<String> getPermissionButton(@Param("roleIds") List<String> roleIds);


}
