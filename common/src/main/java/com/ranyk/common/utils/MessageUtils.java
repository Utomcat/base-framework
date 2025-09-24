package com.ranyk.common.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * CLASS_NAME: MessageUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 获取 i18n 资源工具类
 * @date: 2025-09-25
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {
    /**
     * 获取 MessageSource 对象
     */
    private static final MessageSource MESSAGE_SOURCE = SpringUtil.getBean(MessageSource.class);

    /**
     * 根据 消息码 和 参数 获取对应国际化的错误信息
     *
     * @param code 消息码
     * @param args 参数
     * @return java.lang.String 获取到的国际化翻译值
     */
    public static String message(String code, Object... args){
        try {
            return MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
        }
        catch (Exception e){
            if (e instanceof NoSuchMessageException){
                return code;
            }
            else {
                return e.getMessage();
            }
        }
    }
}
