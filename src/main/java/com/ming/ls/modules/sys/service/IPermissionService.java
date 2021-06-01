package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.permission.PermQuery;
import com.ming.ls.modules.sys.vo.permission.*;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IPermissionService extends IService<Permission> {
    /**
     * 列表
     * @param queue
     * @return
     */
    IPage<PermissionListVo> list(PermQuery queue);

    /**
     * 最上级名称和ID
     * @param id
     * @return
     */
    List<PerNameAndId> parentNameAndId(String id);

    boolean editPermission(PermissionListVo pv);

    boolean addPermission(PermissionListVo pv);

    /**
     * ID获取权限
     * @param id
     * @return
     */
    Permission getPermission(String id);

    /**
     * 改状态
     * @param perIdAndStatus
     * @return
     */
    boolean changeStatus(PerIdAndStatus perIdAndStatus);

    /**
     * 名称ID URL的集合
     * @return
     */
    List<PerNameAndUrl> getNameAndUrl();

    /**
     * 按权限分组
     * @return
     */
    List<IdNameChildren> group();

    String getIdByUrl(String url);
    String getUrlById(String url);

}
