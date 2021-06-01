package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.HangingPoint;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.query.hangingPoint.HangingPointQuery;
import com.ming.ls.modules.sys.vo.hangingPoint.HangingPointVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
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

public interface HangingPointDao extends BaseMapper<HangingPoint> {
    IPage<HangingPointVo> list(IPage<HangingPointVo> page,
                               @Param("query") HangingPointQuery query);

    List<Integer> generateId(@Param("query") BaseQuery query);

    BigDecimal getIncomeByDay(LocalDate localDate);
    BigDecimal getVipIncomeByDay(LocalDate localDate);


}
