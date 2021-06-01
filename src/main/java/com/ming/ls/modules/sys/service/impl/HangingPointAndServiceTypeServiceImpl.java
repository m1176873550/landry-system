package com.ming.ls.modules.sys.service.impl;

import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.sys.entity.HangingPointAndServiceType;
import com.ming.ls.modules.sys.dao.HangingAndServiceTypeDao;
import com.ming.ls.modules.sys.service.IHangingPointAndServiceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.service.IHangingPointService;
import com.ming.ls.modules.sys.service.IServiceTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-02-18
 */
@Service
public class HangingPointAndServiceTypeServiceImpl extends ServiceImpl<HangingAndServiceTypeDao, HangingPointAndServiceType> implements IHangingPointAndServiceTypeService {

    @Autowired
    private IHangingPointAndServiceTypeService hangingPointAndServiceTypeService;
    @Autowired
    private IHangingPointService iHangingPointService;
    @Autowired
    private IServiceTypeService iServiceTypeService;

//    /**
//     * 新增
//     * @param
//     * @return
//     */
//    @Override
//    public boolean addPointAndServiceType(List<HangingPointAndServiceType> hpasts) {
//        for (HangingPointAndServiceType hpast:hpasts){
//            iServiceTypeService.checkId(hpast.getServiceTypeId());
////            hangingPointAndServiceTypeService.saveBatch(hpasts);
//        }
//        return true;
//    }
}
