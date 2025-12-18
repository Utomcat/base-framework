package com.ranyk.core.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.fasterxml.jackson.core.JsonParseException;
import com.ranyk.model.exception.base.BaseException;
import com.ranyk.model.exception.service.ServiceException;
import com.ranyk.model.response.R;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: GlobalExceptionHandler.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 全局异常监听处理器
 * @date: 2025-10-11
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求方式不支持异常监听器
     *
     * @param e       请求方式不支持异常对象 {@link HttpRequestMethodNotSupportedException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public R<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return R.fail(HttpStatus.HTTP_BAD_METHOD, e.getMessage());
    }

    /**
     * 业务异常监听器
     *
     * @param e       业务异常对象 {@link ServiceException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e, HttpServletRequest request) {
        log.error(e.getMessage());
        Integer code = e.getCode();
        return ObjectUtil.isNotNull(code) ? R.fail(code, e.getMessage()) : R.fail(e.getMessage());
    }

    /**
     * servlet 异常监听器
     *
     * @param e       servlet 异常对象 {@link ServletException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(ServletException.class)
    public R<Void> handleServletException(ServletException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return R.fail(e.getMessage());
    }

    /**
     * 基础异常监听器
     *
     * @param e       基础异常对象 {@link BaseException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @SuppressWarnings("unused")
    @ExceptionHandler(BaseException.class)
    public R<Void> handleBaseException(BaseException e, HttpServletRequest request) {
        log.error(e.getMessage());
        return R.fail(e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量异常监听器
     *
     * @param e       请求路径中缺少必需的路径变量异常对象 {@link MissingPathVariableException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public R<Void> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", requestURI);
        return R.fail(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }

    /**
     * 请求参数类型不匹配异常监听器
     *
     * @param e       请求参数类型不匹配异常对象 {@link MethodArgumentTypeMismatchException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public R<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI);
        return R.fail(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(), Objects.isNull(e.getRequiredType()) ? "" : e.getRequiredType().getName(), e.getValue()));
    }

    /**
     * 找不到路由异常监听器
     *
     * @param e       找不到路由异常对象 {@link NoHandlerFoundException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}'不存在.", requestURI);
        return R.fail(HttpStatus.HTTP_NOT_FOUND, e.getMessage());
    }

    /**
     * 拦截 IO 异常监听器
     *
     * @param e       IO 异常对象 {@link IOException}
     * @param request 请求对象 {@link HttpServletRequest}
     */
    @ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public void handleRuntimeException(IOException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("sse")) {
            // sse 经常性连接中断 例如关闭浏览器 直接屏蔽
            return;
        }
        log.error("请求地址'{}',连接中断", requestURI, e);
    }

    /**
     * 拦截未知的运行时异常监听器
     *
     * @param e       运行时异常对象 {@link RuntimeException}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return R.fail(e.getMessage());
    }

    /**
     * 系统级别异常监听器
     *
     * @param e       系统级别异常对象 {@link Exception}
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return R.fail(e.getMessage());
    }

    /**
     * 参数绑定异常监听器
     *
     * @param e 参数绑定异常对象 {@link BindException}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(BindException.class)
    public R<Void> handleBindException(BindException e) {
        log.error(e.getMessage());
        if (CollUtil.isEmpty(e.getAllErrors())) {
            return R.fail();
        }
        String message = e.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).filter(Objects::nonNull).collect(Collectors.joining(", "));
        return R.fail(message);
    }

    /**
     * JSON 解析异常（Jackson 在处理 JSON 格式出错时抛出）
     * 可能是请求体格式非法，也可能是服务端反序列化失败
     *
     * @param e       {@link JsonParseException} 异常对象
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(JsonParseException.class)
    public R<Void> handleJsonParseException(JsonParseException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}' 发生 JSON 解析异常: {}", requestURI, e.getMessage());
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, "请求数据格式错误（JSON 解析失败）：" + e.getMessage());
    }

    /**
     * 请求体读取异常（通常是请求参数格式非法、字段类型不匹配等） 监听器
     *
     * @param e       {@link HttpMessageNotReadableException} 异常对象
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 响应结果对象 {@link R}
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("请求地址'{}', 参数解析失败: {}", request.getRequestURI(), e.getMessage());
        return R.fail(HttpStatus.HTTP_BAD_REQUEST, "请求参数格式错误：" + e.getMostSpecificCause().getMessage());
    }
}
