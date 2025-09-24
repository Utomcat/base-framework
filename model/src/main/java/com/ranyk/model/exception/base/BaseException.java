package com.ranyk.model.exception.base;

import cn.hutool.core.util.StrUtil;
import com.ranyk.common.utils.MessageUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 * CLASS_NAME: BaseException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 基础异常类
 * @date: 2025-09-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 8862542290087224125L;

    /**
     * 所属模块
     */
    private String module;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误码对应的参数
     */
    private Object[] args;
    /**
     * 错误消息
     */
    private String defaultMessage;

    /**
     * 创建一个 BaseException 实例对象
     *
     * @param module       模块
     * @param code         错误码
     * @param args         错误码对应的参数
     */
    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    /**
     * 创建一个 BaseException 实例对象
     *
     * @param module       模块
     * @param defaultMessage 默认消息
     */
    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    /**
     * 创建一个 BaseException 实例对象
     *
     * @param code         错误码
     * @param args         错误码对应的参数
     */
    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    /**
     * 创建一个 BaseException 实例对象
     *
     * @param defaultMessage 默认消息
     */
    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    /**
     * 获取错误消息 如果对应的错误码不为空,则返回通过该错误码获取对应的国际化错误消息,否则返回默认消息
     *
     * @return 返回错误消息
     */
    @Override
    public String getMessage() {
        String message = null;
        if (StrUtil.isNotEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        else  {
            message = defaultMessage;
        }
        return message;
    }

}
