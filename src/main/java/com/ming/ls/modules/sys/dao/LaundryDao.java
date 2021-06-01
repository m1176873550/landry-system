package com.ming.ls.modules.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ming.ls.modules.sys.entity.Laundry;
import com.ming.ls.modules.sys.vo.Laundry.IdAndNameList;
import org.apache.ibatis.annotations.Mapper;

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

public interface LaundryDao extends BaseMapper<Laundry> {
    /**
     * 店铺名和ID
     * @return
     */
    List<IdAndNameList> laundryIdAndNameList();
}
