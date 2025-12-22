package com.ranyk.model.business.userinfo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: UserBase.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 用户基本信息实体类封装对象
 * @date: 2025-10-11
 */
@Getter
@Setter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_base_info")
public class UserBase implements Serializable {

    @Serial
    private static final long serialVersionUID = 7593890464181872993L;
    /**
     * 主键 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "BIGINT AUTO_INCREMENT COMMENT '主键 ID'")
    private Long id;
    /**
     * 用户全名
     */
    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(500) COMMENT '用户全名'")
    private String userName;
    /**
     * 用户姓氏
     */
    @Column(name = "user_last_name", nullable = false, columnDefinition = "varchar(500) COMMENT '用户姓氏'")
    private String lastName;
    /**
     * 用户名字
     */
    @Column(name = "user_first_name", nullable = false, columnDefinition = "varchar(500) COMMENT '用户名字'")
    private String firstName;
    /**
     * 用户性别 0: 未知(默认); 1: 男; 2: 女;
     */
    @Column(name = "user_sex", nullable = false, columnDefinition = "TINYINT DEFAULT 0 COMMENT '用户性别 0: 未知(默认); 1: 男; 2: 女;'")
    private Integer sex;
    /**
     * 用户昵称
     */
    @Column(name = "user_nick_name", nullable = false, columnDefinition = "varchar(500) COMMENT '用户昵称'")
    private String nickName;
    /**
     * 用户座机/固定电话
     */
    @Column(name = "user_fixed_line_phone", nullable = false, columnDefinition = "varchar(500) COMMENT '用户座机/固定电话'")
    private String fixedLinePhone;
    /**
     * 用户电话
     */
    @Column(name = "user_phone", nullable = false, columnDefinition = "varchar(500) COMMENT '用户电话'")
    private String phone;
    /**
     * 用户邮箱
     */
    @Column(name = "user_email", nullable = false, columnDefinition = "varchar(500) COMMENT '用户邮箱'")
    private String email;
    /**
     * 用户状态: -1: 删除; 0: 无效; 1: 正常(默认);
     */
    @Column(name = "user_status", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '用户状态: -1: 删除; 0: 无效; 1: 正常(默认);'")
    private Integer status;
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
