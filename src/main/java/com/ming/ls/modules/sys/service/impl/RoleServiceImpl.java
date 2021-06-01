package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.RoleDao;
import com.ming.ls.modules.sys.entity.Role;
import com.ming.ls.modules.sys.entity.RoleAndPermission;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IEmployeeAndRoleService;
import com.ming.ls.modules.sys.service.IRoleAndPermissionService;
import com.ming.ls.modules.sys.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.role.RoleIdAndName;
import com.ming.ls.modules.sys.vo.role.RoleListVo;
import com.ming.ls.modules.sys.vo.role.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements IRoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IRoleService iRoleService;
    @Autowired
    private IRoleAndPermissionService iRoleAndPermissionService;
    @Autowired
    private IEmployeeAndRoleService iEmployeeAndRoleService;
    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public IPage<RoleListVo> list(BaseQuery query) {
        IPage<RoleListVo> page = new Page<>(2,2);
        return roleDao.list(page, query);
    }

    /**
     * 新增
     *
     * @param roleVo
     * @return
     */
    @Override
    public boolean add(RoleVo roleVo) {
        //检查是否未修改名称，且名称不重复
        iRoleService.checkName(roleVo.getId(), roleVo.getName());
        //通过上一行检查，执行编辑
        Role role = new Role();
        role.setName(roleVo.getName()).setDes(roleVo.getDes()).setStatus(roleVo.getStatus());
        if (iRoleService.save(role)) {
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "编辑角色失败,数据库异常");
        }
    }

    /**
     * 编辑
     *
     * @param roleVo
     * @return
     */
    @Override
    public boolean edit(RoleVo roleVo) {
        //检查是否未修改名称，且名称不重复
        iRoleService.checkName(roleVo.getId(), roleVo.getName());
        //通过上一行检查，执行编辑
        Role role = new Role();
        role.setName(roleVo.getName()).setDes(roleVo.getDes()).setId(roleVo.getId()).setStatus(roleVo.getStatus());
        if (iRoleService.updateById(role)
                && iRoleAndPermissionService.addList(roleVo.getPermissionIds(), roleVo.getId())) {
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "编辑角色失败,数据库异常");
        }
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void del(String id) {
        iRoleService.checkId(id);
        //删除角色绑定的权限
        if (iEmployeeAndRoleService.isExistEmployee(id)){
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "删除角色失败,存在绑定相关职员");
        }
        iRoleAndPermissionService.delByRoleId(id);
        if (!iRoleService.removeById(id)) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "删除角色失败,数据库异常");
        }
    }



    /**
     * 返回ID和名称列表
     *
     * @return
     */
    @Override
    public List<RoleIdAndName> getRoleIdAndName() {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        return roleDao.getRoleIdAndName(laundryId);
    }

    /**
     * 检查ID
     *
     * @param id
     */
    @Override
    public void checkId(String id) {
        Role role = iRoleService.getById(id);
        if (role == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "角色ID异常");
        }
    }

    /**
     * 通过名称查询对象
     *
     * @param name
     * @return
     */
    @Override
    public Role getByName(String name) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<Role> nameWrapper = new LambdaQueryWrapper<Role>()
                .eq(Role::getLaundryId, laundryId)
                .eq(Role::getName, name);
        return iRoleService.getOne(nameWrapper);
    }

    /**
     * 检查名称是否存在且不重复，
     *
     * @param name,id
     */
    @Override
    public void checkName(String id, String name) {
        //通过名称查询的对象
        Role role = iRoleService.getByName(name);
        if (StringUtils.isNotEmpty(id)) {
            //检查ID
            checkId(id);
            //通过ID查询的对象
            Role roleById = iRoleService.getById(id);
            //名称相同,且通过名称查询的ID与当前ID不相同
            if (StringUtils.equals(roleById.getName(), name)
                    && !StringUtils.equals(role.getId(), id)) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "角色名称已存在");
            }
        } else {
            //ID为空且名称存在
            if (role != null && StringUtils.equals(role.getName(), name)) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "角色名称已存在");
            }
        }
    }
}
