package com.ranyk.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * CLASS_NAME: DateTimeUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 日期时间处理工具类
 * @date: 2026-01-09
 */
public class DateTimeUtils {

    public static final String DATE_TIME_DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_DEFAULT_FORMAT = "yyyy-MM-dd";

    public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";

    public static final String DATE_YYYYMMDD_FORMAT = "yyyyMMdd";

    public static final String DATE_TIME_YYYYMMDDHHMMSS_FORMAT = "yyyyMMddHHmmss";

    /**
     * 获取日期字符串
     *
     * @param date   日期对象 {@link LocalDate} 对象
     * @param format 日期格式, 参见 {@link DateTimeUtils} 中的常量
     * @return 返回日期字符串
     */
    public static String getDateStr(LocalDate date, String format){
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 获取日期时间字符串
     *
     * @param date   日期时间对象 {@link LocalDateTime} 对象
     * @param format 日期时间格式, 参见 {@link DateTimeUtils} 中的常量
     * @return 返回日期时间字符串
     */
    public static String getDateTimeStr(LocalDateTime date, String format){
        return date.format(DateTimeFormatter.ofPattern(format));
    }
}
