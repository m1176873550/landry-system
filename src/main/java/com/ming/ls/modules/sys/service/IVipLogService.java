package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.VipLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-03-18
 */
public interface IVipLogService extends IService<VipLog> {
    void recordLog(VipLog vipLog);
}
