package com.ranyk.model.response;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * CLASS_NAME: R.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 响应结果前端封装类
 * @date: 2025-10-10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1864845098230381842L;

    /**
     * 响应码
     */
    private int code;
    /**
     * 响应信息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应成功
     *
     * @return 响应结果, 无返回数据, 状态码为 {@link HttpStatus#HTTP_OK} , 响应信息为 "操作成功"
     */
    public static <T> R<T> ok() {
        return restResult(null, HttpStatus.HTTP_OK, "操作成功");
    }

    /**
     * 响应成功
     *
     * @param data 响应数据
     * @param <T>  传入的响应返回数据泛型
     * @return 响应结果, 返回数据为传入的返回数据, 状态码为 {@link HttpStatus#HTTP_OK} , 响应信息为 "操作成功"
     */
    public static <T> R<T> ok(T data) {
        return restResult(data, HttpStatus.HTTP_OK, "操作成功");
    }

    /**
     * 响应成功
     *
     * @param msg 响应信息
     * @param <T> 响应数据泛型
     * @return 响应结果, 无返回数据, 状态码为 {@link HttpStatus#HTTP_OK} , 响应信息为传入的响应信息
     */
    public static <T> R<T> ok(String msg) {
        return restResult(null, HttpStatus.HTTP_OK, msg);
    }

    /**
     * 响应成功
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @param <T>  响应数据泛型
     * @return 响应结果, 返回数据为传入的返回数据, 状态码为 {@link HttpStatus#HTTP_OK} , 响应信息为传入的响应信息
     */
    public static <T> R<T> ok(String msg, T data) {
        return restResult(data, HttpStatus.HTTP_OK, msg);
    }

    /**
     * 响应失败
     *
     * @param <T> 响应数据泛型
     * @return 响应结果, 无返回数据, 状态码为 {@link HttpStatus#HTTP_INTERNAL_ERROR} , 响应信息为 "操作失败"
     */
    public static <T> R<T> fail() {
        return restResult(null, HttpStatus.HTTP_INTERNAL_ERROR, "操作失败");
    }

    /**
     * 响应失败
     *
     * @param msg 响应信息
     * @param <T> 响应数据泛型
     * @return 响应结果, 无返回数据, 状态码为 {@link HttpStatus#HTTP_INTERNAL_ERROR} , 响应信息为传入的响应信息
     */
    public static <T> R<T> fail(String msg) {
        return restResult(null, HttpStatus.HTTP_INTERNAL_ERROR, msg);
    }

    /**
     * 响应失败
     *
     * @param data 响应数据
     * @param <T>  响应数据泛型
     * @return 响应结果, 响应数据为传入的返回数据, 状态码为 {@link HttpStatus#HTTP_INTERNAL_ERROR} , 响应信息为 "操作失败"
     */
    public static <T> R<T> fail(T data) {
        return restResult(data, HttpStatus.HTTP_INTERNAL_ERROR, "操作失败");
    }

    /**
     * 响应失败
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @param <T>  响应数据泛型
     * @return 响应结果, 响应数据为传入的返回数据, 状态码为 {@link HttpStatus#HTTP_INTERNAL_ERROR} , 响应信息为传入的响应信息
     */
    public static <T> R<T> fail(String msg, T data) {
        return restResult(data, HttpStatus.HTTP_INTERNAL_ERROR, msg);
    }

    /**
     * 响应失败
     *
     * @param code 响应码, 参见 {@link HttpStatus} 类中的响应码
     * @param msg  响应信息
     * @param <T>  响应数据泛型
     * @return 响应结果, 无返回数据, 状态码为传入的响应码, 响应信息为传入的响应信息
     */
    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 响应失败
     *
     * @param msg 响应信息
     * @param <T> 响应数据泛型
     * @return 响应结果, 无返回数据, 状态码为 {@link HttpStatus#HTTP_NOT_IMPLEMENTED} , 响应信息为传入的响应信息
     */
    public static <T> R<T> warn(String msg) {
        return restResult(null, HttpStatus.HTTP_NOT_IMPLEMENTED, msg);
    }

    /**
     * 响应失败
     *
     * @param msg  响应信息
     * @param data 响应数据
     * @param <T>  响应数据泛型
     * @return 响应结果, 响应数据为传入的返回数据, 状态码为 {@link HttpStatus#HTTP_NOT_IMPLEMENTED} , 响应信息为传入的响应信息
     */
    public static <T> R<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.HTTP_NOT_IMPLEMENTED, msg);
    }

    /**
     * 响应结果封装方法
     *
     * @param data 响应数据
     * @param code 响应码, 参见 {@link HttpStatus} 类中的响应码
     * @param msg  响应信息
     * @return 响应结果
     */
    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(Objects.isNull(msg) ? "" : msg);
        return r;
    }

    /**
     * 响应结果失败判断
     *
     * @param ret 响应结果
     * @return 响应结果是否失败
     */
    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    /**
     * 响应结果成功判断
     *
     * @param ret 响应结果
     * @return 响应结果是否成功, 成功返回 true, 失败返回 false
     */
    public static <T> Boolean isSuccess(R<T> ret) {
        return HttpStatus.HTTP_OK == ret.getCode();
    }
}
