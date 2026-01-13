package com.ranyk.model.base.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CLASS_NAME: BaseDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 基础数据接收和传输对象公共属性字段
 * @date: 2025-12-23
 */
@Data
@ToString
@SuperBuilder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6218604120770125160L;

    // 以下是基础数据接收和传输对象公共属性字段

    /**
     * 数据主键 ID
     */
    private Long id;
    /**
     * 数据备注
     */
    private String remark;
    /**
     * 角色数据创建时间
     */
    private LocalDateTime createTime;
    /**
     * 角色数据创建人 ID
     */
    private Long createId;
    /**
     * 角色数据更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 角色数据更新人 ID
     */
    private Long updateId;

    // 以下是额外的数据公共字段

    /**
     * 数据主键 ID 列表
     */
    private List<Long> ids;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页显示数量
     */
    private Integer pageSize;

}
