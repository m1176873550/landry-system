package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.PermissionDao;
import com.ming.ls.modules.sys.entity.Permission;
import com.ming.ls.modules.sys.query.permission.PermQuery;
import com.ming.ls.modules.sys.service.IDictionaryService;
import com.ming.ls.modules.sys.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.service.IRoleAndPermissionService;
import com.ming.ls.modules.sys.vo.permission.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
public class PermissionServiceImpl extends ServiceImpl<PermissionDao, Permission> implements IPermissionService {

    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private IRoleAndPermissionService iRoleAndPermissionService;
    @Autowired
    private IDictionaryService iDictionaryService;
    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public IPage<PermissionListVo> list(PermQuery query) {
        //分页查询对象
        IPage<PermissionListVo> iPage = new Page<>(query.getCurrentPage(), query.getSize());
        //有父级的对象列表
        IPage<PermissionListVo> pageParent = permissionDao.list(iPage, query);
        //若是查询，直接返回，不分级
        if (StringUtils.isNotBlank(query.getKey())) {
            return permissionDao.listAll(iPage, query);
        }
        //父级列表
        List<PermissionListVo> parents = pageParent.getRecords();
        //查询子集
        query.setHasParent(Const.HAS);
        List<PermissionListVo> children = permissionDao.listChildren(query);
        //将子集赋值到父级
        parents.stream().forEach(e->setChild(children,e));
        //分页对象赋值，返回
        return pageParent.setRecords(parents);
    }


    private void setChild(List<PermissionListVo> list, PermissionListVo e) {
        List<PermissionListVo> collect = list.stream()
                .filter(p -> p.getParentId().equals(e.getId()))
                .collect(toList());
        e.setChildren(collect);
    }
    /**
     * 查询属于父级的名字和ID
     *
     * @return
     */
    @Override
    public List<PerNameAndId> parentNameAndId(String id) {
        return permissionDao.parentNameAndId(id);
    }

    /**
     * 编辑
     *
     * @param pv
     * @return
     */
    @Override
    public boolean editPermission(PermissionListVo pv) {
        if (permissionService.getPermission(pv.getId()) != null) {
            Permission permission = new Permission();
            permission.setId(pv.getId())
                    .setCode(pv.getCode())
                    .setDes(pv.getDes())
                    .setLevel(pv.getLevel())
                    .setName(pv.getName())
                    .setStatus(pv.getStatus())
                    .setParentId(pv.getParentId())
                    .setType(pv.getType())
                    .setUrl(pv.getUrl());
            if (permissionService.updateById(permission)) {
                return true;
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "编辑保存失败，数据库异常");
            }
        }
        throw new LsServiceException(ResponseCode.ERROR.getCode(), "权限ID异常");

    }

    /**
     * 新增
     *
     * @param pv
     * @return
     */
    @Override
    public boolean addPermission(PermissionListVo pv) {
        Permission permission = new Permission();
        permission.setCode(pv.getCode())
                .setDes(pv.getDes())
                .setLevel(pv.getLevel())
                .setName(pv.getName())
                .setStatus(pv.getStatus())
                .setParentId(pv.getParentId())
                .setType(pv.getType())
                .setUrl(pv.getUrl());
        if (permissionService.save(permission)) {
            return true;
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "新增失败，数据库异常");
        }
    }

    /**
     * 获得对象
     *
     * @return
     */
    @Override
    public Permission getPermission(String id) {
        return permissionService.getById(id);
    }

    /**
     * 修改启用禁用状态
     *
     * @param
     * @return
     */
    @Override
    public boolean changeStatus(PerIdAndStatus per) {
        //检查ID
        Permission checkIdPer = permissionService.getPermission(per.getId());
        if (checkIdPer != null) {
            if (StringUtils.isNotEmpty(checkIdPer.getParentId())) {
                //检查是否有父集
                LambdaQueryWrapper<Permission> idWrapper = new LambdaQueryWrapper<Permission>()
                        .select(Permission::getId)
                        .eq(Permission::getId, checkIdPer.getParentId())
                        .eq(Permission::getStatus, Const.OFF);
                List<Permission> parents = permissionDao.selectList(idWrapper);
                if (parents.size() > 0) {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前权限上级已禁用，请修改上级后再试");
                }
            }
            //检查是否有子集
            LambdaQueryWrapper<Permission> idWrapper = new LambdaQueryWrapper<Permission>()
                    .select(Permission::getId)
                    .eq(Permission::getParentId, per.getId())
                    .eq(Permission::getStatus, Const.ON);
            List<Permission> children = permissionDao.selectList(idWrapper);
            if (children.size() > 0) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前权限下级已启用，请修改下级后再试");
            }
            //更新对象
            Permission permission = new Permission();
            //判断时启用还是禁用
            if (per.getStatus().equals(Const.OFF)) {
                permission.setStatus(Const.ON);
            } else if (per.getStatus().equals(Const.ON)) {
                permission.setStatus(Const.OFF);
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "权限状态异常");
            }
            permission.setId(per.getId());
            if (permissionService.updateById(permission)) {
                return true;
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "编辑保存失败，数据库异常");
            }
        }
        throw new LsServiceException(ResponseCode.ERROR.getCode(), "权限ID异常");
    }

    /**
     * 获取权限名称，url，ID列表,
     *
     * @return
     */
    @Override
    public List<PerNameAndUrl> getNameAndUrl() {
        return iRoleAndPermissionService.getNameAndUrl();
    }

    /**
     * 按权限分组
     *
     * @return
     */
    @Override
    public List<IdNameChildren> group() {
        List<IdNameChildren> nameAndUrls = permissionDao.groupIdAndName();
        //找子集
        for (IdNameChildren nameAndUrlParent : nameAndUrls) {
            //只有路径才进入筛选
            if (StringUtils.equals(nameAndUrlParent.getType(), Const.LIST)) {
                //子集对象
                List<IdNameChildren> children = new LinkedList<>();
                for (IdNameChildren nameAndUrlChild : nameAndUrls) {
                    //id和父ID相等，插入当前对象的children
                    if (StringUtils.equals(nameAndUrlParent.getId(), nameAndUrlChild.getParentId())) {
                        children.add(nameAndUrlChild);
                    }
                }
                nameAndUrlParent.setChildren(children);
            }
        }
        //字典父value
        Integer permissionParentVal = iDictionaryService.findValueByName(Const.PERMISSION_PARENT);
        //第一层级的数量
        int countParent = permissionService.count(new LambdaQueryWrapper<Permission>().eq(Permission::getType, permissionParentVal).eq(Permission::getStatus,1));
        //将不是路径的按钮排除第一层级,动态取值，剩下的刚好是需要的顶级tree结构结果
        for (int start=0;start<countParent;) {
            if (!StringUtils.equals(nameAndUrls.get(start).getType(), Const.LIST)) {
                nameAndUrls.remove(nameAndUrls.get(start));
            }else {
                start++;
            }
        }
        return nameAndUrls;
    }

    @Override
    public String getIdByUrl(String url) {
        Permission permission = permissionService.getOne(new LambdaQueryWrapper<Permission>().eq(Permission::getUrl, url));
        if (permission !=null){
            return permission.getId();
        }else {
            throw new LsServiceException("路径异常");
        }
    }

    @Override
    public String getUrlById(String id) {
        Permission permission = permissionService.getOne(new LambdaQueryWrapper<Permission>().eq(Permission::getId, id));
        if (permission !=null){
            return permission.getUrl();
        }else {
            throw new LsServiceException("ID异常");
        }
    }


}
