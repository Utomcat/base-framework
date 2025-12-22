package com.ranyk.model.business.login.dto;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * CLASS_NAME: AccountDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端的数据接受对象或后端业务类和业务类之间相互传递的实体封装类
 * @date: 2025-10-10
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6394345823746679769L;
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
    private Integer accountStatus;
    /**
     * 登录成功后的 SA-TOKEN 认证令牌 token 名称
     */
    public String tokenName;
    /**
     * 登录成功后的 SA-TOKEN 认证令牌 token 值
     */
    public String tokenValue;
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
    /**
     * 查询的当前页码, 默认 0 (当前页码 - 1)
     */
    private Integer pageNum = 0;
    /**
     * 查询的每页数量, 默认 10
     */
    private Integer pageSize = 10;
}
