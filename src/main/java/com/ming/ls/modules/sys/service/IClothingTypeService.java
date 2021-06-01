package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.ClothingType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.cloth.ClothTypeGroup;
import com.ming.ls.modules.sys.vo.cloth.ClothVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IClothingTypeService extends IService<ClothingType> {
    /**
     * 列表
     * @param query
     * @return
     */
    List<ClothingType> list(BaseQuery query);

    /**
     * 新增
     * @param clothVo
     * @return
     */
    boolean add(ClothVo clothVo);

    /**
     * 编辑
     * @param clothVo
     * @return
     */
    boolean edit(ClothVo clothVo);

    /**
     * 检查名字是否存在
     * @param name
     * @return
     */
    ClothingType checkName(String name);

    /**
     * 检查ID合法性
     * @param id
     */
    void checkId(String id);

    /**
     * 检查父级是否存在，不存在就新建
     *
     * @param name
     */
    void checkParent(String name);

    /**
     * 名字和ID列表
     * @return
     */
    List<ClothingType> parentTypes();

    /**
     * 查询父级对象通过名称
     * @param clothVo
     * @return
     */
    ClothingType parentByName(ClothVo clothVo);
    /**
     * 查询父级对象通过ID
     * @param clothVo
     * @return
     */
    ClothingType parentById(ClothVo clothVo);

    /**
     * 级联分组
     * @return
     */
    List<ClothTypeGroup> group();

    /**
     * 改变价格
     * @param id
     * @return
     */
    String changePrice(String id);

}
