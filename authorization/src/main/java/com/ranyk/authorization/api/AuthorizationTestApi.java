package com.ranyk.authorization.api;

import com.ranyk.model.business.login.vo.TestOneVO;
import com.ranyk.model.response.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: AuthorizationTestApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 授权模块测试接口 API 类
 * @date: 2025-09-25
 */
@RestController
@RequestMapping("/authorization/test")
public class AuthorizationTestApi {

    /**
     * 测试接口 one
     *
     * @return 返回结果 {@link TestOneVO}
     */
    @GetMapping("/one")
    public R<TestOneVO> one() {
        return R.ok(TestOneVO.builder().result("授权模块测试接口 one").build());
    }

}
