package com.ming.ls.modules.common.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @param <T>
 * @Author nza
 * 统一接口返回对象
 */
// 保证序列化Json对象时, 会忽略null的对象
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = 708309692405384337L;

    private int status;   // 响应状态
    private String msg;   // 状态信息
    private T data;       // 响应数据

    private ServerResponse(int status) {
        this.status = status;
    }

    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    // 不在序列化结果当中
    @JsonIgnore
    public boolean isSuccess() {
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    // 创建一个成功的响应
    public static <T> ServerResponse<T>
    createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    // 创建一个带数据的成功响应
    public static <T> ServerResponse<T> createBySuccess(T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    // 创建一个带消息的成功响应
    public static <T> ServerResponse<T> createBySuccessMessage(String msg) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    // 创建一个带消息和数据的成功响应
    public static <T> ServerResponse<T> createBySuccess(String msg, T data) {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg, data);
    }

    // 创建一个失败的响应
    public static <T> ServerResponse<T> createByError() {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getDesc());
    }

    // 创建一个自定义信息的失败响应
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    // 创建一个自定义状态码与信息的失败响应
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorData(T errors) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errors);
    }

    // 创建一个自定义状态码与信息的失败响应
    public static <T> ServerResponse<T> createByErrorCodeData(int errorCode, T errors) {
        return new ServerResponse<T>(errorCode, errors);
    }
}
