package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.dao.ColorDao;
import com.ming.ls.modules.sys.entity.Color;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IColorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.color.ColorVo;
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
public class ColorServiceImpl extends ServiceImpl<ColorDao, Color> implements IColorService {

    @Autowired
    private IColorService iColorService;
    @Autowired
    private ColorDao colorDao;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public List<Color> list(BaseQuery query) {
        LambdaQueryWrapper<Color> colorWrapper = new LambdaQueryWrapper<>();
        colorWrapper.select(Color::getId, Color::getName);
        //登录用户店铺ID
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        if (!StringUtils.equals(Const.LAUNDRY_ID, laundryId)) {
            colorWrapper.eq(Color::getLaundryId, laundryId);
        }
        //模糊查询
        colorWrapper.like(Color::getName, query.getKey())
                .orderByDesc(Color::getUpdatedAt);
        return colorDao.selectList(colorWrapper);
    }

    /**
     * 检查ID
     *
     * @param id
     */
    @Override
    public void checkId(String id) {
        Color c = iColorService.getById(id);
        if (c == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "无效颜色ID");
        }
    }

    /**
     * 编辑
     *
     * @param colorVo
     * @return
     */
    @Override
    public boolean editColor(ColorVo colorVo) {
        //检查ID
        checkId(colorVo.getId());
        //判断名称
        Color nameIsExist = iColorService.nameIsExist(colorVo.getName());
        //名称存在
        if (nameIsExist!=null){
            //未修改时存在
            if (StringUtils.equals(nameIsExist.getId(),colorVo.getId())){
                return true;
                //修改时存在
            }else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(),"颜色名称已存在");
            }
        }
        //当名称不存在时，修改
        Color color = new Color()
                .setId(colorVo.getId())
                .setName(colorVo.getName());
        if (iColorService.updateById(color)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 增
     *
     * @param name
     * @return
     */
    @Override
    public boolean addColor(String name) {
        Color nameIsExisting = iColorService.nameIsExist(name);
        if (nameIsExisting!=null){
            throw new LsServiceException(ResponseCode.ERROR.getCode(),"颜色名称已存在");
        }
        Color color = new Color()
                .setName(name)
                .setLaundryId(SessionUtil.getCurrentUser().getLaundryId());
        if (iColorService.save(color)) {
            return true;
        }
        return false;
    }

    /**
     * 检查名字是否存在
     *
     * @param name
     * @return
     */
    @Override
    public Color nameIsExist(String name) {
        //店铺ID
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<Color> nameWrapper = new LambdaQueryWrapper<Color>()
                .select(Color::getName, Color::getId)
                .eq(Color::getLaundryId, laundryId)
                .eq(Color::getName,name);
        //返回color对象
        return iColorService.getOne(nameWrapper);
    }
}
