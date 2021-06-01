package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.Color;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.color.ColorVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IColorService extends IService<Color> {

    List<Color> list(BaseQuery query);

    void checkId(String id);

    boolean editColor(ColorVo colorVo);

    boolean addColor(String name);

    /**
     * 检查名字是否存在
     * @param name
     * @return
     */
    Color nameIsExist(String name);
}
