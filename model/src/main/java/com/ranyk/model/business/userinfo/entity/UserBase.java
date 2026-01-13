package com.ranyk.model.business.userinfo.entity;

import com.ranyk.model.base.entity.Base;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

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
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_base_info")
public class UserBase extends Base implements Serializable {

    @Serial
    private static final long serialVersionUID = 7593890464181872993L;
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
    @Column(name = "user_nick_name", nullable = false, columnDefinition = "varchar(500) DEFAULT '-' COMMENT '用户昵称'")
    private String nickName;
    /**
     * 用户座机/固定电话
     */
    @Column(name = "user_fixed_line_phone", nullable = false, columnDefinition = "varchar(500) DEFAULT '-' COMMENT '用户座机/固定电话'")
    private String fixedLinePhone;
    /**
     * 用户电话
     */
    @Column(name = "user_phone", nullable = false, columnDefinition = "varchar(500) DEFAULT '-' COMMENT '用户电话'")
    private String phone;
    /**
     * 用户邮箱
     */
    @Column(name = "user_email", nullable = false, columnDefinition = "varchar(500) DEFAULT '-' COMMENT '用户邮箱'")
    private String email;
    /**
     * 用户头像地址
     */
    @Column(name = "user_avatar", nullable = false, columnDefinition = "varchar(1000) DEFAULT 'C:\\Users\\ranyk\\Pictures\\fHe1eSEYZ.png' COMMENT '用户头像地址'")
    private String avatar;
    /**
     * 用户状态: -1: 删除; 0: 无效; 1: 正常(默认);
     */
    @Column(name = "user_status", nullable = false, columnDefinition = "TINYINT DEFAULT 1 COMMENT '用户状态: -1: 删除; 0: 无效; 1: 正常(默认);'")
    private Integer status;
}
