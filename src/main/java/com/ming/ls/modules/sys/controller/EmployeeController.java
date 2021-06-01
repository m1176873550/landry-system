package com.ming.ls.modules.sys.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.common.util.ServerResponse;
import com.ming.ls.modules.common.util.ValidUtils;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.IEmployeeService;
import com.ming.ls.modules.sys.vo.employee.EmployeeListVo;
import com.ming.ls.modules.sys.vo.employee.EmployeeVo;
import com.ming.ls.modules.sys.vo.employee.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mj
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private IEmployeeService iEmployeeService;

    @GetMapping()
    public ServerResponse list(BaseQuery query ,Errors errors){
        ValidUtils.validateArg(errors);
        IPage<EmployeeListVo> list = iEmployeeService.list(query);
        return ServerResponse.createBySuccess(list);
    }

    /**
     * 编辑
     *
     * @param editVo
     * @return
     */
    @PutMapping()
    public ServerResponse edit(@Validated({EmployeeVo.Update.class}) @RequestBody EmployeeVo editVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iEmployeeService.editEmployee(editVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 新增
     * @param addVo
     * @param errors
     * @return
     */
    @PostMapping()
    public ServerResponse add(@Validated({EmployeeVo.Create.class}) @RequestBody EmployeeVo addVo, Errors errors) {
        ValidUtils.validateArg(errors);
        iEmployeeService.addEmployee(addVo);
        return ServerResponse.createBySuccess();
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ServerResponse del(@PathVariable String id) {
        iEmployeeService.delEmployee(id);
        return ServerResponse.createBySuccess();
    }

    /**
     * 登陆
     * @param loginVo
     * @return
     */
    @PostMapping("/login")
    public ServerResponse login(@RequestBody @Validated LoginVo loginVo,Errors errors) {
        ValidUtils.validateArg(errors);
        String login = iEmployeeService.login(loginVo);

        //权限存入缓存
        return ServerResponse.createBySuccess(login);
    }
    /**
     * 注销
     * @return
     */
    @PutMapping("/logout")
    public ServerResponse logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        iEmployeeService.logout(session,request,response);
        return ServerResponse.createBySuccess();
    }

    /**
     * 忘记密码
     * @return
     */
    @PutMapping("/doForgetPwd")
    public ServerResponse doForgetPwd(@RequestBody LoginVo employee) {
        iEmployeeService.doForgetPwd(employee);
        return ServerResponse.createBySuccess();
    }
    /**
     * 修改密码
     * @return
     */
    @PutMapping("/doChangePwd")
    public ServerResponse doChangePwd(@RequestBody LoginVo employee) {
        iEmployeeService.doChangePwd(employee);
        return ServerResponse.createBySuccess();
    }
    /**
     * 通过职员ID查询店铺ID
     * @param id
     * @return
     */
    @GetMapping("/{id}/laundry-id-by-id")
    public ServerResponse laundryIdById(@PathVariable String id){
        String laundryId = iEmployeeService.laundryIdById(id);
        return ServerResponse.createBySuccess(laundryId);
    }

    /**
     * 通过职员ID查询店铺ID
     * @return
     */
    @GetMapping("/is-admin")
    public ServerResponse checkIsAdmin(){
        boolean isAdmin = iEmployeeService.checkIsAdmin();
        return ServerResponse.createBySuccess(isAdmin);
    }

    /**
     * 通过职员ID查询店铺ID
     * @return
     */
    @GetMapping("/getCurrentUserName")
    public ServerResponse getCurrentUserName(){
        String userName = iEmployeeService.getUserName();
        return ServerResponse.createBySuccess(userName);
    }
}
