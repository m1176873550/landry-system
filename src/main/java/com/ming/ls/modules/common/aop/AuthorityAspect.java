package com.ming.ls.modules.common.aop;

import com.google.common.collect.Lists;
import com.ming.ls.modules.common.annotation.Authorized;
import com.ming.ls.modules.common.exception.LsServiceException;
import com.ming.ls.modules.common.util.CacheUtil;
import com.ming.ls.modules.common.util.ResponseCode;
import com.ming.ls.modules.common.util.SessionUtil;
import com.ming.ls.modules.sys.service.IVipLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制
 * @Author: nza
 */
@Slf4j
@Aspect
@Component
public class AuthorityAspect {

    /**
     * 日志业务对象
     */
    @Autowired
    private IVipLogService vipLogService;



    /**
     * 环绕通知
     * @param pjp 切点对象
     * @return Object
     */
    @Around("@annotation(com.ming.ls.modules.common.annotation.Authorized)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();     // 获取方法签名
        Method method = methodSignature.getMethod();                                // 获取方法
        String[] codes = method.getAnnotation(Authorized.class).codes();// 获取权限编码
        // 获取当前用户权限编码
        String key =SessionUtil.getCurrentUser().getId();

        List<String> permissionEncodes = (List<String>) CacheUtil.getKey(key);

        // 如果获取不到
        if (permissionEncodes == null || permissionEncodes.size() == 0) {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "当前用户权限异常, 请重新登录!");
        }

        // 检验并返回
        return verify(pjp, codes, permissionEncodes);
    }

    /**
     * 校验用户权限
     * @param pjp    切点方法
     * @param codes   权限编码
     * @param permissionEncodes 用户权限编码集合
     * @return  Object
     */
    private Object verify(ProceedingJoinPoint pjp, String[] codes, List<String> permissionEncodes) throws Throwable {
        ArrayList<String> codeList = Lists.newArrayList(codes);
        if ((codeList.size() != 0) && (permissionEncodes != null && (permissionEncodes.size() != 0))) {
            // 校验权限
            for (String code : codeList) {
                if (permissionEncodes.contains(code)) {
                    return pjp.proceed();
                }
            }

            throw new LsServiceException(ResponseCode.ERROR.getCode(), "无权限");
        } else {
            throw new LsServiceException(ResponseCode.ERROR.getCode(), "无权限");
        }
    }

//    /**
//     * 记录系统日志
//     * @param code 权限编码
//     */
//    private void recordSysLog(String code) {
//        String des = "用户[" +
//                SessionUtil.getCurrentUser().getUserName() +
//                "]无权限访问" + code;
//        logService.create(new LogCreateVo()
//                .setIp(SysUtil.getCurrentIp())
//                .setDescription(des)
//                .setLevel(LogLevelEnum.LOG_LEVEL_NORMAL.getCode())
//                .setType(LogType.LOG_AUTHORIZED));
//    }
}
