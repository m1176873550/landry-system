package com.ming.ls.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.ClothingTypeDao;
import com.ming.ls.modules.sys.entity.ClothingType;
import com.ming.ls.modules.sys.entity.HangingPoint;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IClothingTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.cloth.ClothTypeGroup;
import com.ming.ls.modules.sys.vo.cloth.ClothVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@Service
@Transactional(timeout = 30)
public class ClothingTypeServiceImpl extends ServiceImpl<ClothingTypeDao, ClothingType> implements IClothingTypeService {
    @Autowired
    private IClothingTypeService iClothingTypeService;
    @Autowired
    private ClothingTypeDao clothingTypeDao;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public List<ClothingType> list(BaseQuery query) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        //查询wrapper
        LambdaQueryWrapper<ClothingType> wrapper = new LambdaQueryWrapper<ClothingType>()
                .select(ClothingType::getId, ClothingType::getName, ClothingType::getPrice, ClothingType::getParentId)
                .eq(ClothingType::getLaundryId, laundryId)
                .isNotNull(ClothingType::getParentId)
                .orderByDesc(ClothingType::getUpdatedAt);
        if (StringUtils.isNotBlank(query.getId())){
            wrapper.eq(ClothingType::getParentId, query.getId());
        }
        if (StringUtils.isNotBlank(query.getKey())){
            wrapper.like(ClothingType::getName, query.getKey());
        }
        //返回列表结果
        return clothingTypeDao.selectList(wrapper);
    }


    /**
     * 新增
     *
     * @param clothVo
     * @return
     */
    @Override
    public boolean add(ClothVo clothVo) {
        //判断
        if (checkName(clothVo.getName()) != null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "衣物类型名称已存在");
        }
        //ID查询父级对象，检查该父级是否存在
        ClothingType parent = iClothingTypeService.parentByName(clothVo);
        //string价格格式转化
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(clothVo.getPrice()));
        //插入
        if (iClothingTypeService.save(
                new ClothingType()
                        .setName(clothVo.getName())
                        .setPrice(price)
                        //checkParent后，对象一定存在
                        .setParentId(parent.getId()))) {
            return true;
        }
        //插入失败
        return false;
    }

    /**
     * 编辑
     *
     * @param clothVo
     * @return
     */
    @Override
    public boolean edit(ClothVo clothVo) {
        checkId(clothVo.getId());
        ClothingType cloth = iClothingTypeService.checkName(clothVo.getName());
        //ID查询父级对象，检查该父级是否存在
        ClothingType parent = iClothingTypeService.parentById(clothVo);
        //string价格格式转化
        BigDecimal price = BigDecimal.valueOf(Double.parseDouble(clothVo.getPrice()));
        if (cloth != null) {
            //未修改名称和价格
            if (StringUtils.equals(cloth.getId(), clothVo.getId())
                    && price.equals(cloth.getPrice())) {
                return true;
                //名称存在,价格不同
            } else if (!StringUtils.equals(cloth.getId(), clothVo.getId())) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "衣物类型名称已存在");
            } else {
                //更新对象
                ClothingType clothingType = new ClothingType()
                        .setId(clothVo.getId())
                        .setName(clothVo.getName())
                        .setPrice(price)
                        //checkParent后，对象一定存在
                        .setParentId(parent.getId());
                //更新
                if (iClothingTypeService.updateById(clothingType)) {
                    return true;
                } else {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "数据库异常，衣物类型删除失败");
                }
            }
        }
        return false;
    }

    /**
     * 检查衣物类型名称是否重复
     *
     * @param name
     * @return
     */
    @Override
    public ClothingType checkName(String name) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<ClothingType> nameWrapper = new LambdaQueryWrapper<ClothingType>()
                .eq(ClothingType::getLaundryId, laundryId)
                .eq(ClothingType::getName, name);
        return iClothingTypeService.getOne(nameWrapper);
    }

    /**
     * 检查ID
     *
     * @param id
     */
    @Override
    public void checkId(String id) {
        if (iClothingTypeService.getById(id) == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "衣物类型ID异常");
        }
    }

    /**
     * 检查父级是否存在，不存在就新建
     *
     * @param name
     */
    @Override
    public void checkParent(String name) {
        ClothingType type = iClothingTypeService.checkName(name);
        if (type == null) {
            if (!iClothingTypeService.save(new ClothingType().setName(name))) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "数据库异常，衣物类型添加失败");
            }
        }
    }

    /**
     * 父级ID和name列表
     *
     * @return
     */
    @Override
    public List<ClothingType> parentTypes() {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        //查询wrapper
        LambdaQueryWrapper<ClothingType> wrapper = new LambdaQueryWrapper<ClothingType>()
                .select(ClothingType::getId, ClothingType::getName)
                .eq(ClothingType::getLaundryId, laundryId)
                .isNull(ClothingType::getParentId)
                .orderByDesc(ClothingType::getUpdatedAt);
        //返回列表结果
        return clothingTypeDao.selectList(wrapper);
    }


    /**
     * 通过名称查询父级对象
     *
     * @return
     */
    @Override
    public ClothingType parentByName(ClothVo clothVo) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        //检查父级是否存在
