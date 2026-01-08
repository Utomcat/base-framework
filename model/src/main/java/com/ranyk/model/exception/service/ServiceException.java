package com.ranyk.model.exception.service;

import com.ranyk.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Objects;

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
public class ServiceException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4862699970194712172L;

    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误码对应的参数
     */
    private Object[] args;
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
     * @param code    错误码
     */
    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }


    /**
     * 创建一个 ServiceException 错误对象
     *
     * @param message 错误信息
     * @param args    错误参数数组
     */
    public ServiceException(String message, Object... args) {
        this.message = message;
        this.args = args;
    }

    /**
     * 获取错误消息 如果对应的错误码不为空,则返回通过该错误码获取对应的国际化错误消息,否则返回默认消息
     *
     * @return 返回错误消息
     */
    @Override
    public String getMessage() {
        String messageStr;
        if (Objects.nonNull(code)) {
            messageStr = MessageUtils.message(String.valueOf(code), args);
        } else {
            messageStr = Objects.nonNull(message) ? MessageUtils.message(message, args) : MessageUtils.message(detailMessage, args);
        }
        return messageStr;
    }
}
