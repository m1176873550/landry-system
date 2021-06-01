package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.entity.Permission;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.query.permission.PermQuery;
import com.ming.ls.modules.sys.service.IPermissionService;
import com.ming.ls.modules.sys.vo.permission.*;
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
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private IPermissionService iPermissionService;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @PostMapping()
    public ServerResponse list(@RequestBody PermQuery query) {
        IPage<PermissionListVo> list = iPermissionService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse del(@PathVariable("id") String id) {
        if (iPermissionService.removeById(id)) {
            return ServerResponse.createBySuccess();
        } else {
            return ServerResponse.createByErrorMessage("删除失败，数据库异常");
        }
    }

    /**
     * 编辑
     *
     * @param permissionListVo
     * @return
     */
    @PutMapping()
    @Validated(value = PermissionListVo.Update.class)
    public ServerResponse edit(@RequestBody PermissionListVo permissionListVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iPermissionService.editPermission(permissionListVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 新增
     *
     * @param permissionListVo
     * @return
     */
    @PostMapping("/add")
    @Validated(value = PermissionListVo.Update.class)
    public ServerResponse add(@RequestBody PermissionListVo permissionListVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iPermissionService.addPermission(permissionListVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 父级ID和名字的列表
     * @return
     */
    @PostMapping("/parent-id-name")
    public ServerResponse parentNameAndId(@RequestBody PerIdAndStatus per) {
        List<PerNameAndId> list = iPermissionService.parentNameAndId(per.getId());
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 改变可用状态
     * @param per
     * @param errors
     * @return
     */
    @PutMapping("/change-status")
    @Validated
    public ServerResponse changeStatus(@RequestBody PerIdAndStatus per,Errors errors){
        ValidUtils.validateArg(errors);
        iPermissionService.changeStatus(per);
        return ServerResponse.createBySuccess();
    }

    /**
     * 导航路径列表
     * @return
     */
    @GetMapping("/url-name")
    public ServerResponse getUrlAndName(){
        List<PerNameAndUrl> nameAndUrlList = iPermissionService.getNameAndUrl();
        return ServerResponse.createBySuccess(nameAndUrlList);
    }

    /**
     * 根据URL查询ID
     * @return
     */
    @GetMapping("/getIdByUrl")
    public ServerResponse getIdByUrl(@RequestParam("url") String url){
        String id = iPermissionService.getIdByUrl(url);
        return ServerResponse.createBySuccess(id);
    }

    /**
     * 根据ID查询URL
     * @return
     */
    @GetMapping("/getUrlById")
    public ServerResponse getUrlById(@RequestParam("id") String id){
        String url = iPermissionService.getUrlById(id);
        return ServerResponse.createBySuccess(url);
    }
    /**
     * 分组权限列表，角色选择权限时使用
     * @return
     */
    @GetMapping("/group")
    public ServerResponse group(){
        List<IdNameChildren> list = iPermissionService.group();
        return ServerResponse.createBySuccess(list);
    }

}
