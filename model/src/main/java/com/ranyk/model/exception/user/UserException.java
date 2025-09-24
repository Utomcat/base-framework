package com.ranyk.model.exception.user;

import com.ranyk.model.exception.base.BaseException;

import java.io.Serial;

/**
 * CLASS_NAME: UserException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户信息基础异常类
 * @date: 2025-09-25
 */
public class UserException extends BaseException {
    @Serial
    private static final long serialVersionUID = -3430091376493790556L;

    /**
     * 创建一个 UserException 实例对象
     *
     * @param code         错误码
     * @param args         错误码对应的参数
     */
    public UserException(String code, Object... args) {
        super("user", code, args, null);
    }
}
