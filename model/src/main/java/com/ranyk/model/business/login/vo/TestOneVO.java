package com.ranyk.model.business.login.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: TestOneVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 测试接口 one 返回前端实体对象
 * @date: 2025-10-10
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TestOneVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8760813176474199449L;

    /**
     * 测试接口 one 返回结果
     */
    private String result;
}
