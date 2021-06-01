package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.role.RoleIdAndName;
import com.ming.ls.modules.sys.vo.role.RoleListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Mapper

public interface RoleDao extends BaseMapper<Role> {
    /**
     * 列表
     * @param page
     * @param query
     * @return
     */
    IPage<RoleListVo> list(IPage<RoleListVo> page, @Param("query") BaseQuery query);

    /**
     * 角色ID和名字
     * @param laundryId
     * @return
     */
    List<RoleIdAndName> getRoleIdAndName(@Param("laundryId") String laundryId);


}
