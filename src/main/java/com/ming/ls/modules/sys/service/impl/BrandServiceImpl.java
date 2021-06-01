package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.entity.Brand;
import com.ming.ls.modules.sys.dao.BrandDao;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.brand.BrandVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class BrandServiceImpl extends ServiceImpl<BrandDao, Brand> implements IBrandService {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private IBrandService iBrandService;

    /**
     * 列表
     *
     * @return
     */
    @Override
    public List<Brand> list(BaseQuery query) {
        LambdaQueryWrapper<Brand> brandWrapper = new LambdaQueryWrapper<>();
        //登录用户店铺ID
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        //若不为管理员
        if (!StringUtils.equals(Const.LAUNDRY_ID, laundryId)) {
            brandWrapper.eq(Brand::getLaundryId, laundryId);
        }
        brandWrapper.like(Brand::getName, query.getKey()).orderByDesc(Brand::getUpdatedAt);
        //返回值
        return brandDao.selectList(brandWrapper);
    }

    /**
     * 编辑
     *
     * @param brandVo
     * @return
     */
    @Override
    public void editBrand(BrandVo brandVo) {
        iBrandService.checkId(brandVo.getId());
        //检查名称是否存在
        Brand nameIsExist = iBrandService.nameIsExist(brandVo.getName());
        if (nameIsExist != null) {
            //存在但未修改
            if (StringUtils.equals(nameIsExist.getId(), brandVo.getId())) {
                return;
                //存在，且已修改
            } else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "品牌名称已存在");
            }
        }
        //不存在
        Brand brand = new Brand()
                .setId(brandVo.getId())
                .setName(brandVo.getName());
        if (!iBrandService.updateById(brand)) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "更新失败，数据库异常");
        }
    }

    /**
     * 新增
     *
     * @param brandVo
     */
    @Override
    public void addBrand(BrandVo brandVo) {
        //检查名称是否存在
        Brand nameIsExist = iBrandService.nameIsExist(brandVo.getName());
        if (nameIsExist != null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "品牌名称已存在");
        }
        //名称不存在
        Brand brand = new Brand()
                .setName(brandVo.getName())
                .setLaundryId(SessionUtil.getCurrentUser().getLaundryId());
        if (!iBrandService.save(brand)) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "插入失败，数据库异常");
        }
    }

    /**
     * 品牌ID检查
     *
     * @param id
     */
    @Override
    public void checkId(String id) {
        Brand b = iBrandService.getById(id);
        if (b == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "无效品牌ID");
        }
    }

    @Override
    public Brand nameIsExist(String name) {
        //店铺ID
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<Brand> nameWrapper = new LambdaQueryWrapper<Brand>()
                .select(Brand::getName, Brand::getId)
                .eq(Brand::getLaundryId, laundryId)
                .eq(Brand::getName,name);
        //返回color对象
        return iBrandService.getOne(nameWrapper);
    }


}
