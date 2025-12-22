package com.ranyk.model.exception.user;

import java.io.Serial;

/**
 * CLASS_NAME: CaptchaException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码错误异常类
 * @date: 2025-09-25
 */
public class CaptchaException extends UserException{

    @Serial
    private static final long serialVersionUID = -5962775378402624183L;

    /**
     * 创建一个 CaptchaException 错误实例对象
     */
    public CaptchaException() {
        super("user.captcha.error");
    }
}
