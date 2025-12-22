package com.ranyk.model.exception.user;

import java.io.Serial;

/**
 * CLASS_NAME: CaptchaExpireException.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码过期异常类
 * @date: 2025-09-25
 */
public class CaptchaExpireException extends UserException{

    @Serial
    private static final long serialVersionUID = -4538862551681645387L;

    /**
     * 创建一个 CaptchaExpireException 实例对象
     */
    public CaptchaExpireException() {
        super("user.captcha.expire");
    }
}
