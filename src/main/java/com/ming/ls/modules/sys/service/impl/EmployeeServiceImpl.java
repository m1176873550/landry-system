package com.ming.ls.modules.sys.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.sysConst.Const;
import com.ming.ls.modules.common.util.*;
import com.ming.ls.modules.sys.dao.EmployeeDao;
import com.ming.ls.modules.sys.entity.Customer;
import com.ming.ls.modules.sys.entity.Employee;
import com.ming.ls.modules.sys.entity.EmployeeAndRole;
import com.ming.ls.modules.sys.query.BaseQuery;
import com.ming.ls.modules.sys.service.ICustomerService;
import com.ming.ls.modules.sys.service.IEmployeeAndRoleService;
import com.ming.ls.modules.sys.service.IEmployeeService;
import com.ming.ls.modules.sys.vo.employee.EmployeeListVo;
import com.ming.ls.modules.sys.vo.employee.EmployeeVo;
import com.ming.ls.modules.sys.vo.employee.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.bean.BeanUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.ming.ls.modules.sys.CurrentUserConstant.CUSTOMER;
import static com.ming.ls.modules.sys.CurrentUserConstant.EMPLOYEE;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mj
 * @since 2020-01-08
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements IEmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private IEmployeeService iemployeeService;
    /**
     * 顾客服务类
     */
    @Autowired
    private ICustomerService iCustomerService;
    @Autowired
    private IEmployeeAndRoleService iEmployeeAndRoleService;

    /**
     * 列表
     *
     * @param query
     * @return
     */
    @Override
    public IPage<EmployeeListVo> list(BaseQuery query) {
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.orderByDesc(Employee::getAttachmentUrl);
        queryWrapper.eq(Employee::getAddress,"1");
        employeeDao.selectOne(queryWrapper);
        Employee currentUser = SessionUtil.getCurrentUser();
        query.setLaundryId(currentUser.getLaundryId()).setIsAdmin(currentUser.getIsAdmin());
        IPage<EmployeeListVo> page = new Page<>(query.getCurrentPage(), query.getSize());
        return employeeDao.list(page, query);
    }

    /**
     * 新增
     *
     * @param addVo
     */
    @Override
    @Transactional
    public void addEmployee(EmployeeVo addVo) {
        isRepeat(addVo, Const.ADD);
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        //实体类赋值
        Employee e = new Employee();
        //管理员，可操作职员所属店铺
        if (SessionUtil.getCurrentUser().getIsAdmin() == 1) {
            BeanUtil.copyProperties(addVo, e, "isAdmin", "password");
            //非管理员不可操作职员所属店铺
        } else {
            BeanUtil.copyProperties(addVo, e, "isAdmin", "password", "laundryId");
            e.setLaundryId(laundryId);
        }
        e.setPassword(MD5.create().digestHex(addVo.getPhone().substring(5,10)));
        iemployeeService.save(e);
    }

    /**
     * 编辑
     *
     * @param editVo
     */
    @Override
    @Transactional
    public void editEmployee(EmployeeVo editVo) {
        isRepeat(editVo, Const.EDIT);
        //登录用户所属店铺
        String laundryId = SessionUtil.getCurrentUser().getLaundryId();
        Employee e = new Employee();
        //复制对象
        BeanUtil.copyProperties(editVo, e, "isAdmin", "password", "laundryId");
        //管理员才能修改职员所属店铺，否则默认为修改人的店铺
        if (checkIsAdmin(editVo.getId())) {
            e.setLaundryId(editVo.getLaundryId());
        } else {
            e.setLaundryId(laundryId);
        }
        if (!iemployeeService.updateById(e)) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "更新职员信息失败，数据库异常");
            //更新成功,继续插入职员角色表数据
        } else {
            //删除旧职员角色
            LambdaUpdateWrapper<EmployeeAndRole> delWrapper = new LambdaUpdateWrapper<EmployeeAndRole>()
                    .eq(EmployeeAndRole::getEmployeeId, editVo.getId());
            if (iEmployeeAndRoleService.remove(delWrapper)){
                //插入新职员角色
                LinkedList<EmployeeAndRole> employeeAndRoles = new LinkedList<>();
                for (int start = 0; start < editVo.getRoleId().size(); start++) {
                    employeeAndRoles.add(
                            new EmployeeAndRole()
                                    .setEmployeeId(editVo.getId())
                                    .setRoleId(editVo.getRoleId().get(start))
                    );
                }
                if (!iEmployeeAndRoleService.saveBatch(employeeAndRoles)) {
                    throw new LsServiceException(ResponseCode.ERROR.getCode(), "更新职员角色信息失败，数据库异常");
                }
            }else {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "删除旧职员角色信息失败，数据库异常");
            }
        }
    }

    /**
     * 检查是否管理员
     *
     * @param id
     * @return
     */
    @Override
    public boolean checkIsAdmin(String id) {
        checkId(id);
        Employee employee = employeeDao.selectOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getId, id)
                        .select(Employee::getIsAdmin)
        );
        //为1时为管理员
        return employee.getIsAdmin() == 1;
    }
    /**
     * 检查是否管理员
     *
     * @return
     */
    @Override
    public boolean checkIsAdmin() {
        String id = SessionUtil.getCurrentUser().getId();
        return checkIsAdmin(id);
    }

    @Override
    public String getUserName() {
        return SessionUtil.getCurrentUser().getFullName();
    }

    /**
     * 检查合法性
     *
     * @param id
     */
    @Override
    public void checkId(String id) {
        Employee employee = employeeDao.selectOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getId, id)
                        .select(Employee::getId)
        );
        if (employee == null) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "无效职员ID");
        }
    }

    @Override
    public void urlImage(String id, HttpServletResponse response) throws IOException {
        checkId(id);
        //查询
        String attachmentUrl = employeeDao.selectOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getId, id)
                        .select(Employee::getAttachmentUrl)
        ).getAttachmentUrl();
        ImageUtil imageUtil = new ImageUtil();
        imageUtil.image(attachmentUrl, response);
    }

    /**
     * 删除职员
     *
     * @param id
     */
    @Override
    @Transactional
    public void delEmployee(String id) {
        checkId(id);
        if (employeeDao.deleteById(id) == 0) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "删除失败,请稍后重试");
        }
    }

    /**
     * 编辑登录名是否重复
     *
     * @param employeeVo
     */
    @Override
    public void isRepeat(EmployeeVo employeeVo, String addOrEdit) {
        LambdaQueryWrapper<Employee> loginNameWrapper = new LambdaQueryWrapper<>();
        //登陆名称查询wrapper
        LambdaQueryWrapper<Employee> loginNameWrapper2 = new LambdaQueryWrapper<>();
        //编辑,
        if (Const.EDIT.equals(addOrEdit)) {
            //先ID
            checkId(employeeVo.getId());
            loginNameWrapper.select(Employee::getLoginName)
                    .eq(Employee::getId, employeeVo.getId());
            loginNameWrapper2.eq(Employee::getLoginName,employeeVo.getLoginName());
            //查询的职员
            Employee employee = employeeDao.selectOne(loginNameWrapper);
            if (employeeDao.selectCount(loginNameWrapper) > 0
                    && !employee.getLoginName().equals(employeeVo.getLoginName())
                    && employeeDao.selectCount(loginNameWrapper2)>0) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "登陆名称已存在，请修改");
            }
            //新增职员
        } else {
            //直接查询名字
            loginNameWrapper
                    .eq(Employee::getLoginName, employeeVo.getLoginName())
                    .select(Employee::getId);
            Employee employee = employeeDao.selectOne(loginNameWrapper);
            if (employee != null) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "登陆名称已存在，请修改");
            }
        }

    }

    /**
     * 登陆
     *
     * @param loginVo
     * @return
     */
    @Override
    public String login(LoginVo loginVo) {
        //登录对象
        Employee employee = employeeDao.selectOne(new LambdaQueryWrapper<Employee>()
                .eq(Employee::getLoginName, loginVo.getName()));
            //判断是否正常登陆
            if (employee == null || !employee.getPassword().equals(MD5.create().digestHex(loginVo.getPassword()))) {
                throw new LsServiceException(ResponseCode.ERROR.getCode(), "登陆失败，用户名或密码错误");
                //登陆成功
            } else {
                //存入用户信息Session
                SessionUtil.setCurrentUser(employee);
                //权限存入缓存
                List<String> roleIds = iEmployeeAndRoleService.getRoleIdByEmpId(employee.getId());
                CacheUtil.setKey(employee.getId(),roleIds);
                //前端到店员登陆成功的着陆页
                return EMPLOYEE;
            }
    }

    /**
     * 注销
     */
    @Override
    public void logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            SessionUtil.removeSession(Const.CURRENT_USER);
            CookieUtil.delLoginToken(request, response);
        }catch (LsServiceException e){
            throw new LsServiceException(ResponseCode.ERROR.getCode(),"注销异常");
        }

    }


    @Override
    public void doForgetPwd(LoginVo employee) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getLoginName, employee.getName()).eq(Employee::getIdCard,employee.getIdCard());
        Employee one = iemployeeService.getOne(wrapper);
        if (one !=null){
            one.setPassword(MD5.create().digestHex(employee.getNewPassword()));
            iemployeeService.updateById(one);
        }else {
            throw new LsServiceException("用户名不存在");
        }
    }

    @Override
    public void doChangePwd( LoginVo employee) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<Employee>()
                .eq(Employee::getFullName, employee.getName());
        Employee one = iemployeeService.getOne(wrapper);
        if (one !=null){
            iemployeeService.updateById(one.setPassword(MD5.create().digestHex(employee.getNewPassword())));
        }else {
            throw new LsServiceException("修改失败");
        }
    }

    /**
     * 获取店铺名
     *
     * @param id
     * @return
     */
    @Override
    public String laundryIdById(String id) {
        checkId(id);
        Employee employee = employeeDao.selectById(id);
        return employee.getLaundryId();
    }

    /**
     * 检查是否顾客
     *
     * @param loginVo
     * @return
     */
    @Override
    public boolean isCustomer(LoginVo loginVo) {
        //顾客
        LambdaQueryWrapper<Customer> loginWrapper = new LambdaQueryWrapper<Customer>()
                .select(Customer::getPassword)
                .eq(Customer::getPhone, loginVo.getName());
        Customer customer = iCustomerService.getOne(loginWrapper);
        return customer != null && StringUtils
                .equals(customer.getPassword(), MD5.create().digestHex(loginVo.getPassword()));
    }


}
