package com.ming.ls.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.dao.LaundryDao;
import com.ming.ls.modules.sys.entity.Laundry;
import com.ming.ls.modules.sys.service.ILaundryService;
import com.ming.ls.modules.sys.vo.Laundry.IdAndNameList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-10
 */
@Service
@Transactional(timeout = 30)
public class LaundryServiceImpl extends ServiceImpl<LaundryDao, Laundry> implements ILaundryService {

    @Autowired
    private ILaundryService iLaundryService;

    @Autowired
    private LaundryDao laundryDao;
    /**
     * 店铺ID和名字
     *
     * @return
     */
    @Override
    public List<IdAndNameList> laundryIdAndNameList() {
        return laundryDao.laundryIdAndNameList();
    }
}
