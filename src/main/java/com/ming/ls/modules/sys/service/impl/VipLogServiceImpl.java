package com.ming.ls.modules.sys.service.impl;

import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.sys.dao.VipLogDao;
import com.ming.ls.modules.sys.entity.VipLog;
import com.ming.ls.modules.sys.service.IVipLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-03-18
 */
@Service
public class VipLogServiceImpl extends ServiceImpl<VipLogDao, VipLog> implements IVipLogService {

    @Autowired
    private IVipLogService iVipLogService;
    @Override
    public void recordLog(VipLog vipLog) {
        if (!iVipLogService.save(vipLog)){
            throw new LsServiceException("日志记录失败，数据库异常");
        }
    }
}
