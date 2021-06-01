package com.ming.ls.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.entity.ServiceType;
import com.ming.ls.modules.sys.dao.ServiceTypeDao;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IServiceTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.sys.vo.serviceType.ServiceTypeVo;
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
public class ServiceTypeServiceImpl extends ServiceImpl<ServiceTypeDao, ServiceType> implements IServiceTypeService {

    @Autowired
    private IServiceTypeService iServiceTypeService;
    @Autowired
    private ServiceTypeDao serviceTypeDao;
    @Override
    public List<ServiceType> list(BaseQuery query) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<ServiceType> typeList = new LambdaQueryWrapper<ServiceType>()
                .select(ServiceType::getName, ServiceType::getId)
                .eq(ServiceType::getLaundryId, laundryId)
                .orderByDesc(ServiceType::getUpdatedAt)
                .like(ServiceType::getName, query.getKey());
        return serviceTypeDao.selectList(typeList);
    }

    /**
     * 编辑
     * @param serviceTypeVo
     * @return
     */
    @Override
    public boolean editType(ServiceTypeVo serviceTypeVo) {
        checkId(serviceTypeVo.getId());
        //判断是否修改名字
        ServiceType serviceType = checkName(serviceTypeVo.getName());
        if (serviceType!=null){
            //未修改
            if (StringUtils.equals(serviceTypeVo.getId(),serviceType.getId())){
                return true;
            }else {  //已修改
                throw new LsServiceException(ResponseCode.ERROR.getCode(),"服务类型已存在");
            }
        }
        //更新对象
        ServiceType updateType = new ServiceType()
                .setId(serviceTypeVo.getId())
                .setName(serviceTypeVo.getName());
        if (serviceTypeDao.updateById(updateType)>0){
            return true;
        }else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(),"更新服务类型失败，数据库异常");
        }
    }

    /**
     * 新增
     * @param serviceTypeVo
     * @return
     */
    @Override
    public boolean addType(ServiceTypeVo serviceTypeVo) {
        //检查名字
        ServiceType type = checkName(serviceTypeVo.getName());
        if (type!=null){
            throw new LsServiceException(ResponseCode.ERROR.getCode(),"服务类型已存在");
        }
        //插入
        ServiceType serviceType = new ServiceType()
                .setName(serviceTypeVo.getName());
        if (iServiceTypeService.save(serviceType)){
            return true;
        }else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(),"新增服务类型失败，数据库异常");
        }
    }

    /**
     * 检查ID
     * @param id
     */
    @Override
    public void checkId(String id) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<ServiceType> typeWrapper = new LambdaQueryWrapper<ServiceType>()
                .eq(ServiceType::getId, id)
                .eq(ServiceType::getLaundryId, laundryId);
        //ID查询到的对象
        ServiceType type = iServiceTypeService.getOne(typeWrapper);
        if (type==null){
            throw new LsServiceException(ResponseCode.ERROR.getCode(),"服务类型ID异常");
        }
    }

    /**
     * 判断名称是否存在
     * @param name
     * @return
     */
    @Override
    public ServiceType checkName(String name) {
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        LambdaQueryWrapper<ServiceType> nameWrapper = new LambdaQueryWrapper<ServiceType>()
                .eq(ServiceType::getName, name)
                .eq(ServiceType::getLaundryId, laundryId);
        return iServiceTypeService.getOne(nameWrapper);
    }
}
