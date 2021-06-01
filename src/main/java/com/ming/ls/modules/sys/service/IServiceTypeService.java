package com.ming.ls.modules.sys.service;

import com.ming.ls.modules.sys.entity.ServiceType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.serviceType.ServiceTypeVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
public interface IServiceTypeService extends IService<ServiceType> {

    List<ServiceType> list(BaseQuery query);

    boolean editType(ServiceTypeVo serviceTypeVo);

    boolean addType(ServiceTypeVo serviceTypeVo);

    void checkId(String id);

    ServiceType checkName(String name);
}
