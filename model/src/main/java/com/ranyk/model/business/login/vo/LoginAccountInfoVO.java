package com.ranyk.model.business.login.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: LoginVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录信息后端返回前端结果对象
 * @date: 2025-10-10
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginAccountInfoVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2288064358062674044L;

    /**
     * 登录成功后的 SA-TOKEN 认证令牌 token 名称
     */
    private String tokenName;
    /**
     * 登录成功后的 SA-TOKEN 认证令牌 token 值
     */
    private String tokenValue;
    /**
     * 登录账户的账户名
     */
    private String account;
    /**
     * 登录账户的用户名
     */
    private String userAccount;
    /**
     * 登录账户的头像
     */
    private String avatar;
}
