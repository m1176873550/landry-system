package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.modules.sys.entity.Employee;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.employee.EmployeeListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mj
 * @since 2020-01-08
 */
@Mapper

public interface EmployeeDao extends BaseMapper<Employee> {
    /**
     * 职员列表
     * @param page
     * @param query
     * @return
     */
    IPage<EmployeeListVo> list(IPage page,
                               @Param("query") BaseQuery query);
}
