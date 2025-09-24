package com.ranyk.model.exception.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * CLASS_NAME: ServiceException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务异常类
 * @date: 2025-09-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 4862699970194712172L;

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    /**
     * 创建一个 ServiceException 错误对象
     *
     * @param message 错误信息
     */
    public ServiceException(String message) {
        this.message = message;
    }

    /**
     * 创建一个 ServiceException 错误对象
     *
     * @param message 错误信息
     * @param code 错误码
     */
    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
}
