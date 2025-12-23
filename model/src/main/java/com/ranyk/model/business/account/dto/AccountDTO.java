package com.ranyk.model.business.account.dto;

import com.ranyk.model.base.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: AccountDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入后端的数据接受对象或后端业务类和业务类之间相互传递的实体封装类
 * @date: 2025-10-10
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountDTO extends BaseDTO implements Serializable {

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
}
