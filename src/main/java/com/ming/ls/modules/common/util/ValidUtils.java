package com.ming.ls.modules.common.util;

import com.ming.ls.modules.common.exception.LsArgumentException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参数校验工具类
 */
public class ValidUtils {
    /**
     * 校验参数
     *
     * @param errors 错误信息
     */
    public static void validateArg(Errors errors) {
        if (errors.hasErrors()) {
            List<FieldError> errorList = errors.getFieldErrors();
            Map<String, String> map = new HashMap<>();
            for (FieldError error : errorList) {
                map.put(error.getField(), error.getDefaultMessage());
            }
            throw new LsArgumentException(map);
        }
    }
}
