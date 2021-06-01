package com.ming.ls.modules.sys.controller;


import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.entity.ServiceType;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IServiceTypeService;
import com.ming.ls.modules.sys.vo.serviceType.ServiceTypeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/serviceType")
public class ServiceTypeController {
    @Autowired
    private IServiceTypeService iServiceTypeService;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @GetMapping()
    public ServerResponse list(BaseQuery query) {
        List<ServiceType> list = iServiceTypeService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 编辑
     *
     * @param serviceTypeVo
     * @return
     */
    @PutMapping()
    @Validated(value = ServiceTypeVo.Update.class)
    public ServerResponse editServiceType(@RequestBody ServiceTypeVo serviceTypeVo, Errors errors) {
        ValidUtils.validateArg(errors);
        if (iServiceTypeService.editType(serviceTypeVo)) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 新增
     *
     * @param serviceTypeVo
     * @return
     */
    @PostMapping()
    @Validated(value = ServiceTypeVo.Create.class)
    public ServerResponse addServiceType(@RequestBody ServiceTypeVo serviceTypeVo, Errors errors) {
        ValidUtils.validateArg(errors);
        if (iServiceTypeService.addType(serviceTypeVo)) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse delServiceType(@PathVariable String id) {
        iServiceTypeService.checkId(id);
        if (iServiceTypeService.removeById(new ServiceType().setId(id))) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByError();
        }
    }
}
