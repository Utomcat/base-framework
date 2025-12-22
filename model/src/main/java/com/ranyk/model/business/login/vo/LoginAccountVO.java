package com.ranyk.model.business.login.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: LoginAccountVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 后端返回前端查询的登录账户信息数据封装对象类
 * @date: 2025-12-19
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginAccountVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4838463844307536131L;
    /**
     * 账户数据主键 ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 登录账户状态: -2: 停用/注销; -1: 禁用; 0: 锁定; 1: 启用;
     */
    private byte accountStatus;
    /**
     * 账户数据创建时间
     */
    private LocalDateTime createTime;
    /**
     * 账户数据创建人 ID
     */
    private Long createId;
    /**
     * 账户数据更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 账户数据更新人 ID
     */
    private Long updateId;
}
