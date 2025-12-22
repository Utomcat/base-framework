package com.ranyk.model.page.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: PageVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 后端返回前端的分页查询结果封装对象
 * @date: 2025-12-22
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PageVO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 6293626186909343823L;
    /**
     * 数据列表
     */
    private T data;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 数据总数
     */
    private Long total;

}
