package com.ranyk.authorization.service.login;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.ranyk.authorization.repository.loginAccount.LoginAccountRepository;
import com.ranyk.common.constant.AccountStatusEnum;
import com.ranyk.model.business.login.dto.LoginAccountDTO;
import com.ranyk.model.business.login.entity.LoginAccount;
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
     * 登录业务数据库操作对象
     */
    private final LoginAccountRepository loginAccountRepository;

    /**
     * 构造方法
     *
     * @param loginRepository 登录业务数据库操作对象
     */
    @Autowired
    public LoginService(LoginAccountRepository loginAccountRepository) {
        this.loginAccountRepository = loginAccountRepository;
    }


    /**
     * 登录业务逻辑方法
     *
     * @param loginDTO 登录参数封装对象 {@link LoginAccountDTO}
     * @return 登录结果数据传输对象 {@link LoginAccountDTO}, 此返回对象和入参是两个对象,故不能混为一谈
     */
    public LoginAccountDTO login(LoginAccountDTO loginDTO) {
        // 通过传入的用户名和密码进行用户对象的查询, 当未查询到时, 返回一个新建的 Login 对象, 此对象没有数据 id
        LoginAccount login = loginAccountRepository.findByUserNameAndPasswordAndAccountStatusEquals(loginDTO.getUserName(), DigestUtil.md5Hex(loginDTO.getPassword()), AccountStatusEnum.ENABLED.getCode()).orElse(LoginAccount.builder().build());
        // 判断是否存在用户 id
        if (Objects.isNull(login.getId())){
            // 不存在用户 id 则抛出用户登录异常
            throw new UserException("user.password.not.match");
        }
        // 存在用户 id 则进行登录
        StpUtil.login(login.getId());
        // 获取本次登录的 token 对象
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        // 返回登录结果数据传输对象, 此对象中存在用户的登录 token
        return LoginAccountDTO.builder().tokenName(tokenInfo.getTokenName()).tokenValue(tokenInfo.getTokenValue()).build();
    }
}
