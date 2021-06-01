package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.role.RoleIdAndName;
import com.ming.ls.modules.sys.vo.role.RoleListVo;
import com.ming.ls.modules.sys.vo.role.RoleVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IRoleService extends IService<Role> {
    /**
     * 列表
     *
     * @param query
     * @return
     */
    IPage<RoleListVo> list(BaseQuery query);

    /**
     * 新增
     *
     * @param roleVo
     * @return
     */
    boolean add(RoleVo roleVo);

    /**
     * 编辑
     *
     * @param roleVo
     * @return
     */
    boolean edit(RoleVo roleVo);

    /**
     * 删除
     *
     * @param id
     */
    void del(String id);

    /**
     * 权限多级选择框
     *
     * @return
     */
    List<RoleIdAndName> getRoleIdAndName();

    /**
     * 检查ID
     *
     * @param id
     */
    void checkId(String id);

    /**
     * 通过名字查询
     *
     * @param name
     * @return
     */
    Role getByName(String name);

    /**
     * 检查名称是否存在
     *
     * @param name,id
     */
    void checkName(String id, String name);

}