//        iClothingTypeService.checkParent(clothVo.getParentId());
        //查询父级ID
        LambdaQueryWrapper<ClothingType> parentWrapper = new LambdaQueryWrapper<ClothingType>()
                .select(ClothingType::getId)
                .eq(ClothingType::getLaundryId, laundryId)
                .eq(ClothingType::getName, clothVo.getParentName());
        //父级对象
        ClothingType type = iClothingTypeService.getOne(parentWrapper);
        if (type == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前父级不存在");
        }
        return type;
    }

    @Override
    public ClothingType parentById(ClothVo clothVo) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        //检查父级是否存在
//        iClothingTypeService.checkParent(clothVo.getParentId());
        //查询父级ID
        LambdaQueryWrapper<ClothingType> parentWrapper = new LambdaQueryWrapper<ClothingType>()
                .select(ClothingType::getId)
                .eq(ClothingType::getLaundryId, laundryId)
                .eq(ClothingType::getId, clothVo.getParentId());
        //父级对象
        ClothingType type = iClothingTypeService.getOne(parentWrapper);
        if (type == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前父级不存在");
        }
        return type;
    }

    /**
     * 级联分组
     *
     * @return
     */
    @Override
    public List<ClothTypeGroup> group() {
        //返回对象
        List<ClothTypeGroup> groupList = new LinkedList<>();
        //查询全集
        LambdaQueryWrapper<ClothingType> groupWrapper = new LambdaQueryWrapper<ClothingType>()
                .select(ClothingType::getId,
                        ClothingType::getName,
                        ClothingType::getParentId)
                .eq(ClothingType::getLaundryId, SessionUtil.getCurrentUser().getLaundryId());
        //结果
        List<ClothingType> typeList = iClothingTypeService.list(groupWrapper);
        //分级
        for (ClothingType c1 : typeList) {
            //子集
            List<ClothTypeGroup> children = new LinkedList<>();
            for (ClothingType c2 : typeList) {
                //id==parentId
                if (StringUtils.equals(c1.getId(), c2.getParentId())) {
                    //第一分组
                    ClothTypeGroup group1 = new ClothTypeGroup();
                    //第二分组
                    ClothTypeGroup group2 = new ClothTypeGroup();
                    //复制对象
                    group1.setValue(c1.getId())
                            .setLabel(c1.getName())
                            .setPrice(c1.getPrice());
                    group2.setValue(c2.getId())
                            .setLabel(c2.getName())
                            .setPrice(c2.getPrice());
                    //加入子集
                    children.add(group2);
                    //设置成对应对象子集
                    group1.setChildren(children);
                    groupList.add(group1);
                }
            }
        }
        return groupList;
    }

    /**
     * 改变价格
     *
     * @param id
     * @return
     */
    @Override
    public String changePrice(String id) {
        iClothingTypeService.checkId(id);
        return iClothingTypeService.getById(id).getPrice().toString();
    }
}
