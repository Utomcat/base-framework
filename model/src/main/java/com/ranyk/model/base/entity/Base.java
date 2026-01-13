package com.ranyk.model.base.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: Base.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 数据库映射实体公共属性封装对象类
 * @date: 2026-01-08
 */
@Getter
@Setter
@ToString
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class Base implements Serializable {

    @Serial
    private static final long serialVersionUID = -3328172184947408233L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
    /**
     * 数据备注
     */
    @Column(name = "remark", columnDefinition = "VARCHAR(1000) COMMENT '数据备注'")
    private String remark;
    /**
     * 数据创建时间
     */
    @Column(name = "create_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间'")
    private LocalDateTime createTime;
    /**
     * 数据创建人 ID
     */
    @Column(name = "create_id", nullable = false, columnDefinition = "BIGINT DEFAULT 1 COMMENT '数据创建人 ID'")
    private Long createId;
    /**
     * 数据更新时间
     */
    @Column(name = "update_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '数据更新时间'")
    private LocalDateTime updateTime;
    /**
     * 数据更新人 ID
     */
    @Column(name = "update_id", nullable = false, columnDefinition = "BIGINT DEFAULT 1 COMMENT '数据更新人 ID'")
    private Long updateId;
}
