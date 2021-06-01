package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.permission.IdNameChildren;
import com.ming.ls.modules.sys.vo.permission.PerNameAndId;
import com.ming.ls.modules.sys.vo.permission.PerNameAndUrl;
import com.ming.ls.modules.sys.vo.permission.PermissionListVo;
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
public interface PermissionDao extends BaseMapper<Permission> {
    /**
     * 列表
     * @param page
     * @param query
     * @return
     */
    IPage<PermissionListVo> list(IPage<PermissionListVo> page, @Param("query") BaseQuery query);

    /**
     * 查询全集
     * @param page
     * @param query
     * @return
     */
    IPage<PermissionListVo> listAll(IPage<PermissionListVo> page, @Param("query") BaseQuery query);

    /**
     * 子集列表
     * @return
     */
    List<PermissionListVo> listChildren(@Param("query") BaseQuery query);


    /**
     * 返回父级名字和列表
     * @return
     */
    List<PerNameAndId> parentNameAndId(@Param("id")String id);

    List<PerNameAndUrl> getNameAndUrl();

    List<IdNameChildren> groupIdAndName();
}
