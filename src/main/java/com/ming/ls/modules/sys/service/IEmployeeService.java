package com.ming.ls.modules.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ming.ls.modules.sys.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.vo.employee.EmployeeListVo;
import com.ming.ls.modules.sys.vo.employee.EmployeeVo;
import com.ming.ls.modules.sys.vo.employee.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mj
 * @since 2020-01-08
 */
public interface IEmployeeService extends IService<Employee> {
    /**
     * 列表
     * @param query
     * @return
     */
    IPage<EmployeeListVo> list(BaseQuery query);

    /**
     * 新增
     */
    void addEmployee(EmployeeVo addVo);

    /**
     * 编辑
     */
    void editEmployee(EmployeeVo editVo);

    /**
     * 检查是否管理员
     * @param id
     * @return
     */
    boolean checkIsAdmin(String id);
    /**
     * 检查是否管理员
     * @return
     */
    boolean checkIsAdmin();

    String getUserName();
    /**
     * 检查职员ID合法性
     * @param id
     */
    void checkId(String id);

    /**
     * 职员图片
     * @param id
     * @param response
     * @throws IOException
     */
    void urlImage(String id, HttpServletResponse response) throws IOException;

    /**
     * 删除职员
     * @param id
     */
    void delEmployee(String id);

    /**
     * 登陆名是否重复
     * @param employeeVo
     */
    void isRepeat(EmployeeVo employeeVo,String addOrEdit);

    /**
     * 登陆
     * @return
     */
    String login(LoginVo loginVo);
    /**
     * 注销
     * @return
     */
    void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response);

    void doForgetPwd( LoginVo employee);

    void doChangePwd( LoginVo employee);
    /**
     * 查询店员所属店铺ID
     * @param id
     * @return
     */
    String laundryIdById(String id);

    /**
     * 检查是否顾客
     * @param loginVo
     * @return
     */
    boolean isCustomer(LoginVo loginVo);
}
