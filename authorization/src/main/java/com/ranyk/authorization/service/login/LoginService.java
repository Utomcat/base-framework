package com.ranyk.authorization.service.login;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.ranyk.authorization.service.account.AccountService;
import com.ranyk.authorization.service.user.UserService;
import com.ranyk.model.business.account.dto.AccountDTO;
import com.ranyk.model.business.userinfo.dto.UserBaseDTO;
import com.ranyk.model.exception.user.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * CLASS_NAME: LoginService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 登录业务逻辑类
 * @date: 2025-10-10
 */
@Service
public class LoginService {

    /**
     * 账户业务逻辑对象
     */
    private final AccountService accountService;
    /**
     * 用户信息业务逻辑对象
     */
    private final UserService userService;

    /**
     * 构造方法
     *
     * @param accountService 账户业务逻辑对象
     * @param userService    用户信息业务逻辑对象
     */
    @Autowired
    public LoginService(AccountService accountService,
                        UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }


    /**
     * 登录业务逻辑方法
     *
     * @param accountDTO 登录参数封装对象 {@link AccountDTO}
     * @return 登录结果数据传输对象 {@link AccountDTO}, 此返回对象和入参是两个对象,故不能混为一谈
     */
    public AccountDTO login(AccountDTO accountDTO) {
        // 通过传入的用户名和密码进行账户户对象的查询
        AccountDTO queryAccountDTO = accountService.queryLoginAccountByUserNameAndPassword(accountDTO);
        // 判断是否存在用户 id
        if (Objects.isNull(queryAccountDTO.getId())) {
            // 不存在用户 id 则抛出用户登录异常
            throw new UserException("user.password.not.match");
        }
        // 存在用户 id 则进行登录
        StpUtil.login(queryAccountDTO.getId());
        // 获取本次登录的 token 对象
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 获取当前登录账户有关的用户信息 ID
        AccountDTO userInfoDTO = accountService.queryUserInfoIdByAccountId(AccountDTO.builder().id(queryAccountDTO.getId()).build());
        // 获取当前登录账户有关的用户信息
        UserBaseDTO userBaseDTO = userService.queryUserInfoById(UserBaseDTO.builder().id(userInfoDTO.getUserInfoId()).build());
        // 返回登录结果数据传输对象, 此对象中存在用户的登录 token
        return AccountDTO.builder().tokenName(tokenInfo.getTokenName()).tokenValue(tokenInfo.getTokenValue()).userName(queryAccountDTO.getUserName()).avatar(userBaseDTO.getAvatar()).userAccount(userBaseDTO.getUserName()).build();
    }
}
