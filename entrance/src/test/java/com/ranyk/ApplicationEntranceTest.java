package com.ranyk;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CLASS_NAME: ApplicationEntranceTest.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 系统入口单元测试类
 * @date: 2025-09-25
 */
@Slf4j
@SpringBootTest
public class ApplicationEntranceTest {

    /**
     * 测试 Hutool 工具类方法 - 加密解密 - 摘要算法 MD5 方法测试
     */
    @Test
    void test0() {
        String str = "123456";
        // 对 123456 进行加密的结果是  e10adc3949ba59abbe56e057f20f883e
        log.info("对 str {} 字符串进行 MD5 方法进行加密的结果是 {}", str, DigestUtil.md5Hex(str));
    }
}
