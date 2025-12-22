package com.ranyk.common.constant;

import lombok.Getter;

/**
 * CLASS_NAME: PageKeyEnum.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 分页查询 key 枚举类
 * @date: 2025-12-19
 */
@Getter
public enum PageKeyEnum {
    /**
     * 数据列表 key
     */
    PAGE_DATA("data", "数据列表"),
    /**
     * 数据总数 key
     */
    PAGE_TOTAL("total", "数据总数"),
    /**
     * 当前页码 key
     */
    PAGE_NUM("pageNum", "当前页码"),
    /**
     * 总页数 key
     */
    TOTAL_PAGE_NUM("totalPageNum", "总页数"),
    ;

    /**
     * key 值
     */
    private final String key;
    /**
     * key 名称
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param key  key 值
     * @param name key 名称
     */
    PageKeyEnum(String key, String name) {
        this.key = key;
        this.name = name;
    }
}
