package com.ming.ls.modules.common.util;

import org.codehaus.jackson.map.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * jakson 工具类
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);

        // 取消默认转换 timestamps 形式
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 忽略空bean转json的一个错误
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);

        // 所有的日期格式都统一为一下的样式, 即 yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(DateTimeUtil.STANDARD_FORMAT));

        // 忽略在json字符串中存在, 但是在java对象中不存在对应属性的情况, 防止出错
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转换为字符串
     *
     * @param obj 对象
     * @param <T> 类型
     * @return String
     */
    public static <T> String obj2String(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 返回格式化的json字符串
     *
     * @param obj 对象
     * @param <T> 泛型
     * @return String
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("Parse object to String error", e);
            return null;
        }
    }

    /**
     * 字符串装换为对象
     *
     * @param str   json 字符串
     * @param clazz 类
     * @param <T>   泛型
     * @return T
     */
//    public static <T> T string2Obj(String str, Class<T> clazz) {
//        if (StringUtils.isEmpty(str) || clazz == null) {
//            return null;
//        }
//
//        try {
//            return clazz.equals(String.class) ? (T) str : objectMapper.reavalue(str, clazz);
//        } catch (IOException e) {
//            log.warn("Parse String to Object error", e);
//            return null;
//        }
//
//    }
//
//
//    /**
//     * 字符串装换为对象
//     *
//     * @param str           json
//     * @param typeReference 类型
//     * @param <T>           泛型
//     * @return T
//     */
//    public static <T> T string2Obj(String str, TypeReference<T> typeReference) {
//        if (StringUtils.isEmpty(str) || typeReference == null) {
//            return null;
//        }
//
//        try {
//            return (T) (typeReference.getType().equals(String.class) ? (T) str : objectMapper.reavalue(str, typeReference));
//        } catch (IOException e) {
//            log.warn("Parse String to Object error", e);
//            return null;
//        }
//    }
//
//    /**
//     * 字符串装换为对象
//     *
//     * @param str             json
//     * @param collectionClass 集合类型
//     * @param elementClasses  元素类型
//     * @param <T>             泛型
//     * @return T
//     */
//    public static <T> T string2Obj(String str, Class<?> collectionClass, Class<?>... elementClasses) {
//        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
//
//        try {
//            return objectMapper.reavalue(str, javaType);
//        } catch (IOException e) {
//            log.warn("Parse String to Object error", e);
//            return null;
//        }
//    }
}
