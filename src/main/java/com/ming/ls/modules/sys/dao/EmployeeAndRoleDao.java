package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.entity.EmployeeAndRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
@Mapper
public interface EmployeeAndRoleDao extends BaseMapper<EmployeeAndRole> {

    List<String> getRoleIdByEmpId(@Param("employeeId") String employeeId);
}
