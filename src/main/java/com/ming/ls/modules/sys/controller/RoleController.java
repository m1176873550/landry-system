package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IRoleService;
import com.ming.ls.modules.sys.vo.role.RoleIdAndName;
import com.ming.ls.modules.sys.vo.role.RoleListVo;
import com.ming.ls.modules.sys.vo.role.RoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-09
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService iRoleService;

    @PostMapping()
    public ServerResponse list(@RequestBody BaseQuery query){
        IPage<RoleListVo> list = iRoleService.list(query);
        return ServerResponse.createBySuccess(list);
    }
    @PutMapping()
    @Validated(value = RoleVo.Updated.class)
    public ServerResponse edit(@RequestBody RoleVo roleVo){
        iRoleService.edit(roleVo);
        return ServerResponse.createBySuccess();
    }
    @PostMapping("/add")
    @Validated(value = RoleVo.Created.class)
    public ServerResponse add(@RequestBody RoleVo roleVo){
        iRoleService.add(roleVo);
        return ServerResponse.createBySuccess();
    }
    @DeleteMapping("/{id}")
    public ServerResponse del(@PathVariable("id") String id){
        iRoleService.del(id);
        return ServerResponse.createBySuccess();
    }
    @GetMapping("/get-role-id-and-name")
    public ServerResponse getRoleIdAndName(){
        List<RoleIdAndName> roleIdAndName = iRoleService.getRoleIdAndName();
        return ServerResponse.createBySuccess(roleIdAndName);
    }
}
