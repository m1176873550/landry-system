package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.brand.BrandVo;

import java.util.List;

/**
 * <p>
 *  品牌类型业务层
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IBrandService extends IService<Brand> {
    /**
     * 列表
     * @return
     */
    List<Brand> list(BaseQuery query);

    /**
     * 编辑
     * @param brandVo
     * @return
     */
    void editBrand(BrandVo brandVo);

    /**
     * 新增
     * @param brandVo
     * @return
     */
    void addBrand(BrandVo brandVo);

    /**
     * 检查ID
     * @param id
     */
    void checkId(String id);

    /**
     * 检查名字是否存在
     * @param name
     * @return
     */
    Brand nameIsExist(String name);
}
